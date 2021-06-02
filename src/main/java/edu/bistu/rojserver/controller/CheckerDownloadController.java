package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.entity.CheckerEntity;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.exceptions.ProblemNotFoundException;
import edu.bistu.rojserver.service.CheckerService;
import edu.bistu.rojserver.service.ProblemService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shared.Checker;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

@Controller
public class CheckerDownloadController
{
    @Resource
    private CheckerService checkerService;

    @Resource
    private ProblemService problemService;

    @GetMapping("download_checker")
    public ResponseEntity<byte[]> downloadChecker(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "id") Long checkerID)
    {
        DownloadFile file = checkerService.downloadChecker(checkerID);
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(file.getFileName(), StandardCharsets.UTF_8).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).body(file.getFile());
    }

    @GetMapping("/api/download_checker")
    public void judgeNodeDownloadChecker(@RequestParam(name = "problemID") Long problemID, HttpServletResponse response) throws ProblemNotFoundException, IOException
    {
        ProblemEntity problemEntity = problemService.getProblemByID(problemID);
        if(problemEntity == null)
            throw new ProblemNotFoundException();
        CheckerEntity checkerEntity = problemService.getCheckerByProblemIfExists(problemEntity);
        if(checkerEntity == null)
            throw new ProblemNotFoundException("this problem doesn't have checker");
        DownloadFile file = checkerService.downloadChecker(checkerEntity.getCheckerID());
        Checker checker = new Checker();
        checker.setFileName(file.getFileName());
        checker.setArr(file.getFile());
        checker.setLanguageName(checkerEntity.getLanguageEntity().getName());
        ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(response.getOutputStream()));
        outputStream.writeObject(checker);
        outputStream.flush();
        outputStream.close();
    }
}

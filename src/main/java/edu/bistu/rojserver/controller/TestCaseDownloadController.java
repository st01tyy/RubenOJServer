package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.TestCaseFileEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.exceptions.InternalDataException;
import edu.bistu.rojserver.exceptions.TestCaseNotFoundException;
import edu.bistu.rojserver.exceptions.UnAuthorizedException;
import edu.bistu.rojserver.service.ProblemService;
import edu.bistu.rojserver.service.TestCaseService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/download_testcase")
public class TestCaseDownloadController
{
    @Resource
    private TestCaseService testCaseService;

    @Resource
    private ProblemService problemService;

    @GetMapping("/input")
    public ResponseEntity<byte[]> downloadInputFile(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "caseID") Long caseID) throws TestCaseNotFoundException, UnAuthorizedException, InternalDataException
    {
        if(!testCaseService.isTestCaseExist(caseID))
            throw new TestCaseNotFoundException();

        if(!testCaseService.hasPermissionOnTestCase(userEntity, caseID))
            throw new UnAuthorizedException();

        DownloadFile file = testCaseService.downloadInput(caseID);

        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(file.getFileName(), StandardCharsets.UTF_8).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).body(file.getFile());
    }

    @GetMapping("/output")
    public ResponseEntity<byte[]> downloadOutputFile(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "caseID") Long caseID) throws TestCaseNotFoundException, UnAuthorizedException, InternalDataException
    {
        if(!testCaseService.isTestCaseExist(caseID))
            throw new TestCaseNotFoundException();

        if(!testCaseService.hasPermissionOnTestCase(userEntity, caseID))
            throw new UnAuthorizedException();

        DownloadFile file = testCaseService.downloadOutput(caseID);

        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(file.getFileName(), StandardCharsets.UTF_8).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).body(file.getFile());
    }
}

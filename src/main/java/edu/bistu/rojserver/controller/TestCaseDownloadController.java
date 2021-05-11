package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
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
    public ResponseEntity<byte[]> downloadInputFile(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "caseID") Long caseID) throws TestCaseNotFoundException, UnAuthorizedException
    {
        TestCaseEntity testCaseEntity = testCaseService.getTestCaseEntityByCaseID(caseID);
        if(testCaseEntity == null)
            throw new TestCaseNotFoundException();

        if(!hasPermissionOnProblem(userEntity, testCaseEntity.getProblemEntity().getProblemID()))
            throw new UnAuthorizedException();

        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(testCaseEntity.getInputFileName(), StandardCharsets.UTF_8).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).body(testCaseEntity.getInput());
    }

    @GetMapping("/output")
    public ResponseEntity<byte[]> downloadOutputFile(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "caseID") Long caseID) throws TestCaseNotFoundException, UnAuthorizedException
    {
        TestCaseEntity testCaseEntity = testCaseService.getTestCaseEntityByCaseID(caseID);
        if(testCaseEntity == null)
            throw new TestCaseNotFoundException();

        if(!hasPermissionOnProblem(userEntity, testCaseEntity.getProblemEntity().getProblemID()))
            throw new UnAuthorizedException();

        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(testCaseEntity.getOutputFileName(), StandardCharsets.UTF_8).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).body(testCaseEntity.getOutput());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean hasPermissionOnProblem(UserEntity userEntity, Long problemID)
    {
        if(userEntity.getRole() == UserEntity.Role.USER)
            return false;
        ProblemEntity problemEntity = problemService.getProblemByID(problemID);
        return userEntity.getRole() != UserEntity.Role.PROBLEM_AUTHOR || userEntity.getUserID().equals(problemEntity.getAuthor().getUserID());
    }
}

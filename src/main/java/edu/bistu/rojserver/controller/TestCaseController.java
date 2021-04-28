package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.service.TestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shared.TestCase;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Controller
@RequestMapping("/api/download_test_case")
@Slf4j
public class TestCaseController
{
    @Resource
    private TestCaseService testCaseService;

    @GetMapping
    public void downloadTestCase(@RequestParam(name = "problemID") Long problemID, HttpServletResponse response) throws IOException
    {
        log.info("test case download request on " + problemID);
        TestCase[] cases = testCaseService.getTestCasesByProblemID(problemID);
        if(cases == null || cases.length == 0)
        {
            response.setStatus(500);
            log.info("return code 500");
        }
        else
        {
            response.setStatus(200);
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(response.getOutputStream()));
            outputStream.writeObject(cases);
            outputStream.flush();
            outputStream.close();
            log.info("return code 200");
        }
    }
}

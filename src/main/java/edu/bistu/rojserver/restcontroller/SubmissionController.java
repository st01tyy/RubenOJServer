package edu.bistu.rojserver.restcontroller;

import edu.bistu.rojserver.domain.jsonmodel.JudgeResult;
import edu.bistu.rojserver.service.SubmissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/update_result")
public class SubmissionController
{
    @Resource
    private SubmissionService submissionService;

    @PostMapping
    public void updateSubmissionResult(@RequestBody JudgeResult judgeResult)
    {
        submissionService.updateSubmissionResult(judgeResult);
    }
}

package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.SubmissionFetchForm;
import edu.bistu.rojserver.domain.SubmissionFetchResult;
import edu.bistu.rojserver.service.SubmissionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/submission")
public class SubmissionPageController
{
    @Resource
    private SubmissionService submissionService;

    @GetMapping
    public String showSubmissionPage(@AuthenticationPrincipal UserEntity userEntity, Model model)
    {
        List<SubmissionEntity> list = submissionService.getSubmissionsByAuthor(userEntity);
        list.sort((submissionEntity, t1) -> {
            if(submissionEntity.getSubmissionID() > t1.getSubmissionID())
                return -1;
            else
                return 1;
        });
        model.addAttribute("submissions", list);
        return "submission";
    }

    @PostMapping("/auto_fetch")
    @ResponseBody
    public SubmissionFetchResult[] fetchResults(@RequestBody SubmissionFetchForm[] arr)
    {
        return submissionService.fetchResults(arr);
    }
}

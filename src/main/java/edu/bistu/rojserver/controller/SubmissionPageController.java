package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.SubmissionFetchForm;
import edu.bistu.rojserver.domain.SubmissionFetchResult;
import edu.bistu.rojserver.service.SubmissionService;
import org.springframework.data.domain.Slice;
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
    public String showSubmissionPage(@RequestParam(name = "page", required = false) Integer page, @AuthenticationPrincipal UserEntity userEntity, Model model)
    {
        Slice<SubmissionEntity> slice = submissionService.getSubmissionList(page, userEntity);
        model.addAttribute("submissions", slice.getContent());
        model.addAttribute("hasPrevious", slice.hasPrevious());
        model.addAttribute("hasNext", slice.hasNext());
        model.addAttribute("page", slice.getNumber());
        return "submission";
    }

    @PostMapping("/auto_fetch")
    @ResponseBody
    public SubmissionFetchResult[] fetchResults(@RequestBody SubmissionFetchForm[] arr)
    {
        return submissionService.fetchResults(arr);
    }
}

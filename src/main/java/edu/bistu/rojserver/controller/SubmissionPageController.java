package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.service.SubmissionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
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
        model.addAttribute("submissions", list);
        return "submission";
    }
}

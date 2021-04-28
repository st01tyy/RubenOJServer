package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.TestCaseUploadForm;
import edu.bistu.rojserver.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
@RequestMapping("/problem_management")
@Slf4j
public class ProblemManagementController
{
    @Resource
    private ProblemService problemService;

    @GetMapping
    public String showProblemManagementPage(@AuthenticationPrincipal UserEntity userEntity, Model model)
    {
        model.addAttribute("problems", problemService.getProblemsByAuthor(userEntity));
        return "template_problem_management";
    }

    @GetMapping("/edit_problem")
    public String showEditProblemPage(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(required = false, name = "id") Long problemID, Model model)
    {
        ProblemEntity problemEntity = null;
        if(problemID != null)
            problemEntity = problemService.getProblemByID(problemID);
        if(problemEntity != null)
        {
            if(!problemEntity.getAuthor().getUserID().equals(userEntity.getUserID()))
                problemEntity = null;
        }
        if(problemEntity == null)
        {
            problemEntity = new ProblemEntity();
            problemEntity.setTitle("题目标题");
            problemEntity.setTimeLimit(1);
            problemEntity.setMemoryLimit(256);
        }
        model.addAttribute("problem", problemEntity);
        return "template_edit_problem";
    }

    @PostMapping("/edit_problem")
    public String processEditProblem(@AuthenticationPrincipal UserEntity userEntity, ProblemEntity problemEntity)
    {
        problemService.editProblem(problemEntity, userEntity);
        return "redirect:/problem_management";
    }

    @PostMapping("/upload_test_case")
    public String uploadTestCase(TestCaseUploadForm form) throws IOException
    {
        log.info("upload()");
        problemService.uploadTestCase(form);
        return "redirect:/problem_management";
    }

}

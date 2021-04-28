package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.LanguageEntity;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.SubmitForm;
import edu.bistu.rojserver.service.LanguageService;
import edu.bistu.rojserver.service.ProblemService;
import edu.bistu.rojserver.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/problem")
@Slf4j
public class ProblemController
{
    @Resource
    private ProblemService problemService;

    @Resource
    private LanguageService languageService;

    @Resource
    private SubmissionService submissionService;

    @GetMapping
    public String showProblemPage(@RequestParam(name = "id") Long problemID, Model model, HttpServletResponse response)
    {
        ProblemEntity problemEntity = problemService.getProblemByID(problemID);
        if(problemEntity == null)
        {
            response.setStatus(405);
            return "redirect:/error";
        }
        List<LanguageEntity> languageList = languageService.getAllLanguages();
        model.addAttribute("problemID", problemID);
        model.addAttribute("problem", problemEntity);
        model.addAttribute("languages", languageList);
        return "template_problem";
    }

    @PostMapping("/submit")
    public String processSubmission(@AuthenticationPrincipal UserEntity userEntity, SubmitForm submitForm) throws IOException
    {
        Long id = submissionService.createSubmission(userEntity, submitForm);
        log.info("created submission id = " + id);
        return "redirect:/submission";
    }
}

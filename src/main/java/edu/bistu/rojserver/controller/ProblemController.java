package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.ProblemTableItem;
import edu.bistu.rojserver.dao.entity.LanguageEntity;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.SubmitForm;
import edu.bistu.rojserver.domain.TestSubmitForm;
import edu.bistu.rojserver.exceptions.SubmissionCreateException;
import edu.bistu.rojserver.service.LanguageService;
import edu.bistu.rojserver.service.ProblemService;
import edu.bistu.rojserver.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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

    @GetMapping("/list")
    public String showProblemListPage(@RequestParam(name = "page", required = false) Integer page, Model model)
    {
        log.info("request page: " + page);
        int pageCount = problemService.getProblemTablePageCount();
        log.info("page count: " + pageCount);
        if(pageCount == 0)
        {
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("problems", new ArrayList<>(0));
        }
        else
        {
            page = (page == null || page < 1) ? 1 : page;
            page = (page > pageCount) ? pageCount : page;
            log.info("valid page: " + page);
            List<ProblemTableItem> problems;
            problems = problemService.getProblemTableItems(page);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("problems", problems);
            model.addAttribute("pages", problemService.getProblemTablePages(page, pageCount));
        }
        return "template_problem_list";
    }

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
    public String processSubmission(@AuthenticationPrincipal UserEntity userEntity, SubmitForm submitForm) throws SubmissionCreateException, IOException
    {
        Long id = submissionService.createSubmission(userEntity, submitForm);
        log.info("created submission id = " + id);
        return "redirect:/submission";
    }

    @PostMapping("/submit_load_test")
    @ResponseBody
    public boolean loadTest(TestSubmitForm form)
    {
        try
        {
            submissionService.loadTest(form);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

}

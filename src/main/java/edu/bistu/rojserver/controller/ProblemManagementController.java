package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.*;
import edu.bistu.rojserver.exceptions.*;
import edu.bistu.rojserver.service.LanguageService;
import edu.bistu.rojserver.service.ProblemService;
import edu.bistu.rojserver.service.TestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.IOException;

@Controller
@RequestMapping("/management/problem")
@Slf4j
public class ProblemManagementController
{
    @Resource
    private ProblemService problemService;

    @Resource
    private TestCaseService testCaseService;

    @Resource
    private LanguageService languageService;

    @GetMapping
    public String showProblemManagementPage(@AuthenticationPrincipal UserEntity userEntity, Model model)
    {
        model.addAttribute("problems", problemService.getProblemsByAuthor(userEntity));
        return "template_problem_management";
    }

    @GetMapping("/public")
    public String showPublicPage(@RequestParam(name = "id") Long problemID, Model model)
    {
        model.addAttribute("problemID", problemID);
        return "public";
    }

    @PostMapping("public")
    public String setProblemStatusPublic(ProblemPublicRequest request)
    {
        problemService.setProblemStatusPublic(request.getProblemID());
        return "redirect:/management/problem";
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
        Long res = problemService.editProblem(problemEntity, userEntity);
        if(res != null)
            return "redirect:/management/problem/edit_testcase?problemID=" + res;
        else
            return "redirect:/management/problem";
    }

    @GetMapping("/edit_testcase")
    public String showEditTestCasePage(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "problemID") Long problemID, Model model) throws UnAuthorizedException, ProblemNotFoundException
    {
        if(!problemService.isProblemExist(problemID))
            throw new ProblemNotFoundException();

        ProblemEntity problemEntity = problemService.getProblemIfUserHasPermission(userEntity, problemID);
        if(problemEntity == null)
            throw new UnAuthorizedException();

        model.addAttribute("title", problemEntity.getTitle());
        model.addAttribute("problemID", problemEntity.getProblemID());
        model.addAttribute("cases", problemService.getTestCasesByProblemEntity(problemEntity));
        model.addAttribute("checker", problemService.getCheckerByProblemIfExists(problemEntity));
        model.addAttribute("languages", languageService.getAllLanguages());
        return "template_edit_testcase";
    }

    @PostMapping("/delete_testcase")
    public String deleteTestCase(@AuthenticationPrincipal UserEntity userEntity, TestCaseDeleteForm deleteForm) throws TestCaseNotFoundException, UnAuthorizedException, ProblemNotFoundException
    {
        TestCaseEntity testCaseEntity = testCaseService.getTestCaseEntityByCaseID(deleteForm.getCaseID());
        if(testCaseEntity == null)
            throw new TestCaseNotFoundException();

        ProblemEntity problemEntity = testCaseEntity.getProblemEntity();
        if(!problemService.hasPermissionOnProblem(userEntity, problemEntity.getProblemID()))
            throw new UnAuthorizedException();

        problemService.deleteTestCase(problemEntity, testCaseEntity);
        return "redirect:/management/problem/edit_testcase?problemID=" + problemEntity.getProblemID();
    }

    @PostMapping("/create_testcase")
    public String createTestCase(@AuthenticationPrincipal UserEntity userEntity, TestCaseCreateForm form) throws ProblemNotFoundException, UnAuthorizedException, TestCaseCreateException
    {
        ProblemEntity problemEntity = problemService.getProblemByID(form.getProblemID());
        if(problemEntity == null)
            throw new ProblemNotFoundException();

        if(!problemService.hasPermissionOnProblem(userEntity, problemEntity.getProblemID()))
            throw new UnAuthorizedException();

        problemService.createNewTestCaseToProblemEntity(problemEntity, form);
        return "redirect:/management/problem/edit_testcase?problemID=" + problemEntity.getProblemID();
    }

    @PostMapping("/upload_testcase")
    public String uploadTestCase(@AuthenticationPrincipal UserEntity userEntity, TestCaseUploadForm form) throws IOException, ProblemNotFoundException, UnAuthorizedException, TestCaseCreateException
    {
        ProblemEntity problemEntity = problemService.getProblemByID(form.getProblemID());
        if(problemEntity == null)
            throw new ProblemNotFoundException();

        if(!problemService.hasPermissionOnProblem(userEntity, problemEntity.getProblemID()))
            throw new UnAuthorizedException();

        log.info("upload()");
        problemService.uploadTestCaseToProblemEntity(problemEntity, form);
        return "redirect:/management/problem/edit_testcase?problemID=" + problemEntity.getProblemID();
    }

    @PostMapping("/delete_checker")
    public String deleteChecker(CheckerDeleteForm form) throws InternalDataException
    {
        problemService.deleteChecker(form.getCheckerID());
        return "redirect:/management/problem/edit_testcase?problemID=" + form.getProblemID();
    }

    @PostMapping("/upload_checker")
    public String uploadChecker(@AuthenticationPrincipal UserEntity userEntity, SubmitForm form) throws ProblemNotFoundException, UnAuthorizedException, InternalDataException
    {
        if(!problemService.isProblemExist(form.getProblemID()))
            throw new ProblemNotFoundException();

        if(!problemService.hasPermissionOnProblem(userEntity, form.getProblemID()))
            throw new UnAuthorizedException();

        problemService.editChecker(form, form.getProblemID(), userEntity);
        return "redirect:/management/problem/edit_testcase?problemID=" + form.getProblemID();
    }
}

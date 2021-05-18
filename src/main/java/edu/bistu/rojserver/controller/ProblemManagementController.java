package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.TestCaseCreateForm;
import edu.bistu.rojserver.domain.TestCaseDeleteForm;
import edu.bistu.rojserver.domain.TestCaseUploadForm;
import edu.bistu.rojserver.exceptions.ProblemNotFoundException;
import edu.bistu.rojserver.exceptions.TestCaseCreateException;
import edu.bistu.rojserver.exceptions.TestCaseNotFoundException;
import edu.bistu.rojserver.exceptions.UnAuthorizedException;
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
@RequestMapping("/problem_management")
@Slf4j
public class ProblemManagementController
{
    @Resource
    private ProblemService problemService;

    @Resource
    private TestCaseService testCaseService;

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
        Long res = problemService.editProblem(problemEntity, userEntity);
        if(res != null)
            return "redirect:/problem_management/edit_testcase?problemID=" + res;
        else
            return "redirect:/problem_management";
    }

    @GetMapping("/edit_testcase")
    public String showEditTestCasePage(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(name = "problemID") Long problemID, Model model) throws UnAuthorizedException
    {
        if(!hasPermissionOnProblem(userEntity, problemID))
            throw new UnAuthorizedException();

        ProblemEntity problemEntity = problemService.getProblemByID(problemID);
        model.addAttribute("title", problemEntity.getTitle());
        model.addAttribute("problemID", problemEntity.getProblemID());
        model.addAttribute("cases", problemService.getTestCasesByProblemEntity(problemEntity));
        return "template_edit_testcase";
    }

    @PostMapping("/delete_testcase")
    public String deleteTestCase(@AuthenticationPrincipal UserEntity userEntity, TestCaseDeleteForm deleteForm) throws TestCaseNotFoundException, UnAuthorizedException
    {
        TestCaseEntity testCaseEntity = testCaseService.getTestCaseEntityByCaseID(deleteForm.getCaseID());
        if(testCaseEntity == null)
            throw new TestCaseNotFoundException();

        ProblemEntity problemEntity = testCaseEntity.getProblemEntity();
        if(!hasPermissionOnProblem(userEntity, problemEntity.getProblemID()))
            throw new UnAuthorizedException();

        problemService.deleteTestCase(problemEntity, testCaseEntity);
        return "redirect:/problem_management/edit_testcase?problemID=" + problemEntity.getProblemID();
    }

    @PostMapping("/create_testcase")
    public String createTestCase(@AuthenticationPrincipal UserEntity userEntity, TestCaseCreateForm form) throws ProblemNotFoundException, UnAuthorizedException, TestCaseCreateException
    {
        ProblemEntity problemEntity = problemService.getProblemByID(form.getProblemID());
        if(problemEntity == null)
            throw new ProblemNotFoundException();

        if(!hasPermissionOnProblem(userEntity, problemEntity.getProblemID()))
            throw new UnAuthorizedException();

        problemService.createNewTestCaseToProblemEntity(problemEntity, form);
        return "redirect:/problem_management/edit_testcase?problemID=" + problemEntity.getProblemID();
    }

    @PostMapping("/upload_testcase")
    public String uploadTestCase(@AuthenticationPrincipal UserEntity userEntity, TestCaseUploadForm form) throws IOException, ProblemNotFoundException, UnAuthorizedException, TestCaseCreateException
    {
        ProblemEntity problemEntity = problemService.getProblemByID(form.getProblemID());
        if(problemEntity == null)
            throw new ProblemNotFoundException();

        if(!hasPermissionOnProblem(userEntity, problemEntity.getProblemID()))
            throw new UnAuthorizedException();

        log.info("upload()");
        problemService.uploadTestCaseToProblemEntity(problemEntity, form);
        return "redirect:/problem_management/edit_testcase?problemID=" + problemEntity.getProblemID();
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

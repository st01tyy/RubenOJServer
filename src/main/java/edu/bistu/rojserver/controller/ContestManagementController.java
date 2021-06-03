package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.domain.Contest;
import edu.bistu.rojserver.domain.ProblemSelectForm;
import edu.bistu.rojserver.service.ContestService;
import edu.bistu.rojserver.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/management/contest")
public class ContestManagementController
{
    @Resource
    private ContestService contestService;

    @Resource
    private ProblemService problemService;

    @GetMapping
    public String showManagementPage(Model model)
    {
        model.addAttribute("contestList", contestService.getContestList());
        return "template_contest_management";
    }

    @GetMapping("/edit")
    public String showContestEditPage(@RequestParam(name = "id", required = false) Long contestID, Model model)
    {
        if(contestID == null)
            model.addAttribute("contest", new Contest());
        else
            model.addAttribute("contest", contestService.getContest(contestID));
        if(contestID != null)
        {
            model.addAttribute("availableProblems", problemService.getAvailableProblems());
            model.addAttribute("selectedProblems", problemService.getSelectedProblems(contestID));
        }
        return "template_edit_contest";
    }

    @PostMapping("/edit_information")
    public String editContestInformation(Contest contest, @AuthenticationPrincipal UserEntity userEntity) throws Exception
    {
        Long contestID = contestService.editContestInformation(contest, userEntity);
        if(contestID == null)
            return "redirect:/management/contest/edit";
        return "redirect:/management/contest/edit?id=" + contestID;
    }

    @PostMapping("/select_problems")
    public String selectProblemsToContest(ProblemSelectForm form)
    {
        contestService.addProblemsToContest(form);
        return "redirect:/management/contest/edit?id=" + form.getContestID();
    }

    @GetMapping("/delete_problem")
    public String showDeleteProblemPage(@RequestParam(name = "problemID") Long problemID, @RequestParam(name = "contestID") Long contestID, Model model)
    {
        model.addAttribute("problemID", problemID);
        model.addAttribute("contestID", contestID);
        return "problem_detach";
    }

    @PostMapping("/delete_problem")
    public String deleteProblemFromContest(ProblemSelectForm form)
    {
        contestService.deleteProblemFromContest(form);
        return "redirect:/management/contest/edit?id=" + form.getContestID();
    }

}

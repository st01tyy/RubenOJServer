package edu.bistu.rojserver.controller;

import edu.bistu.rojserver.service.ContestService;
import edu.bistu.rojserver.service.ProblemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/contest")
public class ContestController
{
    @Resource
    private ContestService contestService;

    @Resource
    private ProblemService problemService;

    @GetMapping("/list")
    public String showContestListPage(Model model)
    {
        model.addAttribute("contestList", contestService.getContestList());
        return "template_contest_list";
    }

    @GetMapping
    public String showContestPage(@RequestParam(name = "id") Long contestID, Model model)
    {
        model.addAttribute("contest", contestService.getContest(contestID));
        model.addAttribute("problems", problemService.getInContestProblems(contestID));
        return "template_contest";
    }

    @GetMapping("/rank")
    public String showContestRankPage(@RequestParam(name = "contestID") Long contestID, Model model)
    {
        model.addAttribute("contest", contestService.getContest(contestID));
        model.addAttribute("userList", contestService.getContestRankTable(contestID));
        return "template_ranking";
    }
}

package edu.bistu.rojserver.restcontroller;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.domain.jsonmodel.Limit;
import edu.bistu.rojserver.service.ProblemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/problem_limit")
public class ProblemLimitController
{
    @Resource
    private ProblemService problemService;

    @GetMapping
    public Limit getProblemLimit(@RequestParam Long problemID)
    {
        ProblemEntity problemEntity = problemService.getProblemByID(problemID);
        if(problemEntity == null)
            return null;
        return new Limit(problemEntity.getTimeLimit(), problemEntity.getMemoryLimit());
    }
}

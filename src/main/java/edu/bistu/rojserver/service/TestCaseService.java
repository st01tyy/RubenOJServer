package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.repository.ProblemRepository;
import edu.bistu.rojserver.dao.repository.TestCaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shared.TestCase;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TestCaseService
{
    @Resource
    private ProblemRepository problemRepository;

    @Resource
    private TestCaseRepository testCaseRepository;

    public TestCase[] getTestCasesByProblemID(Long problemID)
    {
        Optional<ProblemEntity> optional = problemRepository.findById(problemID);
        if(optional.isEmpty())
            return null;
        ProblemEntity problemEntity = optional.get();
        List<TestCaseEntity> caseList = problemEntity.getCaseList();
        if(caseList == null || caseList.size() == 0)
            return null;
        TestCase[] cases = new TestCase[caseList.size()];
        for(int i = 0; i < cases.length; i++)
        {
            cases[i] = new TestCase(caseList.get(i).getInput(), caseList.get(i).getOutput());
            log.info("cases[" + i + "] input length" + cases[i].getInput().length);
        }
        return cases;
    }
}

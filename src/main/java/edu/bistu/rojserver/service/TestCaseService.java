package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.DownloadFile;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.TestCaseFileEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.ProblemRepository;
import edu.bistu.rojserver.dao.repository.TestCaseFileRepository;
import edu.bistu.rojserver.dao.repository.TestCaseRepository;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.exceptions.InternalDataException;
import edu.bistu.rojserver.exceptions.ProblemNotFoundException;
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

    @Resource
    private TestCaseFileRepository testCaseFileRepository;

    @Resource
    private UserRepository userRepository;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isTestCaseExist(Long caseID)
    {
        return testCaseRepository.existsById(caseID);
    }

    public TestCase[] getTestCasesByProblemID(Long problemID)
    {
        Optional<ProblemEntity> optional = problemRepository.findById(problemID);
        if(optional.isEmpty())
            return null;
        ProblemEntity problemEntity = optional.get();
        List<TestCaseFileEntity> caseList = testCaseFileRepository.findAllByProblemEntity(problemEntity);
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

    public TestCaseEntity getTestCaseEntityByCaseID(Long caseID)
    {
        Optional<TestCaseEntity> optional = testCaseRepository.findById(caseID);
        if(optional.isEmpty())
            return null;
        return optional.get();
    }

    public DownloadFile downloadInput(Long caseID)
    {
        return testCaseFileRepository.downloadTestCaseInputFileByCaseID(caseID);
    }

    public DownloadFile downloadOutput(Long caseID)
    {
        return testCaseFileRepository.downloadTestCaseOutputFileByCaseID(caseID);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasPermissionOnTestCase(UserEntity userEntity, Long caseID) throws InternalDataException
    {
        if(userEntity.getRole().compareTo(UserEntity.Role.CONTEST_ORGANIZER) >= 0)
            return true;
        if(userEntity.getRole() == UserEntity.Role.USER)
            return false;
        try
        {
            Long problemID = testCaseRepository.getTestCaseProblemID(caseID);
            if(problemID == null)
                throw new InternalDataException();
            Long userID = problemRepository.getAuthorID(problemID);
            if(userID == null)
                throw new InternalDataException();
            UserEntity temp = userRepository.findById(userID).orElse(null);
            if(temp == null)
                throw new InternalDataException();
            return temp.getUserID().equals(userEntity.getUserID());
        }
        catch (Exception e)
        {
            throw new InternalDataException();
        }
    }
}

package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.ProblemStatus;
import edu.bistu.rojserver.dao.ProblemTableItem;
import edu.bistu.rojserver.dao.entity.*;
import edu.bistu.rojserver.dao.repository.*;
import edu.bistu.rojserver.domain.TestCase;
import edu.bistu.rojserver.domain.TestCaseCreateForm;
import edu.bistu.rojserver.domain.TestCaseUploadForm;
import edu.bistu.rojserver.exceptions.SubmissionCreateException;
import edu.bistu.rojserver.exceptions.TestCaseCreateException;
import edu.bistu.rojserver.property.WebServerProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shared.SubmissionResult;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProblemService
{
    @Resource
    private ProblemRepository problemRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private TestCaseRepository testCaseRepository;

    @Resource
    private SubmissionRepository submissionRepository;

    @Resource
    private WebServerProperty webServerProperty;

    @Resource
    private TestCaseFileRepository testCaseFileRepository;

    public List<ProblemEntity> getProblemsByAuthor(UserEntity author)
    {
        return problemRepository.findAllByAuthor(author);
    }

    public int getProblemTablePageCount()
    {
        int itemCount = problemRepository.countByProblemStatus(ProblemStatus.PUBLIC);
        if(itemCount == 0)
            return 0;
        int pageCount = itemCount / webServerProperty.getPage_size();
        if(itemCount % webServerProperty.getPage_size() != 0)
            pageCount ++;
        return pageCount;
    }

    public List<Integer> getProblemTablePages(int currentPage, int pageCount)
    {
        log.info("getProblemTablePages(" + currentPage + ", " + pageCount + ")");
        if(pageCount == 0)
            return null;
        List<Integer> pages;
        if(pageCount <= webServerProperty.getPage_count_limit())
        {
            pages = new ArrayList<>(pageCount);
            for(int i = 1; i <= pageCount; i++)
            {
                pages.add(i);
            }
        }
        else
        {
            pages = new ArrayList<>(webServerProperty.getPage_count_limit());
            int limit = webServerProperty.getPage_count_limit() - 1;
            int expectLeft = limit / 2;
            int expectRight = limit - expectLeft;
            if(currentPage - 1 < expectLeft)
            {
                for(int i = 1; i <= limit + 1; i++)
                {
                    pages.add(i);
                }
            }
            else if(pageCount - currentPage < expectRight)
            {
                for(int i = pageCount - limit - 1; i <= pageCount; i++)
                {
                    pages.add(i);
                }
            }
            else
            {
                for(int i = currentPage - expectLeft; i <= currentPage + expectRight; i++)
                {
                    pages.add(i);
                }
            }
        }
        return pages;
    }

    public List<ProblemTableItem> getProblemTableItems(int page)
    {
        log.info("getProblemTableItems(" + page + ")");
        return problemRepository.findProblemTableItems((page - 1) * webServerProperty.getPage_size(), webServerProperty.getPage_size());
    }

    public ProblemEntity getProblemByID(Long problemID)
    {
        Optional<ProblemEntity> optional = problemRepository.findByProblemID(problemID);
        return optional.orElse(null);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Transactional(rollbackFor = Exception.class)
    public Long editProblem(ProblemEntity problemEntity, UserEntity editor)
    {
        Optional<ProblemEntity> optional = problemRepository.findById((problemEntity.getProblemID() == null) ? 0 : problemEntity.getProblemID());
        if(optional.isPresent())
        {
            ProblemEntity old = optional.get();
            old.setTitle(problemEntity.getTitle());
            old.setContent(problemEntity.getContent());
            old.setTimeLimit(problemEntity.getTimeLimit());
            old.setMemoryLimit(problemEntity.getMemoryLimit());
            old.setDifficulty(problemEntity.getDifficulty());
            problemEntity = old;
        }

        if(problemEntity.getAuthor() == null)
        {
            problemEntity.setAuthor(editor);
            problemEntity = problemRepository.saveAndFlush(problemEntity);
            return problemEntity.getProblemID();
        }
        else
        {
            problemRepository.saveAndFlush(problemEntity);
            return null;
        }
    }

    public List<TestCase> getTestCasesByProblemEntity(ProblemEntity problemEntity)
    {
        List<TestCaseEntity> list = testCaseRepository.findAllByProblemEntity(problemEntity);
        List<TestCase> cases = new ArrayList<>(list.size());
        for(TestCaseEntity entity : list)
        {
            cases.add(TestCase.fromEntity(entity));
        }
        return cases;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTestCase(ProblemEntity problemEntity, TestCaseEntity testCaseEntity)
    {
        TestCaseFileEntity testCaseFileEntity = new TestCaseFileEntity();
        testCaseFileEntity.setCaseID(testCaseEntity.getCaseID());
        testCaseRepository.delete(testCaseEntity);
        testCaseFileRepository.delete(testCaseFileEntity);
        changeProblemStatusOnDeletingTestCase(problemEntity);
        problemRepository.save(problemEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createNewTestCaseToProblemEntity(ProblemEntity problemEntity, TestCaseCreateForm form) throws TestCaseCreateException
    {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setInputFileName("input.txt");
        testCaseEntity.setInputContent(form.getNew_input().substring(0, Math.min(200, form.getNew_input().length())));
        testCaseEntity.setOutputFileName("output.txt");
        testCaseEntity.setOutputContent(form.getNew_output().substring(0, Math.min(200, form.getNew_output().length())));
        testCaseEntity.setProblemEntity(problemEntity);
        try
        {
            testCaseEntity = testCaseRepository.saveAndFlush(testCaseEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new TestCaseCreateException();
        }

        if(testCaseEntity.getCaseID() == null)
            throw new TestCaseCreateException();

        TestCaseFileEntity testCaseFileEntity = new TestCaseFileEntity();
        testCaseFileEntity.setCaseID(testCaseEntity.getCaseID());
        testCaseFileEntity.setInput(form.getNew_input().getBytes(StandardCharsets.UTF_8));
        testCaseFileEntity.setOutput(form.getNew_output().getBytes(StandardCharsets.UTF_8));
        testCaseFileEntity.setInputFileName(testCaseEntity.getInputFileName());
        testCaseFileEntity.setOutputFileName(testCaseEntity.getOutputFileName());
        testCaseFileEntity.setProblemEntity(problemEntity);
        try
        {
            testCaseFileRepository.saveAndFlush(testCaseFileEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new TestCaseCreateException();
        }

        changeProblemStatusOnAddingNewTestCase(problemEntity);
        problemRepository.save(problemEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadTestCaseToProblemEntity(ProblemEntity problemEntity, TestCaseUploadForm form) throws IOException, TestCaseCreateException
    {
        byte[] input = form.getInputFile().getBytes();
        byte[] output = form.getOutputFile().getBytes();
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setInputFileName(form.getInputFile().getOriginalFilename());
        testCaseEntity.setInputContent(fromByteArray(input));
        testCaseEntity.setOutputFileName(form.getOutputFile().getOriginalFilename());
        testCaseEntity.setOutputContent(fromByteArray(output));
        testCaseEntity.setProblemEntity(problemEntity);
        try
        {
            testCaseEntity = testCaseRepository.saveAndFlush(testCaseEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new TestCaseCreateException();
        }

        TestCaseFileEntity testCaseFileEntity = new TestCaseFileEntity();
        testCaseFileEntity.setCaseID(testCaseEntity.getCaseID());
        testCaseFileEntity.setInput(input);
        testCaseFileEntity.setOutput(output);
        testCaseFileEntity.setInputFileName(testCaseEntity.getInputFileName());
        testCaseFileEntity.setOutputFileName(testCaseEntity.getOutputFileName());
        testCaseFileEntity.setProblemEntity(problemEntity);
        try
        {
            testCaseFileRepository.saveAndFlush(testCaseFileEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new TestCaseCreateException();
        }

        changeProblemStatusOnAddingNewTestCase(problemEntity);
        problemRepository.save(problemEntity);
    }

    private String fromByteArray(byte[] arr)
    {
        StringBuilder sb = new StringBuilder(new String(arr, 0, Math.min(arr.length, 200), StandardCharsets.UTF_8));    //默认不含中文等多字节文字
        if(sb.length() == 200)
            sb.append("...");
        return sb.toString();
    }

    private void changeProblemStatusOnDeletingTestCase(ProblemEntity problemEntity)
    {
        if(problemEntity.getProblemStatus() == ProblemStatus.UNREADY || problemEntity.getProblemStatus() == ProblemStatus.READY)
        {
            if(testCaseRepository.countByProblemEntity(problemEntity) == 0)
                problemEntity.setProblemStatus(ProblemStatus.EDITING);
            else
                problemEntity.setProblemStatus(ProblemStatus.UNREADY);
        }
    }

    private void changeProblemStatusOnAddingNewTestCase(ProblemEntity problemEntity)
    {
        if(problemEntity.getProblemStatus() == ProblemStatus.EDITING)
            problemEntity.setProblemStatus(ProblemStatus.UNREADY);
        else if(problemEntity.getProblemStatus() == ProblemStatus.READY)
        {
            expireAllSubmissionsByProblemEntity(problemEntity);
            problemEntity.setProblemStatus(ProblemStatus.UNREADY);
        }
    }

    private void expireAllSubmissionsByProblemEntity(ProblemEntity problemEntity)
    {
        //将之前的提交结果改为过期
        List<SubmissionEntity> submissionEntityList = submissionRepository.findAllByProblemEntity(problemEntity);
        for(SubmissionEntity submissionEntity : submissionEntityList)
        {
            submissionEntity.setResult(SubmissionResult.Expired);
            submissionRepository.save(submissionEntity);
        }
    }

}

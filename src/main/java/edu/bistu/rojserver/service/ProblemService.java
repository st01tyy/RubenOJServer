package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.ProblemTableItem;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.ProblemRepository;
import edu.bistu.rojserver.dao.repository.SubmissionRepository;
import edu.bistu.rojserver.dao.repository.TestCaseRepository;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.domain.TestCase;
import edu.bistu.rojserver.domain.TestCaseCreateForm;
import edu.bistu.rojserver.domain.TestCaseUploadForm;
import edu.bistu.rojserver.property.WebServerProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public List<ProblemEntity> getProblemsByAuthor(UserEntity author)
    {
        return problemRepository.getAllByAuthor(author);
    }

    public int getProblemTablePageCount()
    {
        int itemCount = problemRepository.countByStatus(ProblemEntity.Status.PUBLIC);
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
            UserEntity userEntity = userRepository.findById(editor.getUserID()).get();
            List<ProblemEntity> list = userEntity.getProblemList();
            if(list == null)
                list = new ArrayList<>(1);
            list.add(problemEntity);
            editor.setProblemList(list);
            problemEntity = problemRepository.saveAndFlush(problemEntity);
            userRepository.saveAndFlush(userEntity);
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
        List<TestCaseEntity> list = problemEntity.getCaseList();
        List<TestCase> cases = new ArrayList<>(list.size());
        for(TestCaseEntity entity : list)
        {
            cases.add(TestCase.fromEntity(entity));
        }
        return cases;
    }

    public void deleteTestCase(ProblemEntity problemEntity, TestCaseEntity testCaseEntity)
    {
        problemEntity.getCaseList().removeIf(e -> e.getCaseID().equals(testCaseEntity.getCaseID()));
        testCaseRepository.delete(testCaseEntity);
        problemEntity = problemRepository.saveAndFlush(problemEntity);

        changeProblemStatusOnDeletingTestCase(problemEntity);
        problemRepository.saveAndFlush(problemEntity);
    }

    public void createNewTestCaseToProblemEntity(ProblemEntity problemEntity, TestCaseCreateForm form)
    {
        TestCaseEntity testCaseEntity = new TestCaseEntity();
        testCaseEntity.setInput(form.getNew_input().getBytes(StandardCharsets.UTF_8));
        testCaseEntity.setInputFileName("input.txt");
        testCaseEntity.setOutput(form.getNew_output().getBytes(StandardCharsets.UTF_8));
        testCaseEntity.setOutputFileName("output.txt");
        testCaseEntity.setProblemEntity(problemEntity);
        testCaseEntity = testCaseRepository.saveAndFlush(testCaseEntity);

        changeProblemStatusOnAddingNewTestCase(problemEntity);

        problemEntity.getCaseList().add(testCaseEntity);
        problemRepository.saveAndFlush(problemEntity);
    }

    public void uploadTestCaseToProblemEntity(ProblemEntity problemEntity, TestCaseUploadForm form) throws IOException
    {
        TestCaseEntity testCaseEntity = form.convertToEntity();
        testCaseEntity.setProblemEntity(problemEntity);
        testCaseEntity = testCaseRepository.saveAndFlush(testCaseEntity);

        changeProblemStatusOnAddingNewTestCase(problemEntity);

        problemEntity.getCaseList().add(testCaseEntity);
        problemRepository.save(problemEntity);
    }

    private void changeProblemStatusOnDeletingTestCase(ProblemEntity problemEntity)
    {
        if(problemEntity.getStatus() == ProblemEntity.Status.UNREADY || problemEntity.getStatus() == ProblemEntity.Status.READY)
        {
            if(problemEntity.getCaseList() == null || problemEntity.getCaseList().isEmpty())
                problemEntity.setStatus(ProblemEntity.Status.EDITING);
            else
                problemEntity.setStatus(ProblemEntity.Status.UNREADY);
        }
    }

    private void changeProblemStatusOnAddingNewTestCase(ProblemEntity problemEntity)
    {
        if(problemEntity.getStatus() == ProblemEntity.Status.EDITING)
            problemEntity.setStatus(ProblemEntity.Status.UNREADY);
        else if(problemEntity.getStatus() == ProblemEntity.Status.READY)
        {
            expireAllSubmissionsByProblemEntity(problemEntity);
            problemEntity.setStatus(ProblemEntity.Status.UNREADY);
        }
    }

    private void expireAllSubmissionsByProblemEntity(ProblemEntity problemEntity)
    {
        //将之前的提交结果改为过期
        List<SubmissionEntity> submissionEntityList = submissionRepository.findAllByProblemEntity(problemEntity);
        for(SubmissionEntity submissionEntity : submissionEntityList)
        {
            submissionEntity.setResult("Expired");
            submissionRepository.save(submissionEntity);
        }
    }

}

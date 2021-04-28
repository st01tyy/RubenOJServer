package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.ProblemRepository;
import edu.bistu.rojserver.dao.repository.TestCaseRepository;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.domain.TestCaseUploadForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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

    public List<ProblemEntity> getProblemsByAuthor(UserEntity author)
    {
        return problemRepository.getAllByAuthor(author);
    }

    public ProblemEntity getProblemByID(Long problemID)
    {
        Optional<ProblemEntity> optional = problemRepository.findByProblemID(problemID);
        return optional.orElse(null);
    }

    public void editProblem(ProblemEntity problemEntity, UserEntity editor)
    {
        Optional<ProblemEntity> optional = problemRepository.findById(problemEntity.getProblemID());
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
            problemRepository.saveAndFlush(problemEntity);
            userRepository.saveAndFlush(userEntity);
        }
        else
            problemRepository.saveAndFlush(problemEntity);
    }

    public void uploadTestCase(TestCaseUploadForm form) throws IOException
    {
        Optional<ProblemEntity> optional = problemRepository.findById(form.getProblemID());
        if(optional.isEmpty())
        {
            log.info("problem not found");
            return;
        }
        ProblemEntity problemEntity = optional.get();
        TestCaseEntity testCaseEntity = form.convertToEntity();
        testCaseEntity.setProblemEntity(problemEntity);
        problemEntity.getCaseList().add(testCaseEntity);
        testCaseRepository.save(testCaseEntity);
        problemRepository.save(problemEntity);
    }

}

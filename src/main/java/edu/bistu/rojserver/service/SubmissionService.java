package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.ProblemStatus;
import edu.bistu.rojserver.dao.entity.*;
import edu.bistu.rojserver.dao.repository.*;
import edu.bistu.rojserver.domain.SubmissionFetchForm;
import edu.bistu.rojserver.domain.SubmissionFetchResult;
import edu.bistu.rojserver.domain.SubmitForm;
import edu.bistu.rojserver.domain.TestSubmitForm;
import edu.bistu.rojserver.domain.jsonmodel.JudgeResult;
import edu.bistu.rojserver.exceptions.SubmissionCreateException;
import edu.bistu.rojserver.property.WebServerProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shared.Submission;
import shared.SubmissionResult;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubmissionService
{
    @Resource
    private ProblemRepository problemRepository;

    @Resource
    private SubmissionRepository submissionRepository;

    @Resource
    private SubmissionFileRepository submissionFileRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private LanguageRepository languageRepository;

    @Resource
    private WebServerProperty webServerProperty;

    @Resource
    private KafkaTemplate<String, Submission> kafkaTemplate;

    public void updateSubmissionResult(JudgeResult judgeResult)
    {
        if(judgeResult == null)
            return;
        Optional<SubmissionEntity> optional = submissionRepository.findById(judgeResult.getSubmissionID());
        if(optional.isPresent())
        {
            SubmissionEntity submissionEntity = optional.get();
            submissionEntity.setResult(judgeResult.getResult());
            submissionEntity.setExecutionTime(judgeResult.getExecutionTime());
            submissionEntity.setMemoryUsage(judgeResult.getMemoryUsage());

            if(submissionEntity.getResult() == SubmissionResult.Running)
                submissionEntity.setWaitTime(System.currentTimeMillis());

            else
                submissionEntity.setJudgeTime(System.currentTimeMillis());

            submissionEntity = submissionRepository.save(submissionEntity);

            if(submissionEntity.getResult() == SubmissionResult.Accepted && submissionEntity.getProblemStatus() == ProblemStatus.UNREADY)
            {
                submissionEntity.getProblemEntity().setProblemStatus(ProblemStatus.READY);
                problemRepository.save(submissionEntity.getProblemEntity());
            }
        }
    }

    public void loadTest(TestSubmitForm submitForm)
    {
        UserEntity userEntity = userRepository.findUserEntityByUsername(submitForm.getUsername()).orElse(null);
        try
        {
            createSubmission(userEntity, submitForm);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createSubmission(UserEntity author, SubmitForm submitForm) throws SubmissionCreateException, IOException
    {
        Optional<ProblemEntity> optional = problemRepository.findByProblemID(submitForm.getProblemID());
        if(optional.isEmpty())
            return null;
        ProblemEntity problemEntity = optional.get();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(author.getUserID());
        if(optionalUserEntity.isEmpty())
            return null;
        author = optionalUserEntity.get();
        Optional<LanguageEntity> optionalLanguageEntity = languageRepository.findLanguageEntityByName(submitForm.getLanguage());
        if(optionalLanguageEntity.isEmpty())
            return null;

        LanguageEntity languageEntity = optionalLanguageEntity.get();

        SubmissionEntity submissionEntity = new SubmissionEntity();
        submissionEntity.setProblemEntity(problemEntity);
        submissionEntity.setAuthor(author);
        submissionEntity.setLanguageEntity(languageEntity);
        submissionEntity.setProblemStatus(problemEntity.getProblemStatus());
        submissionEntity.setSubmitTime(System.currentTimeMillis());
        submissionEntity.setSourceFileName(submitForm.getSourceFile().getOriginalFilename());
        try
        {
            submissionEntity = submissionRepository.saveAndFlush(submissionEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SubmissionCreateException();
        }

        if(submissionEntity.getSubmissionID() == null)
            throw new SubmissionCreateException();

        byte[] source = submitForm.getSourceFile().getBytes();
        SubmissionFileEntity submissionFileEntity = new SubmissionFileEntity();
        submissionFileEntity.setSubmissionID(submissionEntity.getSubmissionID());
        submissionFileEntity.setSource(source);
        submissionFileEntity.setSourceFileName(submissionEntity.getSourceFileName());
        try
        {
            submissionFileRepository.saveAndFlush(submissionFileEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SubmissionCreateException();
        }


        String topicName = "LANGUAGE-" + languageEntity.getLanguageID();
        Submission submission = new Submission();
        submission.setSubmissionID(submissionEntity.getSubmissionID());
        submission.setSubmitTime(submissionEntity.getSubmitTime());
        submission.setLanguageName(submitForm.getLanguage());
        submission.setArr(submitForm.getSourceFile().getBytes());
        submission.setSourceFileName(submitForm.getSourceFile().getOriginalFilename());
        submission.setSourceName(getFileNameWithoutPostFix(Objects.requireNonNull(submitForm.getSourceFile().getOriginalFilename())));
        submission.setProblemID(submitForm.getProblemID());

        kafkaTemplate.send(topicName, submission);
        return submissionEntity.getSubmissionID();
    }

    public List<SubmissionEntity> getSubmissionsByAuthor(UserEntity userEntity)
    {
        return submissionRepository.findAllByAuthor(userEntity);
    }

    public Slice<SubmissionEntity> getSubmissionList(Integer page, UserEntity userEntity)
    {
        if(page == null)
            page = 0;
        Pageable pageable = PageRequest.of(page, webServerProperty.getPage_size());
        return submissionRepository.findAllByAuthorOrderBySubmissionIDDesc(userEntity, pageable);
    }

    private String getFileNameWithoutPostFix(String name)
    {
        int i;
        for(i = name.length() - 1; i > 0; i--)
        {
            if(name.charAt(i) == '.')
                break;
        }
        if(i == 0)
            return name;
        return name.substring(0, i);
    }

    public SubmissionFetchResult[] fetchResults(SubmissionFetchForm[] arr)
    {
        SubmissionFetchResult[] result = new SubmissionFetchResult[arr.length];
        for(int i = 0; i < arr.length; i++)
        {
            SubmissionEntity submissionEntity = submissionRepository.findById(arr[i].getSubmissionID()).orElse(null);
            result[i] = new SubmissionFetchResult();
            result[i].setSubmissionID(arr[i].getSubmissionID());
            result[i].setResult((submissionEntity == null) ? "Submission Not Found" : submissionEntity.getResult().getResultName());
            result[i].setTime((submissionEntity == null || submissionEntity.getExecutionTime() == null) ? "--" : submissionEntity.getExecutionTime() + " ms");
            result[i].setMemory((submissionEntity == null || submissionEntity.getMemoryUsage() == null) ? "--" : submissionEntity.getMemoryUsage() + " KB");
        }
        return result;
    }
}

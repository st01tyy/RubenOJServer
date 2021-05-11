package edu.bistu.rojserver.service;

import edu.bistu.rojserver.dao.entity.LanguageEntity;
import edu.bistu.rojserver.dao.entity.ProblemEntity;
import edu.bistu.rojserver.dao.entity.SubmissionEntity;
import edu.bistu.rojserver.dao.entity.UserEntity;
import edu.bistu.rojserver.dao.repository.LanguageRepository;
import edu.bistu.rojserver.dao.repository.ProblemRepository;
import edu.bistu.rojserver.dao.repository.SubmissionRepository;
import edu.bistu.rojserver.dao.repository.UserRepository;
import edu.bistu.rojserver.domain.SubmitForm;
import edu.bistu.rojserver.domain.jsonmodel.JudgeResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import shared.Submission;

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
    private UserRepository userRepository;

    @Resource
    private LanguageRepository languageRepository;

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
            submissionEntity = submissionRepository.save(submissionEntity);

            if(submissionEntity.getResult().equals("Accepted") && submissionEntity.getProblemEntity().getStatus() == ProblemEntity.Status.UNREADY)
            {
                submissionEntity.getProblemEntity().setStatus(ProblemEntity.Status.READY);
                problemRepository.save(submissionEntity.getProblemEntity());
            }
        }
    }

    public Long createSubmission(UserEntity author, SubmitForm submitForm) throws IOException
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
        submissionEntity.setResult("Waiting");
        if(problemEntity.getStatus().compareTo(ProblemEntity.Status.IN_CONTEST) < 0)
            submissionEntity.setTestSubmission(true);

        Long time = System.currentTimeMillis();
        submissionEntity.setSubmitDate(time);
        String str = getFileNameWithoutPostFix(Objects.requireNonNull(submitForm.getSourceFile().getOriginalFilename()));
        submissionEntity.setSourceFileName(str);
        submissionEntity.setSource(submitForm.getSourceFile().getBytes());

        submissionEntity = submissionRepository.saveAndFlush(submissionEntity);

        if(submissionEntity.getSubmissionID() == null)
            return null;

        author.getSubmissionList().add(submissionEntity);
        userRepository.save(author);

        String topicName = "LANGUAGE-" + languageEntity.getLanguageID();
        Submission submission = new Submission();
        submission.setSubmissionID(submissionEntity.getSubmissionID());
        submission.setSubmitTime(time);
        submission.setLanguageName(submitForm.getLanguage());
        submission.setArr(submitForm.getSourceFile().getBytes());
        submission.setSourceFileName(submitForm.getSourceFile().getOriginalFilename());
        submission.setSourceName(getFileNameWithoutPostFix(str));
        submission.setProblemID(submitForm.getProblemID());

        kafkaTemplate.send(topicName, submission);
        return submissionEntity.getSubmissionID();
    }

    public List<SubmissionEntity> getSubmissionsByAuthor(UserEntity userEntity)
    {
        return submissionRepository.findAllByAuthor(userEntity);
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
}

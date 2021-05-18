package edu.bistu.rojserver.dao.entity;

import edu.bistu.rojserver.dao.ProblemStatus;
import lombok.Getter;
import lombok.Setter;
import shared.SubmissionResult;
import javax.persistence.*;

@Getter
@Setter
@Entity(name = "submission")
@Table(indexes = @Index(name = "index_problem_status_result", columnList = "problem_status, result"))
public class SubmissionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long submissionID;

    @Column(nullable = false, name = "source_file_name")
    private String sourceFileName;  //with postfix

    @Column(nullable = false, name = "submit_time")
    private Long submitTime;    //System.currentTimeMillis() after user submit

    @Column(nullable = false, name = "judge_time")
    private Long judgeTime = 0L; //System.currentTImeMillis() after judge complete

    @Column(nullable = false, name = "memory_usage")
    private Integer memoryUsage = 0;

    @Column(nullable = false, name = "execution_time")
    private Integer executionTime = 0;  //ms

    @Column(nullable = false)
    private SubmissionResult result = SubmissionResult.Waiting;

    @Column(nullable = false, name = "problem_status")
    private ProblemStatus problemStatus;    //problem status when user submit

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
    private ProblemEntity problemEntity;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    private LanguageEntity languageEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity author;
}
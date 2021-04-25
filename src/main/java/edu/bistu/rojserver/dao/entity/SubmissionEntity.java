package edu.bistu.rojserver.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "submissions")
@Data
@NoArgsConstructor
public class SubmissionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long submissionID;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'source'")
    private String sourceFileName;  //without postfix

    @Column(nullable = false, columnDefinition = "blob")
    private byte[] source;  //source file binary

    @Column(nullable = false)
    private Long submitDate;    //System.currentTimeMillis()

    private Integer memoryUsage;
    private Integer executionTime;  //ms

    @Column(columnDefinition = "varchar(32)")
    private String result;  //Accepted, Wrong Answer...

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean testSubmission; //验题

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean contestSubmission;

    @ManyToOne
    private LanguageEntity languageEntity;

    @ManyToOne
    private UserEntity author;
}

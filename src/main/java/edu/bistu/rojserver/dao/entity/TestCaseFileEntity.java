package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "test_case_file")
public class TestCaseFileEntity
{
    @Id
    @Column(name = "case_id")
    private Long caseID;

    @Column(nullable = false, name = "input_file_name")
    private String inputFileName;

    @Column(nullable = false, name = "output_file_name")
    private String outputFileName;

    @Column(nullable = false, columnDefinition = "mediumblob")
    private byte[] input;

    @Column(nullable = false, columnDefinition = "mediumblob")
    private byte[] output;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
    private ProblemEntity problemEntity;
}

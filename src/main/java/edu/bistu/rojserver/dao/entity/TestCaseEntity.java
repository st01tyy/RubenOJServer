package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity(name = "test_case")
public class TestCaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private Long caseID;

    @Column(nullable = false, name = "input_file_name")
    private String inputFileName;

    @Column(nullable = false, name = "input_content")
    private String inputContent;    //first 200 characters of the input file

    @Column(nullable = false, name = "output_file_name")
    private String outputFileName;

    @Column(nullable = false, name = "output_content")
    private String outputContent;   //first 200 characters of the output file

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", referencedColumnName = "problem_id")
    private ProblemEntity problemEntity;
}

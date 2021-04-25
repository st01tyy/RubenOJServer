package edu.bistu.rojserver.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "test_cases")
@Data
@NoArgsConstructor
public class TestCaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long caseID;

    @Column(nullable = false, columnDefinition = "varchar(32)")
    private String inputFileName;

    @Column(nullable = false, columnDefinition = "mediumblob")
    private byte[] input;

    @Column(nullable = false, columnDefinition = "varchar(32)")
    private String outputFileName;

    @Column(nullable = false, columnDefinition = "mediumblob")
    private byte[] output;

    @ManyToOne
    private ProblemEntity problemEntity;
}

package edu.bistu.rojserver.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "problems")
public class ProblemEntity
{
    enum Status
    {
        UNREADY, READY, IN_CONTEST, PUBLIC
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long problemID;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content; //output of rich text editor

    @Column(nullable = false)
    private Integer timeLimit;  //UNIT: second

    @Column(nullable = false)
    private Integer memoryLimit;    //UNIT: KB

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean limitLanguage = false;

    private Integer difficulty;

    @Column(nullable = false)
    private Status status = Status.UNREADY;

    @ManyToOne
    private UserEntity author;

    @OneToMany
    private List<TestCaseEntity> caseList;

    @ManyToMany
    private List<TagEntity> tagList;
}

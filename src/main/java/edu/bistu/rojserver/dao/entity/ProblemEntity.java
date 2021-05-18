package edu.bistu.rojserver.dao.entity;

import edu.bistu.rojserver.dao.ProblemStatus;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity(name = "problem")
@Table(indexes = @Index(name = "index_problem_status", columnList = "problem_status"))
public class ProblemEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long problemID;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content; //output of rich text editor

    @Column(nullable = false, name = "time_limit")
    private Integer timeLimit;  //UNIT: second

    @Column(nullable = false, name = "memory_limit")
    private Integer memoryLimit;    //UNIT: KB

    @Column(nullable = false, name = "limit_language")
    private Boolean limitLanguage = false;

    @Column(nullable = false)
    private Integer difficulty = 1200;

    @Column(nullable = false, name = "problem_status")
    private ProblemStatus problemStatus = ProblemStatus.EDITING;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity author;
}

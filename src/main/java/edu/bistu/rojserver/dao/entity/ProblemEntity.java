package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "problems")
public class ProblemEntity
{
    enum Difficulty
    {
        BASIC("基础"), EASY("简单"), MEDIUM("中等"), HARD("困难"), ULTRA("非常难");

        private final String name;

        Difficulty(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long problemID;

    @Column(length = 16)
    private String title;

    private String description;

    private Integer timeLimit;  //Time Unit: Second

    private Integer memoryLimit;    //Unit: MB

    private Boolean limitLanguage;  //default value is false

    private Difficulty difficulty;

    @OneToMany
    private List<LanguageEntity> supportedLanguages;    //if limitLanguage is true, this value works

    @ManyToMany
    private List<ProblemTagEntity> tags;
}

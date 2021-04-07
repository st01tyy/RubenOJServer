package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "problem_tags")
public class ProblemTagEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tagID;

    private String name;

    @ManyToMany
    private List<ProblemEntity> problems;
}

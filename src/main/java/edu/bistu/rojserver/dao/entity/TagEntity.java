package edu.bistu.rojserver.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "tags")
public class TagEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tagID;

    @Column(length = 16)
    private String tagName;

    @ManyToMany
    private List<ProblemEntity> problemList;
}

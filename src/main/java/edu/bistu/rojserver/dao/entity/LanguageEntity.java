package edu.bistu.rojserver.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "languages")
public class LanguageEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long languageID;

    @Column(nullable = false, unique = true)
    private String name;

    private String compileCommand;

    @Column(nullable = false)
    private String executeCommand;
}

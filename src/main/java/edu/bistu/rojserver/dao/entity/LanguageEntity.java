package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity(name = "language")
@Table(indexes = @Index(name = "index_name", columnList = "name"))
public class LanguageEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Long languageID;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "compile_command")
    private String compileCommand;

    @Column(nullable = false, name = "execute_command")
    private String executeCommand;
}

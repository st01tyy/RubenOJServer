package edu.bistu.rojserver.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "languages")
public class LanguageEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long languageID;

    private String languageName;

    private String languageVerifyCommand;

    private String languageVerifyResult;
}

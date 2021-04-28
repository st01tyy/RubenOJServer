package edu.bistu.rojserver.domain.jsonmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageVerifyResult
{
    private List<Language> languageList;
}

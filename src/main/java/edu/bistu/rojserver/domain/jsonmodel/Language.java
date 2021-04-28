package edu.bistu.rojserver.domain.jsonmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language
{
    private String languageName;
    private String topicName;
    private String compileCommand;
    private String executeCommand;
}

package edu.bistu.rojserver.domain.jsonmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeResult
{
    private Long submissionID;
    private String result;
    private Integer executionTime;
    private Integer memoryUsage;
}

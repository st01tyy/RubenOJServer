package edu.bistu.rojserver.domain.jsonmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shared.SubmissionResult;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeResult
{
    private Long submissionID;
    private SubmissionResult result;
    private Integer executionTime;
    private Integer memoryUsage;
}

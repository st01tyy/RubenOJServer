package edu.bistu.rojserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionFetchResult
{
    private Long submissionID;
    private String result;
    private String time;
    private String memory;
}

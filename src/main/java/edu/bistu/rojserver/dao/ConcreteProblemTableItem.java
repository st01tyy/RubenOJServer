package edu.bistu.rojserver.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteProblemTableItem implements ProblemTableItem
{
    private Long problemID;
    private String title;
    private Integer acceptedSubmissionNumber;
    private Integer difficulty;
}

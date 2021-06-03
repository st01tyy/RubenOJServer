package edu.bistu.rojserver.domain;

import lombok.Data;

import java.util.List;

@Data
public class ProblemSelectForm
{
    private Long contestID;
    private Long problemID;
    private List<Long> problems;
}

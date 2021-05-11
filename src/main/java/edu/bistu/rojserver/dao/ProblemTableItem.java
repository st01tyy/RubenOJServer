package edu.bistu.rojserver.dao;

public interface ProblemTableItem
{
    Long getProblemID();
    String getTitle();
    Integer getAcceptedSubmissionNumber();
    Integer getDifficulty();
}

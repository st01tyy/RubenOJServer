package edu.bistu.rojserver.dao;

public interface SubmissionTableItem
{
    Integer getSubmissionID();
    Long getSubmitDate();
    String getTitle();
    Integer getExecutionTime();
    Integer getMemoryUsage();
    String getResult();
}

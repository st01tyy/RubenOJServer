package edu.bistu.rojserver.domain;

import edu.bistu.rojserver.dao.entity.TestCaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase
{
    private Long caseID;
    private String inputFileName;
    private String inputContent;
    private String outputFileName;
    private String outputContent;

    public TestCase(Long caseID, String inputFileName, String outputFileName)
    {
        this.caseID = caseID;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public static TestCase fromEntity(TestCaseEntity entity)
    {
        TestCase testCase = new TestCase(entity.getCaseID(), entity.getInputFileName(), entity.getOutputFileName());
        testCase.setInputContent(entity.getInputContent());
        testCase.setOutputContent(entity.getOutputContent());
        return testCase;
    }
}

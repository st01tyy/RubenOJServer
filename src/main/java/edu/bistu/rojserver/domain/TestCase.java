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
        testCase.setInputContent(fromByteArray(entity.getInput()));
        testCase.setOutputContent(fromByteArray(entity.getOutput()));
        return testCase;
    }

    private static String fromByteArray(byte[] arr)
    {
        StringBuilder sb = new StringBuilder(new String(arr, 0, Math.min(arr.length, 200), StandardCharsets.UTF_8));    //默认不含中文等多字节文字
        if(sb.length() == 200)
            sb.append("...");
        return sb.toString();
    }
}

package edu.bistu.rojserver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseCreateForm
{
    private Long problemID;
    private String new_input;
    private String new_output;
}

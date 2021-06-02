package edu.bistu.rojserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TestSubmitForm extends SubmitForm
{
    private String username;
}

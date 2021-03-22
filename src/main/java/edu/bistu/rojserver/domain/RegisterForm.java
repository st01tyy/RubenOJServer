package edu.bistu.rojserver.domain;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class RegisterForm
{
    @Size(min = 8, max = 16)
    private String username;

    @Size(min = 8, max = 20)
    private String password;

    private String passwordConfirm;

    private boolean registerFailed;

    private String errorMessage;

    public RegisterForm(){}

}

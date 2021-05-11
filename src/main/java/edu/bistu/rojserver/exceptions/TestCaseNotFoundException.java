package edu.bistu.rojserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "TestCaseNotFound")
public class TestCaseNotFoundException extends Exception
{
    public TestCaseNotFoundException()
    {
        this(null);
    }

    public TestCaseNotFoundException(String msg)
    {
        super(msg);
    }
}

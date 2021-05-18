package edu.bistu.rojserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to create test case.")
public class TestCaseCreateException extends Exception
{
    public TestCaseCreateException()
    {
        this(null);
    }

    public TestCaseCreateException(String msg)
    {
        super(msg);
    }
}

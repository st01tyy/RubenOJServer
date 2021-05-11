package edu.bistu.rojserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "ProblemNotFound")
public class ProblemNotFoundException extends Exception
{
    public ProblemNotFoundException()
    {
        this(null);
    }

    public ProblemNotFoundException(String msg)
    {
        super(msg);
    }
}

package edu.bistu.rojserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to create submission")
public class SubmissionCreateException extends Exception
{
    public SubmissionCreateException()
    {
        this(null);
    }

    public SubmissionCreateException(String msg)
    {
        super(msg);
    }
}

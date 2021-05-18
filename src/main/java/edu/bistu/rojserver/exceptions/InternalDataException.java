package edu.bistu.rojserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Data Error")
public class InternalDataException extends Exception
{
    public InternalDataException()
    {
        this(null);
    }

    public InternalDataException(String msg)
    {
        super(msg);
    }
}

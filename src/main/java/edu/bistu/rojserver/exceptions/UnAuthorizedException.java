package edu.bistu.rojserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "您没有权限访问该页面")
public class UnAuthorizedException extends Exception
{
    public UnAuthorizedException()
    {
        this(null);
    }

    public UnAuthorizedException(String msg)
    {
        super(msg);
    }
}

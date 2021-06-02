package edu.bistu.rojserver.domain;

import lombok.Data;

@Data
public class UserTableFilterRequest
{
    private String role;
    private String username;
}

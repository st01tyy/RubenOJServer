package edu.bistu.rojserver.domain.jsonmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Limit
{
    private Integer timeLimit;
    private Integer memoryLimit;
}

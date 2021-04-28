package shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase implements Serializable
{
    private static final long serialVersionUID = 19990914L;

    private byte[] input;
    private byte[] output;
}

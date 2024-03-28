package junit;

import org.junit.Test;
import static org.junit.Assert.*;
import static server.Connection.testValidatePortInput;

public class Tests {
    @Test
    public void testPortInput() {
        Boolean result2 = testValidatePortInput("");
        assertFalse(result2);

        Boolean result3 = testValidatePortInput("abcd");
        assertFalse(result3);

        Boolean result4 = testValidatePortInput("@@@@");
        assertFalse(result4);

        Boolean result5 = testValidatePortInput("8888");
        assertTrue(result5);
    }
}

package mapitems;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class UnknownDungeonItemExceptionTest {

    @Test
    public void testCustomMessageInError() {
        String customError = "custom error";
        UnknownDungeonItemException exception = new UnknownDungeonItemException(customError);
        assertThat(exception.getMessage(), equalTo(customError));
    }
}
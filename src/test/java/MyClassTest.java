import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class MyClassTest {

    @Test
    public void testMe() {
        assertThat(MyClass.getGreeting(), equalTo("Hello, world!"));
    }
}

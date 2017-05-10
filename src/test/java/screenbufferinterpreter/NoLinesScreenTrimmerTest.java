package screenbufferinterpreter;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class NoLinesScreenTrimmerTest {

    @Test
    public void testDeletesNoLines() {
        char[][] screenBuffer = new char[][]{
                {'1'}
        };
        ScreenTrimmer trimmer = new NoLinesScreenTrimmer();
        assertThat(trimmer.trim(screenBuffer), equalTo(new char[][]{{'1'}}));
    }
}

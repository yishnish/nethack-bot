package screenbufferinterpreter;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class TextLinesScreenTrimmerTest {

    @Test
    public void testTrimsTopTwoAndBottomThreeTextFromTopAndBottomOfScreenBuffer(){
        ScreenTrimmer trimmer = new TextLinesScreenTrimmer();
        char[][] screenBuffer = new char[][]{
                {'1'},
                {'2'},
                {'@'},
                {'4'},
                {'5'},
                {'6'},
        };

        assertThat(trimmer.trim(screenBuffer), equalTo(new char[][]{{'@'}}));
    }
}

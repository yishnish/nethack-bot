package screenbufferinterpreter;

import java.util.Arrays;

public class TextLinesScreenTrimmer implements ScreenTrimmer{

    public static final int NUM_TOP_SCREEN_TEXT_ROWS = 2;
    public static final int NUM_BOTTOM_SCREEN_TEXT_ROWS = 3;

    public char[][] trim(char[][] screenBuffer) {
        int rowCount = screenBuffer.length - NUM_TOP_SCREEN_TEXT_ROWS - NUM_BOTTOM_SCREEN_TEXT_ROWS;
        int columnCount = screenBuffer[0].length;
        char[][] newRow = new char[rowCount][columnCount];
        for (int i = NUM_TOP_SCREEN_TEXT_ROWS; i < screenBuffer.length - NUM_BOTTOM_SCREEN_TEXT_ROWS; i++) {
            newRow[i - NUM_TOP_SCREEN_TEXT_ROWS] = Arrays.copyOf(screenBuffer[i], screenBuffer[i].length);
        }
        return newRow;
    }
}

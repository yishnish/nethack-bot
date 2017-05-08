package bot;

import level.NethackLevel;
import screen.NethackScreen;
import terminal.TimePiece;

public class NethackBot {

    public static final long TIME_UNTIL_LEVEL_CONSIDERED_STABLE = 500L;

    private final TimePiece timePiece;

    private long lastRequestTimestamp = 0L;

    public NethackBot(TimePiece timePiece) {
        this.timePiece = timePiece;
    }

    public void getLevelFromScreen(NethackScreen nethackScreen) {
        NethackLevel level;
        while ((level = nethackScreen.getSuspectedNewAndStableLevel(lastRequestTimestamp,
                TIME_UNTIL_LEVEL_CONSIDERED_STABLE)) == null) {
            lastRequestTimestamp = timePiece.getTimeMillis();
        }
    }
}

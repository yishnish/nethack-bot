package bot;

import level.NethackLevel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import screen.NethackScreen;
import terminal.TimePiece;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class NethackBotTest {

    NethackScreen nethackScreen;

    TimePiece timePiece;

    @Before
    public void setUp() {
        nethackScreen = mock(NethackScreen.class);
        timePiece = mock(TimePiece.class);
    }

    @Test
    public void nethackBotShouldAskForANethackLevelUntilItGetsOne() {
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        NethackBot nethackBot = new NethackBot(timePiece);
        nethackBot.getLevelFromScreen(nethackScreen);
        verify(nethackScreen, times(2)).getSuspectedNewAndStableLevel(anyLong(), anyLong());
    }

    @Test
    public void nethackBotShouldOnlyAskForNethackLevelsThatAreCreatedAfterTheLastRequest() {
        InOrder inOrder = Mockito.inOrder(nethackScreen);
        when(nethackScreen.getSuspectedNewAndStableLevel(anyLong(), anyLong()))
                .thenReturn(null).thenReturn(new NethackLevel(new char[1][1]));
        when(timePiece.getTimeMillis()).thenReturn(1L).thenReturn(2L).thenReturn(3L);

        NethackBot nethackBot = new NethackBot(timePiece);
        nethackBot.getLevelFromScreen(nethackScreen);

        inOrder.verify(nethackScreen).getSuspectedNewAndStableLevel(0L, NethackBot.TIME_UNTIL_LEVEL_CONSIDERED_STABLE);
        inOrder.verify(nethackScreen).getSuspectedNewAndStableLevel(1L, NethackBot.TIME_UNTIL_LEVEL_CONSIDERED_STABLE);
    }
}

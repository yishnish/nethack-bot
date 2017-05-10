[![Build Status](https://travis-ci.org/yishnish/nethack-bot.svg?branch=master)](https://travis-ci.org/yishnish/nethack-bot)    [![codecov](https://codecov.io/gh/yishnish/nethack-bot/branch/master/graph/badge.svg)](https://codecov.io/gh/yishnish/nethack-bot)

Just another foolish @ trying to make its way in this world.

Something like this to get a dude to do a thing:

```java
public class Main {

    public static void main(String... args) throws IOException, InterruptedException {
        NethackBot nethackBot = new NethackBot(new UTCTimePiece());
        VTerminal terminal = new Vermont(new PrintStreamDisplay(System.out));
        MyTelnetNegotiator telnetNegotiator = new MyTelnetNegotiator(terminal, new TelnetClient());
        telnetNegotiator.setMode(TerminalMode.ONES_BASED);
        NethackScreenInterpreter screenInterpreter = new NethackScreenInterpreter(new TextLinesScreenTrimmer());
        NethackScreen nethackScreen = new NethackScreen(terminal, screenInterpreter);

        try {
            telnetNegotiator.connect("localhost");
            Thread.sleep(100);
            telnetNegotiator.sendLine("vagrant");
            Thread.sleep(100);
            telnetNegotiator.sendLine("vagrant");
            Thread.sleep(100);
            telnetNegotiator.sendLine("nethack");
            Thread.sleep(100);

            telnetNegotiator.sendLine("");

            while (true) {
                Thread.sleep(1000L);
                clearScreenMessages(telnetNegotiator);
                NethackLevel level = nethackBot.getLevelFromScreen(nethackScreen);
                telnetNegotiator.send(nethackBot.getNextMove(level).getCommand());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            telnetNegotiator.sendLine("S");
            Thread.sleep(100);
            telnetNegotiator.sendLine("y");
        }
    }

    private static void clearScreenMessages(MyTelnetNegotiator telnetNegotiator) throws IOException {
        telnetNegotiator.send(Ascii.ESC);
    }
}
```
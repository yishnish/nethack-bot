package mapitems;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DungeonThing {
    VACANT('.'),
    HERO('@'),
    HORIZONTAL_WALL('-'),
    VERTICAL_WALL('|'),
    STAIRWAY_UP('<'),
    CLOSED_DOOR('+'),
    HALLWAY('#'),
    EMPTY_WHAT_IS_THIS_REALLY(' '),
    UNKNOWN(Character.MIN_VALUE),
    LIZARDO(':'),
    FELINE('f');

    private static Map<Character, DungeonThing> converter = Collections.unmodifiableMap(initializeMapping());

    private final Character displayCharacter;

    DungeonThing(Character displayCharacter) {
        this.displayCharacter = displayCharacter;
    }

    private static Map<Character, DungeonThing> initializeMapping() {
        Map<Character, DungeonThing> map = new HashMap<Character, DungeonThing>();
        for (DungeonThing value : values()) {
            map.put(value.displayCharacter, value);
        }
        return map;
    }

    public static DungeonThing forCharacter(Character character) {
        DungeonThing dungeonThing = converter.get(character);
        if (dungeonThing == null) {
            throw new UnknownDungeonItemException("Unknown character: [" + character + "]");
        }
        return dungeonThing;
    }
}

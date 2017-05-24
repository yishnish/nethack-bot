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
    BOULDER('`'),
    ALTAR('_'),
    GOLD('$'),
    FOOD('%'),
    RING('='),
    ARMOR('['),
    WEAPON(')'),
    TOOL('('),
    WAND('/'),
    SCROLL('?'),
    EMPTY_WHAT_IS_THIS_REALLY(' '),
    UNKNOWN(Character.MIN_VALUE),
    LIZARDO(':'),
    BAT('B'),
    YELLOW_LIGHT('y'),
    CANINE('d'),
    FUNGUS('F'),
    RODENT('r'),
    FELINE('f'),
    KOBOLD('k'),
    ORC('o');

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

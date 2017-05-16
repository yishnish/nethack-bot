package collections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionsHelpers<T> {
    public static <T> Set<T> setOf(T... args) {
        return new HashSet<T>(Arrays.asList(args));
    }
}

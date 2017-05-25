package collections;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class CollectionsHelpers<T> {
    public static <T> Set<T> setOf(T... args) {
        return new LinkedHashSet<T>(Arrays.asList(args));
    }
}

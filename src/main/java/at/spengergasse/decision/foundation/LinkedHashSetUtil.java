package at.spengergasse.decision.foundation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LinkedHashSetUtil {

    @SafeVarargs
    public static <T> LinkedHashSet<T> of(T... elements) {
        LinkedHashSet<T> set = new LinkedHashSet<>();
        Collections.addAll(set, elements);
        return set;
    }
}

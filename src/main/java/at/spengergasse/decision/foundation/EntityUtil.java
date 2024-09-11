package at.spengergasse.decision.foundation;

import java.util.UUID;

/**
 * Entity utility class for assisting in basic entity operations.
 */
public abstract class EntityUtil {

    /**
     * Generates a UUIDv4.
     */
    public static String generateUUIDv4() {
        return UUID.randomUUID().toString();
    }
}

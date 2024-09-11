package at.spengergasse.decision.security.password;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for password encoding and strength assessment.
 *
 * <p>In this class we use the Zxcvbn library to assess the strength of a password
 * and the Spring Security PasswordEncoder to encode it.
 */

// <<Outer Class>>
@Service
@RequiredArgsConstructor
public class PasswordService {

    // Zxcvbn score threshold for password strength
    public static final int ZXCVBN_STRENGTH_THRESHOLD = 3;

    // Zxcvbn password strength assessment library
    private final Zxcvbn zxcvbn = new Zxcvbn();

    // PasswordEncoder from SecurityConfig
    private final PasswordEncoder passwordEncoder;

    /**
     * Checks password strength and encodes it with a cryptographic hashing functions
     *
     * @param rawPassword the password to be encoded
     * @return the encoded password as a custom type {@link EncodedPassword}
     * @throws IllegalArgumentException if the password is too weak (zxcvbn score < 3)
     */
    public EncodedPassword encode(String rawPassword) {
        // 1. Password strength assessment
        Strength measure = zxcvbn.measure(rawPassword);
        if (measure.getScore() < ZXCVBN_STRENGTH_THRESHOLD)
            throw new IllegalArgumentException("Password to weak; score " + measure.getScore());

        // 2. Password hashing
        String encodePassword = passwordEncoder.encode(rawPassword);

        // 3. Return custom type
        return new EncodedPassword(encodePassword);
    }

    // <<Inner Class>>
    @Getter
    public static class EncodedPassword {
        // The hashed password
        // e.g {bcrypt}$2a$10$/YeAGfv.i/NgnUCYygA0/uDomrkxM4Ji5ksO3d5UIS7zU0S/1epOi
        private final String encodedPassword;

        // Private constructors are only accessible from within the class and the <<Outer Class>>.
        // So only `PasswordService` can create `EncodedPassword` objects.
        private EncodedPassword(String hashedValue) {
            this.encodedPassword = hashedValue;
        }
    }
}

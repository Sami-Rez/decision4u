package at.spengergasse.decision.security;

import at.spengergasse.decision.security.password.PasswordEncoderConfig;
import at.spengergasse.decision.security.password.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {PasswordService.class})
@Import(PasswordEncoderConfig.class)
public class PasswordServiceTest {
    public static final String STRONG_PASSWORD = "alle meine 99 java entchen lieben C#";
    public static final String WEAK_PASSWORD = "password";

    @Autowired
    PasswordService passwordService;

    @Test
    public void encode_shouldFail_whenProvidingWeakPasswords() {
        assertThrows(
                IllegalArgumentException.class,
                () -> passwordService.encode(WEAK_PASSWORD));
    }

    @Test
    public void encode_shouldPass_whenProvidingStrongPasswords() {
        assertThat(
                passwordService.encode(STRONG_PASSWORD),
                is(notNullValue()));
    }

    @Test
    public void encode_shouldReturnHashes_whenProvidingStrongPasswords() {
        assertThat(
                passwordService.encode(STRONG_PASSWORD).getEncodedPassword(),
                is(not(equalTo(STRONG_PASSWORD))));
    }

    @Test
    public void encode_shouldProduceDifferentHashes_whenProvidingSamePasswords() {
        assertThat(
                passwordService.encode(STRONG_PASSWORD).getEncodedPassword(),
                is(not(equalTo(passwordService.encode(STRONG_PASSWORD).getEncodedPassword()))));
    }
}

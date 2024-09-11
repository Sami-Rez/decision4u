package at.spengergasse.decision.fixture;

import at.spengergasse.decision.domain.user.Profile;
import at.spengergasse.decision.domain.user.Role;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.security.password.PasswordService;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

public class UserFixture {
    // some random users
    public static final String EMAIL1 = "user1@spengergasse.at";
    public static final String EMAIL2 = "user2@spengergasse.at";
    public static final String EMAIL3 = "user3@spengergasse.at";

    public static final String PASSWORD = "spengergasse";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    private static final PasswordService passwordService =
        new PasswordService(createDelegatingPasswordEncoder());

    private static final PasswordService.EncodedPassword encodedPassword =
        passwordService.encode(PASSWORD);

    public static User createUser() {
        return createUser(EMAIL1);
    }

    public static User createUser(String email) {
        var profile = new Profile(FIRST_NAME, LAST_NAME);
        return new User(email, encodedPassword, Role.USER, profile);
    }
}

package at.spengergasse.decision.domain.user;

import at.spengergasse.decision.domain.BaseEntity;
import at.spengergasse.decision.security.password.PasswordService.EncodedPassword;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static at.spengergasse.decision.foundation.AssertUtil.isValidEmail;
import static at.spengergasse.decision.foundation.EntityUtil.generateUUIDv4;

/**
 * A user of the application.
 */
@Getter
@ToString
@Document(collection = "user")
@TypeAlias("user")
public class User extends BaseEntity<String> {

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    // What is the user allowed to do
    private Role role;

    // The user's profile information
    private Profile profile;

    // The user's account information like enabled, email verification tokens, etc.
    private Account account;


    // ctor --------------------------------------------

    // Constructor for Spring Data to use when creating a new user from DB into memory.
    // Spring Data uses reflection to create an instance of this class.
    @PersistenceCreator
    @JsonCreator
    protected User(String id) {
        super(id);
    }

    // Constructor for us developers to use when creating a new user in memory.
    public User(String email, EncodedPassword password, Role role, Profile profile) {
        super(generateUUIDv4());

        this.email = isValidEmail(email, "email");
        this.password = password.getEncodedPassword();

        this.role = role;
        this.profile = profile;
        this.account = new Account();
    }
}

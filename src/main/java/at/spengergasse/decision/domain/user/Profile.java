package at.spengergasse.decision.domain.user;

import lombok.Getter;

import static at.spengergasse.decision.foundation.AssertUtil.hasMaxText;

/**
 * Profile of a user.
 *
 * <p>Here we can place base profile information of a user.
 */
@Getter
public class Profile {

    private String firstName;
    private String lastName;

    // private Media avatar;

    public Profile(String firstName, String lastName /*Media avatar*/) {
        this.firstName = hasMaxText(firstName, 255, "firstName");
        this.lastName = hasMaxText(lastName, 255, "lastName");
        // That means the user has to upload an avatar if not we can make it nullable.
        // this.avatar = isNotNull(avatar, "avatar");
    }
}

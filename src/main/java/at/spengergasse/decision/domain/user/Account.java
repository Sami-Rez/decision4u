package at.spengergasse.decision.domain.user;

import lombok.Data;

/**
 * Account of a user.
 *
 * <p>Here we can place base account information of a user.
 */
@Data
public class Account {
    // Is the account enabled or not?
    private boolean enabled = true;
}

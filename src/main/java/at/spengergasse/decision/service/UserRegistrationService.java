package at.spengergasse.decision.service;

import at.spengergasse.decision.domain.user.Profile;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.presentation.commands.Commands.UserRegistrationCommand;
import at.spengergasse.decision.persistence.UserRepository;
import at.spengergasse.decision.presentation.views.Views.UserRegisteredView;
import at.spengergasse.decision.security.password.PasswordService;
import at.spengergasse.decision.security.password.PasswordService.EncodedPassword;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static at.spengergasse.decision.domain.user.Role.USER;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationService.class);

    // Helps us to retrieve users from the database.
    private final UserQueryService userQueryService;

    // Helps us to hash passwords and check password strength.
    private final PasswordService passwordService;

    // Helps us to save users in the database.
    private final UserRepository userRepository;


    // Register User
    // --------------------------------------------------------------------------------------------
    public UserRegisteredView register(UserRegistrationCommand command) {
        LOGGER.debug("User registration {}", command);

        // 1. Check if email is not taken if not throw exception
        userQueryService.checkEmailNotTaken(command.email());

        // 2. Check password strength / hash password
        var encodedPassword = passwordService.encode(command.password());

        // 3. Instantiate/save user (with account disabled!) in DB
        var user = createUserFrom(command, encodedPassword);
        var savedUser = userRepository.save(user);

        LOGGER.debug("User registration successful {}", user);
        return new UserRegisteredView(savedUser.getId());
    }


    // Private Helper Methods for Domain Layer Logic
    // --------------------------------------------------------------------------------------------

    private User createUserFrom(UserRegistrationCommand command, EncodedPassword password) {
        var profile = new Profile(command.firstName(), command.lastName());
        return new User(command.email(), password, USER, profile);
    }
}

package at.spengergasse.decision.presentation;

import at.spengergasse.decision.presentation.commands.Commands.UserRegistrationCommand;
import at.spengergasse.decision.presentation.views.Views.UserRegisteredView;
import at.spengergasse.decision.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class UserRegistrationController {
    // private final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
    private final UserRegistrationService userRegistrationService;


    /**
     * Registers a new user.
     *
     * @param command the user registration command
     * @return 201 Created with the user id and the location header
     */
    @PostMapping
    public ResponseEntity<UserRegisteredView> register(@RequestBody UserRegistrationCommand command) {
        var registerView = userRegistrationService.register(command);

        // Creates the location header
        URI uri = URI.create("/api/user/" + registerView.userId());
        return ResponseEntity.created(uri).body(registerView);
    }
}

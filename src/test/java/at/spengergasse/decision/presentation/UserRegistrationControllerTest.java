package at.spengergasse.decision.presentation;

import at.spengergasse.decision.presentation.helpers.ApiTester;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static at.spengergasse.decision.fixture.CommandFixtures.USER_REGISTRATION_COMMAND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

public class UserRegistrationControllerTest extends AbstractControllerTest {

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }


    @Test
    public void givenNonExistingUser_whenUserRegisters_ThenReceivesUserRegisteredView() {

        // Given: A user registration command
        var command = USER_REGISTRATION_COMMAND;

        // When: A non existing user registers
        var response = ApiTester.registerUser(command);

        // Then: A user account is created
        assertThat(response.getStatusCode(), equalTo(CREATED.value()));

        // And: The response contains the userId
        JsonPath jsonPath = response.getBody().jsonPath();
        assertThat(jsonPath.getString("userId"), notNullValue());
    }


    @Test
    public void givenExistingUser_whenUserRegisters_ThenReceivesBadRequest() {

        // Given: A user has already registered
        var command = USER_REGISTRATION_COMMAND;
        ApiTester.registerUser(command);

        // When: The user attempts to register a second time
        var response = ApiTester.registerUser(command);

        // Then: The user receives a bad request response
        assertThat(response.statusCode(), is(BAD_REQUEST.value()));

        // And: The response contains the expected message
        assertThat(response.getBody().asString(), containsStringIgnoringCase("email already taken"));
    }
}

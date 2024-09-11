package at.spengergasse.decision.presentation;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static at.spengergasse.decision.presentation.helpers.ApiTester.loginUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;


public class UserControllerTest extends AbstractControllerTest {

    @BeforeEach
    public void setup() {
        cleanAndPopulateDB();
    }

    @Test
    public void givenAuthenticatedUser_whenLogin_thenReceivesLoginView() {

        // When: Registered user logs in
        var response = loginUser(authToken);

        // Then: The user is authenticated
        assertThat(response.getStatusCode(), equalTo(OK.value()));

        // And: The response contains login view
        JsonPath jsonPath = response.getBody().jsonPath();
        assertThat(jsonPath.getString("authUser"), notNullValue());
        assertThat(jsonPath.getList("otherUsers"), hasSize(2));
        assertThat(jsonPath.getList("createdProjects"), hasSize(1));
        assertThat(jsonPath.getList("invitedProjects"), hasSize(1));
    }
}

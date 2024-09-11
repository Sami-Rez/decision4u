package at.spengergasse.decision.presentation.helpers;

import at.spengergasse.decision.presentation.commands.Commands.ProjectCreateCommand;
import at.spengergasse.decision.presentation.commands.Commands.ProjectVoteCommand;
import at.spengergasse.decision.presentation.commands.Commands.UserRegistrationCommand;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public abstract class ApiTester {
  public static final String API_REGISTRATION = "/api/registration";
  public static final String API_LOGIN = "/api/user";
  public static final String API_PROJECTS = "/api/project";

  public record AuthToken(String email, String password) { }

  public static Response registerUser(UserRegistrationCommand command) {
    // spotless:off
    Response response =
        given()
            .contentType("application/json")
            .body(command)
            .when()
            .post(API_REGISTRATION)
            .then()
            .extract()
            .response();
    // spotless:on

    return response;
  }

  // creates login with Basic Auth header
    public static Response loginUser(AuthToken token) {
        // spotless:off
        Response response =
            given()
                .auth()
                .preemptive()
                .basic(token.email, token.password)
            .when()
                .get(API_LOGIN)
            .then()
                .extract()
                .response();
        // spotless:on

        return response;
    }

    public static Response createProject(AuthToken token, ProjectCreateCommand command) {
        // spotless:off
        Response response =
            given()
                .auth()
                .preemptive()
                .basic(token.email, token.password)
                .contentType("application/json")
                .body(command)
            .when()
                .post(API_PROJECTS)
            .then()
                .extract()
                .response();
        // spotless:on

        return response;
    }

    public static Response voteOnProject(AuthToken token, ProjectVoteCommand command) {
        // spotless:off
        return given()
                .auth()
                .preemptive()
                .basic(token.email, token.password)
                .contentType("application/json")
                .body(command)
            .when()
                // TODO rollback
                //.post(API_PROJECTS + "/" + command.projectId() + "/vote")
                .put(API_PROJECTS)
            .then()
                .extract()
                .response();
        // spotless:on
    }
}

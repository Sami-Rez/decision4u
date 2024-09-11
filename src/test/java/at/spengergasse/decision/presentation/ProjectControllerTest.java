package at.spengergasse.decision.presentation;

import at.spengergasse.decision.presentation.helpers.ApiTester;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static at.spengergasse.decision.fixture.CommandFixtures.PROJECT_CREATION_COMMAND;
import static at.spengergasse.decision.fixture.CommandFixtures.PROJECT_VOTE_COMMAND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;


public class ProjectControllerTest extends AbstractControllerTest {

    @BeforeEach
    public void setup() {
        cleanAndPopulateDB();
    }


    @Test
    public void givenProjectCommand_whenCreateProject_thenProjectIsCreated() {

        // Given: A project creation command
        var command = PROJECT_CREATION_COMMAND;

        // When: A user creates a project
        var response = ApiTester.createProject(authToken, command);

        // Then: The project is created
        assertThat(response.statusCode(), is(CREATED.value()));

        // And: The response contains the projectId
        JsonPath jsonPath = response.getBody().jsonPath();
        assertThat(jsonPath.getString("projectId"), notNullValue());
    }


    @Test
    public void givenInvitedUserInProject_whenVoteOnProject_thenReceivesVoteProcessedView() {

        // Given: A user has not voted on a project yet
        var command = PROJECT_VOTE_COMMAND.withProjectId(otherUserProject.getId());

        // When: The user votes on the project
        var response = ApiTester.voteOnProject(authToken, command);

        // Then: The vote is processed
        assertThat(response.statusCode(), is(OK.value()));

        // And: The response contains the voteId and scores
        JsonPath jsonPath = response.getBody().jsonPath();
        assertThat(jsonPath.getString("voteId"), notNullValue());
        assertThat(jsonPath.getString("scores"), notNullValue());
    }


    @Test
    public void givenUserHasAlreadyVoted_whenVoteOnProject_thenReceivesBadRequest() {

        // Given: A user has already voted on a project
        var command = PROJECT_VOTE_COMMAND.withProjectId(otherUserProject.getId());
        ApiTester.voteOnProject(authToken, command);

        // When: The user attempts to vote a second time
        var response = ApiTester.voteOnProject(authToken, command);

        // Then: The user receives a bad request response
        assertThat(response.statusCode(), is(BAD_REQUEST.value()));

        // And: The response contains the expected message
        assertThat(response.getBody().asString(), containsStringIgnoringCase("has already voted"));
    }



    @Test
    public void givenUserNotVoter_whenVoteOnProject_thenReceivesBadRequest() {

        // Given: A user who tries to vote on a project he is not part of voting list e.g. the creator
        var projectId = authUserProject.getId();
        var command = PROJECT_VOTE_COMMAND.withProjectId(projectId);

        // When: The non-voter attempts to vote
        var response = ApiTester.voteOnProject(authToken, command);

        // Then: The user receives a bad request response
        assertThat(response.statusCode(), is(BAD_REQUEST.value()));

        // And: The response contains the expected message
        assertThat(response.getBody().asString(), containsStringIgnoringCase("not part of the voters list"));
    }
}

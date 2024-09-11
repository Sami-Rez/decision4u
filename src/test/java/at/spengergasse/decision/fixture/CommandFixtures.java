package at.spengergasse.decision.fixture;

import at.spengergasse.decision.presentation.commands.Commands.ProjectCreateCommand;
import at.spengergasse.decision.presentation.commands.Commands.ProjectVoteCommand;
import at.spengergasse.decision.presentation.commands.Commands.UserRegistrationCommand;

import static at.spengergasse.decision.fixture.ProjectFixture.*;
import static at.spengergasse.decision.fixture.UserFixture.*;

public class CommandFixtures {

  // This fixtures are used to test the commands in the presentation layer.

  // They are using the fixtures from the domain layer.
  // e.g. ProjectFixture, UserFixture, VoteFixture

  public static final UserRegistrationCommand USER_REGISTRATION_COMMAND =
      new UserRegistrationCommand(
            EMAIL1, PASSWORD, FIRST_NAME, LAST_NAME);

  public static final ProjectCreateCommand PROJECT_CREATION_COMMAND =
      new ProjectCreateCommand(
            TITLE, DESCRIPTION, VOTER_IDS, ALTERNATIVES, CRITERIA, ALTERNATIVE_JUDGMENTS);

  public static ProjectVoteCommandBuilder PROJECT_VOTE_COMMAND =
      new ProjectVoteCommandBuilder();

  public static class ProjectVoteCommandBuilder {
    public ProjectVoteCommand withProjectId(String projectId) {
      return new ProjectVoteCommand(projectId, CRITERIA_ASSESSMENT);
    }
  }
}

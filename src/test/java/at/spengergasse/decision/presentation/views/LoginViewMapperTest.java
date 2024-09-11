package at.spengergasse.decision.presentation.views;


import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.fixture.ProjectFixture;
import at.spengergasse.decision.fixture.UserFixture;
import at.spengergasse.decision.presentation.views.Views.LoginView;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class LoginViewMapperTest {

    LoginViewMapper mapper = LoginViewMapper.INSTANCE;

    @Test
    public void toLoginView_shouldMapParamsToLoginView() {
        // Given
        User user = UserFixture.createUser();
        List<User> otherUsers = Stream.generate(UserFixture::createUser).limit(3).toList();
        List<Project> createdProjects = Stream.generate(ProjectFixture::createProject).limit(3).toList();
        List<Project> invitedProjects = Stream.generate(ProjectFixture::createProject).limit(3).toList();
        List<Project> votedProjects = Stream.generate(ProjectFixture::createProject).limit(3).toList();

        // When
        LoginView loginView = mapper.toLoginView(user, otherUsers, createdProjects, invitedProjects, votedProjects);

        // Then
        assertThat(loginView, notNullValue());
        assertThat(loginView.otherUsers(), notNullValue());
        assertThat(loginView.createdProjects(), notNullValue());
        assertThat(loginView.invitedProjects(), notNullValue());
        assertThat(loginView.votedProjects(), notNullValue());
    }

}

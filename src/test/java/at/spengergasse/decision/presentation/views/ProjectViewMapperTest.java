package at.spengergasse.decision.presentation.views;

import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.fixture.ProjectFixture;
import at.spengergasse.decision.presentation.views.Views.ProjectView;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ProjectViewMapperTest {

    ProjectViewMapper mapper = ProjectViewMapper.INSTANCE;

    @Test
    public void toProjectView_shouldMapProjectToProjectView() {
        // Given
        Project project = ProjectFixture.createProject();

        // When
        ProjectView projectView = mapper.toProjectView(project);

        // Then
        assertThat(projectView, notNullValue());
        assertThat(projectView.id(), equalTo(project.getId()));
    }

}
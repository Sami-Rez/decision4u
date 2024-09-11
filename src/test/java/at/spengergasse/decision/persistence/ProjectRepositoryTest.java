package at.spengergasse.decision.persistence;

import at.spengergasse.decision.config.MongoConfig;
import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.fixture.ProjectFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@Import(MongoConfig.class)
public class ProjectRepositoryTest {
    @Autowired private ProjectRepository projectRepository;

    private Project projectSaved;

    @BeforeEach
    public void setup() {
        var project = ProjectFixture.createProject();

        projectRepository.deleteAll();
        projectSaved = projectRepository.save(project);
    }

    @Test
    public void saveProject_shouldReturnSavedProject() {
        assertThat(projectSaved, notNullValue());
    }

    @Test
    public void saveProject_shouldSetAuditFields() {
        // Then
        assertThat(projectSaved.getCreatedAt(), notNullValue());
        assertThat(projectSaved.getLastModifiedAt(), notNullValue());
        assertThat(projectSaved.getVersion(), notNullValue());
        assertThat(projectSaved.getVersion(), equalTo(0L));
    }

    @Test
    public void findById_shouldReturnProject_whenProjectExists() {
        // When
        var projectFound = projectRepository.findById(projectSaved.getId());

        // Then
        assertThat(projectFound.isPresent(), is(true));
        assertThat(projectFound.get().getId(), equalTo(projectSaved.getId()));
    }

    @Test
    public void saveProject_shouldFail_withOldVersion() {
        // When
        // version = 0
        var projectRead1 = projectRepository.findById(projectSaved.getId()).get();
        var projectRead2 = projectRepository.findById(projectSaved.getId()).get();

        // When
        // userRead1 version = 0; DB version = 0 -> version 1
        projectRepository.save(projectRead1);

        // userRead2 version = 0; DB version = 1
        // Has it been modified meanwhile
        assertThrows(OptimisticLockingFailureException.class,
                () -> projectRepository.save(projectRead2));
    }




}

package at.spengergasse.decision.persistence;

import at.spengergasse.decision.config.MongoConfig;
import at.spengergasse.decision.domain.project.Vote;
import at.spengergasse.decision.fixture.VoteFixture;
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
public class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;

    private Vote voteSaved;

    @BeforeEach
    public void setup() {
        var vote = VoteFixture.createVote();

        voteRepository.deleteAll();
        voteSaved = voteRepository.save(vote);
    }

    @Test
    public void saveVote_shouldReturnSavedVote() {
        assertThat(voteSaved, notNullValue());
    }

    @Test
    public void saveVote_shouldSetAuditFields() {
        // Then
        assertThat(voteSaved.getCreatedAt(), notNullValue());
        assertThat(voteSaved.getLastModifiedAt(), notNullValue());
        assertThat(voteSaved.getVersion(), notNullValue());
        assertThat(voteSaved.getVersion(), equalTo(0L));
    }

    @Test
    public void findById_shouldReturnVote_whenVoteExists() {
        // When
        var voteFound = voteRepository.findById(voteSaved.getId());

        // Then
        assertThat(voteFound.isPresent(), is(true));
        assertThat(voteFound.get().getId(), equalTo(voteSaved.getId()));
    }

    @Test
    public void saveUser_shouldFail_withOldVersion() {
        // When
        // version = 0
        var voteRead1 = voteRepository.findById(voteSaved.getId()).get();
        var voteRead2 = voteRepository.findById(voteSaved.getId()).get();

        // When
        // userRead1 version = 0; DB version = 0 -> version 1
        voteRepository.save(voteRead1);

        // userRead2 version = 0; DB version = 1
        // Has it been modified meanwhile
        assertThrows(OptimisticLockingFailureException.class,
                () -> voteRepository.save(voteRead2));
    }

}

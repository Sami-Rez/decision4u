package at.spengergasse.decision.persistence;

import at.spengergasse.decision.domain.project.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {

    List<Vote> findAllByProjectId(String projectId);

    List<Vote> findAllByVoterId(String voterId);

    boolean existsByProjectIdAndVoterId(String projectId, String voterId);
}

package at.spengergasse.decision.persistence;

import at.spengergasse.decision.domain.project.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    // find all where i am creator
    List<Project> findAllByCreatorId(String creatorId);

    // find all where i am voter
    List<Project> findAllByVoterIdsContaining(String voterId);
}

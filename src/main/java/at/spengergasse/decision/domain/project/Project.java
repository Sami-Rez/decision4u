package at.spengergasse.decision.domain.project;

import at.spengergasse.decision.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

import static at.spengergasse.decision.domain.project.ProjectAssertUtil.checkAlternativeJudgments;
import static at.spengergasse.decision.domain.project.ProjectAssertUtil.checkCriteriaAssessments;
import static at.spengergasse.decision.foundation.AssertUtil.*;
import static at.spengergasse.decision.foundation.EntityUtil.generateUUIDv4;

/**
 * Represents a structured environment for collective decision-making within an organization or a group.
 *
 * <p>This class encapsulates a decision project, providing a platform where designated participants, known as voters,
 * engage in a systematic process of expressing preferences among various options through voting.</p>
 *
 * @see at.spengergasse.decision.algorithm For details on the AHP interface and its implementation.
 */
@Getter
@ToString
@Document(collection = "project")
@TypeAlias("project")
public class Project extends BaseEntity<String> {

    // Who created this project?
    @Indexed
    private String creatorId;

    // Who can vote in this project?
    @Indexed
    private Set<String> voterIds;

    // A short title of the project.
    private String title;

    // A longer description of the project.
    private String description;

    // The alternatives to be compared.
    private Set<String> alternatives;

    // The criteria to be used for the pairwise comparison.
    private Set<String> criteria;

    // The creator's judgments of how well each criterion is implemented by each alternative.
    private int[][] alternativeJudgments;

    // The score evaluation of the alternatives calculated by the AHP algorithm.
    // However, in the ProjectService, the scores are updated on each vote.
    // Thus, this gives the current voting state of the project.
    @Setter
    private double[] scores;


    // ctor --------------------------------------------

    // Constructor for Spring Data to use when creating a new user from DB into memory.
    // Spring Data uses reflection to create an instance of this class.
    @PersistenceCreator
    @JsonCreator
    protected Project(String id) {
        super(id);
    }

    // Constructor for us developers to use when creating a new user in memory.
    @Builder
    public Project(
        String creatorId, Set<String> voterIds, String title, String description,
        LinkedHashSet<String> alternatives, LinkedHashSet<String> criteria,
        int[][] alternativeJudgments
    ) {
        super(generateUUIDv4());

        this.creatorId = isNotNull(creatorId, "creatorId");
        this.voterIds = hasMinSize(voterIds, 1, "voterIds");

        this.title = hasMaxText(title, 100, "title");
        this.description = hasMaxText(description, 4096, "description");

        this.alternatives = hasMinSize(alternatives, 2, "alternatives");
        this.criteria = hasMinSize(criteria, 2, "criteria");
        this.alternativeJudgments = alternativeJudgments;

        // Check if the alternative judgments are valid if not throw an exception.
        checkAlternativeJudgments(alternativeJudgments, alternatives, criteria);
    }


    // methods --------------------------------------------

    /**
     * Casts a vote for a specific voter in this project.
     */
    public Vote createVoteFrom(String voterId, double[][] criteriaAssessment) {

        // Check if the voter is allowed to vote in this project.
        isTrue(voterIds.contains(voterId), "voter is not allowed to vote in this project");

        // Check if the criteria assessment is valid if not throw an exception.
        checkCriteriaAssessments(criteriaAssessment, criteria);

        // Create a new vote object.
        return new Vote(this.getId(), voterId, criteriaAssessment);
    }


    public boolean isVoter(String id) {
        return voterIds.contains(id);
    }
}

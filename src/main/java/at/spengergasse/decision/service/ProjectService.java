package at.spengergasse.decision.service;

import at.spengergasse.decision.algorithm.AHPProcess;
import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.domain.project.Vote;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.persistence.ProjectRepository;
import at.spengergasse.decision.persistence.VoteRepository;
import at.spengergasse.decision.presentation.commands.Commands.ProjectCreateCommand;
import at.spengergasse.decision.presentation.commands.Commands.ProjectVoteCommand;
import at.spengergasse.decision.presentation.views.Views.ProjectCreatedView;
import at.spengergasse.decision.presentation.views.Views.VoteProcessedView;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static at.spengergasse.decision.foundation.AssertUtil.isTrue;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final VoteRepository voteRepository;
    private final ProjectQueryService projectQueryService;


    // User creates a project
    // --------------------------------------------------------------------------------------------
    public ProjectCreatedView createProject(User authUser, ProjectCreateCommand command) {
        LOGGER.debug("User {} creates Project {}", authUser.getId(), command);

        Project project = createProjectFrom(authUser, command);
        var savedProject = projectRepository.save(project);

        LOGGER.debug("User {} creates project successfully {}", authUser.getId(), savedProject);
        return new ProjectCreatedView(project.getId());
    }


    // User votes on a project
    // --------------------------------------------------------------------------------------------
    public VoteProcessedView voteProject(User authUser, ProjectVoteCommand command) {
        LOGGER.debug("User {} votes on project {}", authUser.getId(), command);

        // Check if the user has not voted yet
        isTrue(projectQueryService.hasUserNotVoted(command.projectId(), authUser.getId()),
            "User cannot vote, he has already voted on project " + command.projectId());

        // Check if user is part of the voters list
        var project = projectQueryService.findById(command.projectId());
        isTrue(project.isVoter(authUser.getId()),
            "User cannot vote, he is not part of the voters list on project " + command.projectId());

        // Create a new vote and combine it with all other votes
        var userVote = project.createVoteFrom(authUser.getId(), command.votes());
        var allVotes = Vote.combine(projectQueryService.findAllVotes(project.getId()), userVote);

        // Execute the AHP process
        var scores = AHPProcess.execute(project.getAlternativeJudgments(), Vote.toCriteriaAssessment(allVotes));
        LOGGER.debug("User {} with vote {} resulted in new score of {}", authUser.getId(), userVote, scores);

        // Save all changes and update the database
        project.setScores(scores);
        voteRepository.save(userVote);
        projectRepository.save(project);

        // Return a view with the vote id and the scores
        LOGGER.debug("User {} votes on project {} successfully", authUser.getId(), command.projectId());
        return new VoteProcessedView(userVote.getId(), scores);
    }


    // Private Helper Methods for Domain Layer Logic
    // --------------------------------------------------------------------------------------------

    private static Project createProjectFrom(User authUser, ProjectCreateCommand command) {
        return Project.builder()
            .creatorId(authUser.getId())
            .title(command.title())
            .description(command.description())
            .voterIds(command.votersIds())
            .alternatives(command.alternatives())
            .criteria(command.criteria())
            .alternativeJudgments(command.alternativeJudgements())
            .build();
    }
}

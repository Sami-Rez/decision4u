package at.spengergasse.decision.presentation.commands;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Commands {

    public record UserRegistrationCommand(
        String email,
        String password,
        String firstName,
        String lastName
    ) {
        public UserRegistrationCommand {
            email = email.trim().toLowerCase();
        }
    }


    public record ProjectCreateCommand(
        String title,
        String description,
        Set<String> votersIds,
        LinkedHashSet<String> alternatives,
        LinkedHashSet<String> criteria,
        int[][] alternativeJudgements
    ) {
        public ProjectCreateCommand {
            title = title.trim();
            description = description.trim();
        }
    }

    public record ProjectVoteCommand(
        String projectId,
        // TODO rename criteriaAssessment
        double[][] votes
    ) {
    }
}

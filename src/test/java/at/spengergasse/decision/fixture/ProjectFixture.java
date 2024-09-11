package at.spengergasse.decision.fixture;

import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.foundation.LinkedHashSetUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProjectFixture {
    // a random project
    public static final String CREATOR_ID = "creator-id";
    public static final String TITLE = "Project Title";
    public static final String DESCRIPTION = "Project Description";
    public static final Set<String> VOTER_IDS = Set.of("voter-id-1", "voter-id-2");
    public static final LinkedHashSet<String> ALTERNATIVES = LinkedHashSetUtil.of("Alternative 1", "Alternative 2");
    public static final LinkedHashSet<String> CRITERIA = LinkedHashSetUtil.of("Criteria 1", "Criteria 2", "Criteria 3");
    public static final int[][] ALTERNATIVE_JUDGMENTS = {{1, 2, 3}, {3, 4, 5}};
    public static final double[][] CRITERIA_ASSESSMENT = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};

    public static Project createProject() {
        return createProject(CREATOR_ID, VOTER_IDS);
    }

    public static Project createProject(String creatorId, Set<String> voterIds) {
        var project = Project.builder()
            .creatorId(creatorId)
            .title(TITLE)
            .description(DESCRIPTION)
            .voterIds(voterIds)
            .alternatives(ALTERNATIVES)
            .criteria(CRITERIA)
            .alternativeJudgments(ALTERNATIVE_JUDGMENTS)
            .build();
        return project;
    }
}

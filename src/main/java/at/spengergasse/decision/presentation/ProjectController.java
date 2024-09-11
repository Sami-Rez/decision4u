package at.spengergasse.decision.presentation;

import at.spengergasse.decision.presentation.commands.Commands.ProjectCreateCommand;
import at.spengergasse.decision.presentation.commands.Commands.ProjectVoteCommand;
import at.spengergasse.decision.presentation.views.Views.ProjectCreatedView;
import at.spengergasse.decision.presentation.views.Views.VoteProcessedView;
import at.spengergasse.decision.security.web.SecurityUser;
import at.spengergasse.decision.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    // private final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    /**
     * Creates a new project.
     *
     * @param command the project creation command
     * @return 201 Created with the created project view and the location header
     */
    @PostMapping
    public ResponseEntity<ProjectCreatedView> createProject(
        @AuthenticationPrincipal SecurityUser principal,
        @RequestBody ProjectCreateCommand command
    ) {
        var view = projectService.createProject(principal.getUser(), command);

        // Creates the location header
        URI uri = URI.create("/api/project/" + view.projectId());
        return ResponseEntity.created(uri).body(view);
    }


    /**
     * Votes for a given project.
     *
     * @param command the vote command
     * @return 201 Created with the created vote view
     */
    // @PostMapping("{projectId}/vote")
    @PutMapping
    public VoteProcessedView voteProject(
        @AuthenticationPrincipal SecurityUser principal,
        // @PathVariable("projectId") Long projectId,
        @RequestBody ProjectVoteCommand command
    ) {
        var view = projectService.voteProject(principal.getUser(), command);

        // No need to return a location header here, as the vote is not a resource
        return view;
    }
}

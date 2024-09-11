package at.spengergasse.decision.service;

import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.presentation.views.LoginViewMapper;
import at.spengergasse.decision.presentation.views.Views.LoginView;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final LoginViewMapper mapper = LoginViewMapper.INSTANCE;

    private final ProjectQueryService projectQueryService;
    private final UserQueryService userQueryService;


    // Login User
    // --------------------------------------------------------------------------------------------
    public LoginView login(User authUser) {
        LOGGER.debug("User {} logins", authUser.getId());

        List<User> otherUsers = userQueryService.findAllExclude(authUser.getId());
        List<Project> createdProjects = projectQueryService.findAllMyCreated(authUser.getId());
        List<Project> invitedProjects = projectQueryService.findAllMyInvited(authUser.getId());
        List<Project> votedProjects = projectQueryService.findAllMyVoted(authUser.getId());

        LOGGER.debug("User {} logins with otherUsers: {}, createdProjects: {}, invitedProjects: {}, votedProjects: {}",
            authUser.getId(), otherUsers, createdProjects, invitedProjects, votedProjects);

        var view = mapper.toLoginView(authUser, otherUsers, createdProjects, invitedProjects, votedProjects);
        LOGGER.debug("User {} logins successfully {}", authUser.getId(), view);
        return view;
    }
}

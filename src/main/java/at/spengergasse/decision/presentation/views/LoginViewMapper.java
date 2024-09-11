package at.spengergasse.decision.presentation.views;

import at.spengergasse.decision.domain.project.Project;
import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.presentation.views.Views.LoginView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {UserViewMapper.class, ProjectViewMapper.class})
public interface LoginViewMapper {

    LoginViewMapper INSTANCE = Mappers.getMapper(LoginViewMapper.class);

    LoginView toLoginView(
        User authUser,
        List<User> otherUsers,
        List<Project> createdProjects,
        List<Project> invitedProjects,
        List<Project> votedProjects
    );
}

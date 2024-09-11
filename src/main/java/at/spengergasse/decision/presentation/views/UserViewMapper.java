package at.spengergasse.decision.presentation.views;

import at.spengergasse.decision.domain.user.User;
import at.spengergasse.decision.presentation.views.Views.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserViewMapper {

    UserViewMapper INSTANCE = Mappers.getMapper(UserViewMapper.class);

    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    UserView toUserView(User user);
}

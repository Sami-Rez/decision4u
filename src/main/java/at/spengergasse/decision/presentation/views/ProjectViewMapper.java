package at.spengergasse.decision.presentation.views;

import at.spengergasse.decision.domain.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectViewMapper {

    ProjectViewMapper INSTANCE = Mappers.getMapper(ProjectViewMapper.class);

    Views.ProjectView toProjectView(Project projectView);
}

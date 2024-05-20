package dev.planifier.Planifier.service;

import dev.planifier.Planifier.data.project.ProjectsRepository;
import dev.planifier.Planifier.data.project.model.Project;
import dev.planifier.Planifier.data.project_data.ProjectDataRepository;
import dev.planifier.Planifier.data.project_data.model.ProjectData;
import dev.planifier.Planifier.exception.ProjectDataNotFoundException;
import dev.planifier.Planifier.exception.ProjectNotFoundException;
import dev.planifier.Planifier.exception.UserNotFoundException;
import dev.planifier.Planifier.security.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class ProjectDataService {

    @Autowired
    private ProjectsRepository projectsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProjectDataRepository projectDataRepository;
    @Autowired
    private ProjectsService projectsService;

    public void createEmptyProjectData(String projectId, String creatorId) throws ProjectNotFoundException, UserNotFoundException {
        if (!projectsRepository.existsById(projectId)) throw new ProjectNotFoundException();
        if (!usersRepository.existsById(creatorId)) throw new UserNotFoundException();

        ProjectData newProjData = new ProjectData();

        newProjData.setProjectId(projectId);
        newProjData.setBudget(0);
        newProjData.setCurrency("$");
        newProjData.setTasks(new ArrayList<>());
        newProjData.setAssignableRoles(List.of("Creator"));
        newProjData.setCrewMembers_Roles(Collections.singletonMap(creatorId, "Creator"));

        projectDataRepository.save(newProjData);
    }

    public ProjectData findByProjectId(String projectId) {
        return projectDataRepository.findByProjectId(projectId).orElseThrow((Supplier<RuntimeException>) ProjectDataNotFoundException::new);
    }

    public void dropProject(String id) {
        ProjectData projectData = projectDataRepository.findByProjectId(id).orElseThrow((Supplier<RuntimeException>) ProjectDataNotFoundException::new);
        projectDataRepository.delete(projectData);
    }

    public boolean testRole(String projectId, String role) {
        try {
            return findByProjectId(projectId).getAssignableRoles().contains(role);
        } catch (ProjectDataNotFoundException _) { return false; }
    }

    public ResponseEntity<?> updateProjectData(String projectID, Consumer<ProjectData> dataConsumer) {
        Project project;
        try {
            project = projectsService.retrieveById(projectID);
        } catch (ProjectNotFoundException _) {
            return ResponseEntity.notFound().build();
        }

        ProjectData projectData;
        try {
            projectData = findByProjectId(projectID);
        } catch (ProjectDataNotFoundException _) {
            return ResponseEntity.notFound().build();
        }

        dataConsumer.accept(projectData);

        projectDataRepository.save(projectData);

        return ResponseEntity.ok(project);
    }

}

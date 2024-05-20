package dev.planifier.Planifier.service;

import dev.planifier.Planifier.data.project.ProjectsRepository;
import dev.planifier.Planifier.data.project.model.Project;
import dev.planifier.Planifier.data.project_data.ProjectDataRepository;
import dev.planifier.Planifier.data.project_data.model.ProjectData;
import dev.planifier.Planifier.exception.ProjectDataNotFoundException;
import dev.planifier.Planifier.exception.ProjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;

@Service
public class ProjectsService {

    @Autowired
    private ProjectsRepository projectsRepository;
    @Autowired
    private ProjectDataRepository projectDataRepository;

    public Project createNewProject(String name) {
        return new Project(name);
    }

    public Project saveProject(Project project) {
        return projectsRepository.save(project);
    }

    public void dropProject(String projectID) {
        projectsRepository.deleteById(projectID);
    }

    public Project retrieveById(String id) {
        return projectsRepository.findById(id).orElseThrow((Supplier<RuntimeException>) ProjectNotFoundException::new);
    }

    public Map<String, String> getAllOf(String userId) {
        Map<String, String> userProjects = new HashMap<>();

        projectsRepository.findAll().forEach(project -> {
            Optional<ProjectData> projectData = projectDataRepository.findByProjectId(project.getID());
            if (projectData.isEmpty()) throw new ProjectDataNotFoundException();
            Map<String, String> crewMembers = projectData.get().getCrewMembers_Roles();

            if (crewMembers.containsKey(userId)) userProjects.put(project.getID(), crewMembers.get(userId));
        });

        return userProjects;
    }
}

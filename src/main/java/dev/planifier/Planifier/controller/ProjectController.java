package dev.planifier.Planifier.controller;

import dev.planifier.Planifier.data.project.model.Project;
import dev.planifier.Planifier.exception.ProjectDataNotFoundException;
import dev.planifier.Planifier.exception.ProjectNotFoundException;
import dev.planifier.Planifier.exception.ProjectTypeNotFoundException;
import dev.planifier.Planifier.security.users.UsersService;
import dev.planifier.Planifier.service.ProjectDataService;
import dev.planifier.Planifier.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
// CHANGE TO MORE SPECIFIC
@CrossOrigin
public class ProjectController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private ProjectsService projectsService;
    @Autowired
    private ProjectDataService projectDataService;

    @GetMapping("/grab")
    public ResponseEntity<?> grabProject(@RequestParam(value = "id") String id) {
        try {
            return ResponseEntity.ok(projectsService.retrieveById(id));
        }
        catch (ProjectNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/projectsOf")
    public ResponseEntity<?> getProjectsOf(@RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok(projectsService.getAllOf(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewProject(
                @RequestParam(value = "creatorId") String creatorId,
            @RequestParam(value = "name") String name) {
        if (!usersService.isUser(creatorId)) return ResponseEntity.notFound().build();

        Project project;
        try {
            project = projectsService.createNewProject(name);
        } catch (ProjectTypeNotFoundException _) {
            return ResponseEntity.notFound().build();
        }

        project = projectsService.saveProject(project);
        projectDataService.createEmptyProjectData(project.getID(), creatorId);

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> dropProject(@RequestParam(value = "id") String id) {
        try {
            projectsService.dropProject(id);
            projectDataService.dropProject(id);
        } catch (ProjectNotFoundException | ProjectDataNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}

package dev.planifier.Planifier.controller;

import dev.planifier.Planifier.data.project.model.Project;
import dev.planifier.Planifier.exception.ProjectDataNotFoundException;
import dev.planifier.Planifier.exception.ProjectNotFoundException;
import dev.planifier.Planifier.security.users.UsersService;
import dev.planifier.Planifier.service.ProjectDataService;
import dev.planifier.Planifier.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project_panel")
// CHANGE TO MORE SPECIFIC
@CrossOrigin
public class ProjectPanelController {

    @Autowired
    private ProjectsService projectsService;
    @Autowired
    private ProjectDataService projectDataService;
    @Autowired
    private UsersService usersService;

    @PostMapping("/name")
    public ResponseEntity<?> rename(@RequestParam("projectID") String projectID, @RequestParam("new_name") String new_name) {
        Project project;
        try {
            project = projectsService.retrieveById(projectID);
        } catch (ProjectNotFoundException _) {
            return ResponseEntity.notFound().build();
        }

        project.setName(new_name);
        projectsService.saveProject(project);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/budget")
    public ResponseEntity<?> setBudget(
            @RequestParam("projectID") String projectID,
            @RequestParam("budget_currency") String budget_currency, @RequestParam("new_budget") double newBudget) {

        return projectDataService.updateProjectData(projectID, data -> {
            data.setCurrency(budget_currency);
            data.setBudget(newBudget);
        });
    }

    @GetMapping("/data")
    public ResponseEntity<?> getData(@RequestParam("projectID") String projectID) {
        try {
            return ResponseEntity.ok(projectDataService.findByProjectId(projectID));
        } catch (ProjectDataNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/roles")
    public ResponseEntity<?> updateAssignableRoles(
            @RequestParam("projectID") String projectID, @RequestParam("roles") List<String> rolesList
    ) {
        return projectDataService.updateProjectData(projectID, data -> data.setAssignableRoles(rolesList));
    }

    @PostMapping("/crew_members")
    public ResponseEntity<?> assignNewCrewMember(
            @RequestParam("projectID") String projectID,
            @RequestParam("member_id") String memberId, @RequestParam("role") String role
    ) {
        if (!projectDataService.testRole(projectID, role) || !usersService.isUser(memberId)) return ResponseEntity.notFound().build();

        return projectDataService.updateProjectData(projectID, data -> {
            Map<String, String> crewMembers = data.getCrewMembers_Roles();
            crewMembers.put(memberId, role);
            data.setCrewMembers_Roles(crewMembers);
        });
    }

    @DeleteMapping("/crew_members")
    public ResponseEntity<?> removeCrewMember(@RequestParam("projectID") String projectID, @RequestParam("member_id") String memberId) {
        return projectDataService.updateProjectData(projectID, data -> {
            Map<String, String> crewMembers = data.getCrewMembers_Roles();
            crewMembers.remove(memberId);
            data.setCrewMembers_Roles(crewMembers);
        });
    }

    @PostMapping("tasks")
    public ResponseEntity<?> updateCurrentTasks(@RequestParam("projectID") String projectID, @RequestParam("tasks") List<String> tasks) {
        return projectDataService.updateProjectData(projectID, data -> data.setTasks(tasks));
    }

}

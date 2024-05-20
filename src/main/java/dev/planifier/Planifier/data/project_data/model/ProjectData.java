package dev.planifier.Planifier.data.project_data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document("projects_data")
@NoArgsConstructor
@Getter @Setter
public class ProjectData {

    @Indexed(unique = true)
    @Id
    private String projectId;

    private double budget;
    private String currency;
    private List<String> assignableRoles;
    private Map<String, String> crewMembers_Roles;
    private List<String> tasks;
}

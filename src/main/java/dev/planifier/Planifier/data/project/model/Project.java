package dev.planifier.Planifier.data.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("created_projects")
@NoArgsConstructor @RequiredArgsConstructor
@Getter @Setter
public class Project implements Serializable {
    @Id
    private String ID;

    @NonNull
    private String name;
}

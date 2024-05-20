package dev.planifier.Planifier.data.project_data;

import dev.planifier.Planifier.data.project_data.model.ProjectData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectDataRepository extends MongoRepository<ProjectData, String> {
    Optional<ProjectData> findByProjectId(String projectId);
}

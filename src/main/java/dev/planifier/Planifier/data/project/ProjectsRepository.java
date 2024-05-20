package dev.planifier.Planifier.data.project;

import dev.planifier.Planifier.data.project.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends MongoRepository<Project, String> {}

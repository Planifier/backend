package dev.planifier.Planifier.security.users;

import dev.planifier.Planifier.security.users.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}

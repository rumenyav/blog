package blog.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import blog.project.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}

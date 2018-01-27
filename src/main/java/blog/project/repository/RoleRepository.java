package blog.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import blog.project.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
Role findByName(String name);

}










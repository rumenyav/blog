package blog.project.repository;

import blog.project.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
Article findBy(String author);

}

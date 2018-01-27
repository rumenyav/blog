package blog.project.controller.admin;

import blog.project.bindingModel.CategoryBindingModel;
import blog.project.entity.Article;
import blog.project.entity.Category;

import blog.project.entity.User;
import blog.project.repository.ArticleRepository;
import blog.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String list(Model model) {

        List<Category> categories = this.categoryRepository.findAll();

        categories = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        model.addAttribute("view", "admin/category/list");
        model.addAttribute("categories", categories);
        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("view", "admin/category/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel) throws IOException {
        if(StringUtils.isEmpty(categoryBindingModel.getName())){
            return "redirect:/admin/categories/create";
        }
        Category category = new Category(categoryBindingModel.getName());

        String encoder = Base64.getEncoder().encodeToString(categoryBindingModel.getCategoryPicture().getBytes());
        category.setCategoryPicture(encoder);
        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);
        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/edit");

        return "base-layout";
    }
    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id,
                                CategoryBindingModel categoryBindingModel) throws IOException {
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.findOne(id);
        category.setName(categoryBindingModel.getName());

        String encoder = Base64.getEncoder().encodeToString(categoryBindingModel.getCategoryPicture().getBytes());
        category.setCategoryPicture(encoder);
        /*String encoded = Base64.getEncoder().encodeToString(categoryBindingModel.getCategoryPicture().getBytes());
        category.setCategoryPicture(encoded);*/

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
     if(!this.categoryRepository.exists(id)){
         return "redirect:/admin/categories/";
     }

     Category category = this.categoryRepository.findOne(id);
     for(Article article : category.getArticles()){
        this.articleRepository.delete(article);
     }
     this.categoryRepository.delete(category);
     return "redirect:/admin/categories/";
    }

}
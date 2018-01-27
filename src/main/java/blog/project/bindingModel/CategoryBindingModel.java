package blog.project.bindingModel;

import org.springframework.web.multipart.MultipartFile;

public class CategoryBindingModel {
    private String name;

    private MultipartFile categoryPicture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryPicture(MultipartFile categoryPicture) {
        this.categoryPicture = categoryPicture;
    }

}

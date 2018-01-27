package blog.project.bindingModel;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class ArticleBindingModel {
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Integer categoryId;
    private String tagString;
    private MultipartFile articlePicture;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategoryId()   {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public MultipartFile getArticlePicture() {
        return articlePicture;
    }

    public void setArticlePicture(MultipartFile articlePicture) {
        this.articlePicture = articlePicture;
    }
}

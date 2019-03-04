package app.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reviewer extends Role
{
    @OneToMany(mappedBy = "reviewer")
    private List<Article> assignedArticles;
    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviews;
    private final String name;

    public Reviewer()
    {
        this.name = Role.reviewer;
        assignedArticles = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public void addAssignedArticle(Article article) {
        assignedArticles.add(article);
    }
}
package app.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reviewer extends User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(mappedBy = "reviewer")
    private List<Article> assignedArticles;
    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviews;

    public Reviewer(String username, String password, String role)
    {
        super(username, password, role);
        assignedArticles = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public void assignArticle(Article article)
    {
        if (article == null) {
            throw new NullPointerException("assignArticle was passed a null article");
        }
        article.setReviewer(this);
        assignedArticles.add(article);
    }
}
package app.models;

import java.util.ArrayList;

public class Reviewer
{
    private ArrayList<Article> assignedArticles;
    private ArrayList<Review> reviews;

    public void assignArticle(Article article)
    {
        if (article == null) {
            throw new NullPointerException("assignArticle was passed a null article");
        }
        article.setReviewer(this);
        assignedArticles.add(article);
    }
}
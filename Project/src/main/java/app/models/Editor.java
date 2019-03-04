package app.models;

import sun.plugin.dom.exception.InvalidStateException;

import javax.persistence.Entity;

@Entity
public class Editor extends Role
{
    private final String name;

    public Editor() {
        name = Role.editor;
    }

    public void acceptArticle(Article article) {
        if (article == null) {
            throw new NullPointerException("acceptArticle was passed a null article");
        }
        article.setAccepted(true);
    }

    public void rejectArticle(Article article) {
        if (article == null) {
            throw new NullPointerException("rejectArticle was passed a null article");
        }
        article.setAccepted(false);
    }

    public void assignArticle(Article article, User user)
    {
        if (article == null) {
            throw new NullPointerException("assignArticle was passed a null article");
        }

        if (user == null) {
            throw new NullPointerException("assignArticle was passed a null user");
        }

        if (user.getRole() instanceof Reviewer)
        {
            Reviewer reviewer = (Reviewer) user.getRole();
            article.addAuthorizedUser(user);
            article.setReviewer(reviewer);
            reviewer.addAssignedArticle(article);
        }
        else {
            throw new InvalidStateException("assignArticle was called on a user without the Reviewer role");
        }
    }
}

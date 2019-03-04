package app.models;

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
}

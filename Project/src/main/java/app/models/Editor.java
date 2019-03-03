package app.models;

public class Editor {

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

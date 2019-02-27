public class Article
{
    private Reviewer reviewer;
    private Review review;
    private enum ArticleState {
        SUBMITTED,
        IN_REVIEW,
        ACCEPTED,
        REJECTED
    }
    private ArticleState state;


    public Article(Reviewer reviewer) {
        this.reviewer = reviewer;

        if (reviewer != null) {
            state = ArticleState.IN_REVIEW;
        }
        else {  // reviewer == null
            state = ArticleState.SUBMITTED;
        }
    }

    public Article() {
        this(null);
    }

    public ArticleState getState() {
        return state;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Reviewer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Reviewer reviewer) {
        if (this.reviewer == null) {
            this.reviewer = reviewer;
        }
        else {  // this.reviewer != null
            throw new IllegalStateException("setReviewer was called on an article with an existing reviewer");
        }
    }

    public void setAccepted(boolean accepted) {
        if (review != null) {
            state = (accepted) ? ArticleState.ACCEPTED : ArticleState.REJECTED;
        }
        else {  // review == null
            throw new IllegalStateException("setAccepted was called on an article with no review");
        }
    }
}
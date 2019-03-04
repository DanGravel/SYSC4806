package app.models;

public enum ArticleState {
    SUBMITTED("Submitted"),
    IN_REVIEW("In Review"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String text;

    ArticleState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

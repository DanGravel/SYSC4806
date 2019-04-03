package app.models;

public enum ArticleState {
    // enum by default implements a toString() method
    // ArticleState.SUBMITTED.toString() -> "SUBMITTED"
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

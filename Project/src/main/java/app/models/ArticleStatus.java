package app.models;

public enum ArticleStatus {
    SUBMITTED("SUBMITTED"),
    INREVIEW("IN REVIEW"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED");


    private String status;

    ArticleStatus(final String value) {
        this.status = value;
    }

    public String getValue() {
        return status;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
package app.models;

public enum Role {
    EDITOR("EDITOR"), REVIEWER("REVIEWER"), SUBMITTER("SUBMITTER");

    private String role;

    Role(final String value) {
        this.role = value;
    }

    public String getValue() {
        return role;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}

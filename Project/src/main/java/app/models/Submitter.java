package app.models;

import javax.persistence.Entity;

@Entity
public class Submitter extends Role {
    private final String name;

    public Submitter() {
        this.name = Role.submitter;
    }
}

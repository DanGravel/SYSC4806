package app.models;

import javax.persistence.Entity;

@Entity
public class Submitter extends Role {

    public Submitter() {
        name = Role.submitter;
    }

    @Override
    public String toString() {
        return name;
    }
}

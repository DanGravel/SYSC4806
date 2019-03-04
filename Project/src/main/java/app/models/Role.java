package app.models;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="descriminatorColumn")
public abstract class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public static final String editor = "EDITOR";
    public static final String reviewer = "REVIEWER";
    public static final String submitter = "SUBMITTER";

    protected String name;

    public abstract String toString();
}

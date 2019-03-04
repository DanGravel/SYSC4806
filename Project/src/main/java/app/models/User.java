package app.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    @OneToOne
    private Role role;

    @OneToMany(mappedBy = "submitter", cascade = CascadeType.ALL)
    private List<Article> submittedArticles;

    public User() {
        this(null, null, null);
    }
    public User(String username, String password, String role){
        this.username = username;
        this.password = password;

        if (role != null) {
            switch (role) {
                case Role.reviewer: {
                    this.role = new Reviewer();
                }
                case Role.editor: {
                    this.role = new Editor();
                }
            }
        }
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRole(String role) {
        if (role != null) {
            switch (role) {
                case Role.reviewer: {
                    this.role = new Reviewer();
                    break;
                }
                case Role.editor: {
                    this.role = new Editor();
                    break;
                }
            }
        }
    }
}

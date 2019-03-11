package app.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Article
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<User> users;
    private String fileName;
    private String fileType;
    private String review;
    private ArticleState state;

    private Date date;

    @Lob
    private byte[] data;

    public Article() {
        this.users = new ArrayList<>();
        state = ArticleState.SUBMITTED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addAuthorizedUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public User getReviewer()
    {
        return users.stream().filter(u -> u.getRole().equals(Role.REVIEWER)).findFirst().get();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDate() {

        String pattern = "yyyy-MM-dd hh:mm a"; //e.g 2019-03-11 12:04 AM
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getState() {
        return state.toString();
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void addReviewer(Role caller, User user) {
        if (user == null) {
            throw new NullPointerException("addReviewer was passed a null user");
        }

        if (!(user.getRole() == Role.REVIEWER)) {
            throw new IllegalStateException("addReviewer was called on a user without the Reviewer role");
        }

        if (caller == Role.EDITOR) {
            this.state = ArticleState.IN_REVIEW;
            addAuthorizedUser(user);
        }
        else {
            throw new IllegalStateException("Non-editor called addReviewer");
        }
    }

    public void setAccepted(boolean accepted, Role caller) {
        if (!(caller == Role.EDITOR)) {
            throw new IllegalStateException("Non-editor called setAccepted");
        }

        if (review != null) {
            state = (accepted) ? ArticleState.ACCEPTED : ArticleState.REJECTED;
        }
        else {  // review == null
            throw new IllegalStateException("setAccepted was called on an article with no review");
        }
    }
}
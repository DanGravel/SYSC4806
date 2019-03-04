package app.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Article
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> users;
    private String fileName;
    private String fileType;

    @ManyToOne
    private Reviewer reviewer;
    @OneToOne(cascade = CascadeType.ALL)
    private Review review;
    private enum ArticleState {
        SUBMITTED("Submitted"),
        IN_REVIEW("In Reivew"),
        ACCEPTED("Accepted"),
        REJECTED("Rejected");

        private final String text;

        private ArticleState(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    private ArticleState state;

    @Lob
    private byte[] data;

    public Article(Reviewer reviewer) {
        this.reviewer = reviewer;
        this.users = new ArrayList<>();

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<User> getAuthorizedUsers() {
        return users;
    }

    public void setAuthorizedUsers(List<User> users) {
        this.users = users;
    }

    public void addAuthorizedUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
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

    public String getState() {
        return state.toString();
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
            this.state = ArticleState.IN_REVIEW;
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
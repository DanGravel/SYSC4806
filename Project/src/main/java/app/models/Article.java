package app.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private String reviewFileType;
    private ArticleState state;
    private Date date;
    private Date reviewDueDate;
    private Date reviewSubmissionDate;

    @Lob
    private byte[] data;
    @Lob
    private byte[] review;

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

    public void removeUser(User user) {
        users.remove(user);
    }

    public User getReviewer() {
        return users.stream()
            .filter(u -> u.getRole().equals(Role.REVIEWER))
            .findFirst()
            .orElse(null);
    }

    public void removeReviewer() {
        removeUser(this.getReviewer());
        state = ArticleState.SUBMITTED;
    }

    public String getReviewFileType() { return reviewFileType; }

    public void setReviewFileType(String reviewFileType) { this.reviewFileType = reviewFileType; }

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

    public void deleteReview() {
        this.setReview(null);
        this.setReviewFileType(null);
    }

    public String getDate() {
        String pattern = "yyyy-MM-dd hh:mm a"; //e.g 2019-03-11 12:04 AM
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getReviewDueDate() {
        return this.reviewDueDate;
    }
    public void setReviewSubmissionDate(Date date) {
        this.reviewSubmissionDate = date;
    }

    public Date getReviewSubmissionDate() {
        return this.reviewDueDate;
    }

    public String getFormattedDueDate() {
        if(this.reviewDueDate == null) return null;
        return new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a").format(this.reviewDueDate);
    }
    public String getFormattedReviewSubmissionDate() {
        if(this.reviewSubmissionDate == null) return null;
        return new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a").format(this.reviewSubmissionDate);
    }

    public void setReviewDueDate(Date date) {
        this.reviewDueDate = date;
    }

    public String getState() {
        return state.toString();
    }

    public byte[] getReview() {
        return review;
    }

    public void setReview(byte[] review) {
        this.review = review;
    }

    public boolean hasReview() { return this.review != null;}

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

    public boolean isAccepted() {
        return (state == ArticleState.ACCEPTED);
    }

    public boolean isRejected() {
        return (state == ArticleState.REJECTED);
    }

    /**
     * Checks to see if a review has been submitted on time
     * @return boolean true if review has been submitted on time
     */
    public boolean isOnTime() {
        if(reviewDueDate != null) {
            if(reviewSubmissionDate != null) {
                return reviewDueDate.after(reviewSubmissionDate); //Checks review date against submission date
            }
            return reviewDueDate.after(new Date()); //Checks review date against current date
        }
        return true; //On time if you dont have a due date
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(users, article.users) &&
                Objects.equals(fileName, article.fileName) &&
                Objects.equals(fileType, article.fileType) &&
                Objects.equals(reviewFileType, article.reviewFileType) &&
                state == article.state &&
                Objects.equals(date, article.date) &&
                Objects.equals(reviewDueDate, article.reviewDueDate) &&
                Objects.equals(reviewSubmissionDate, article.reviewSubmissionDate) &&
                Arrays.equals(data, article.data) &&
                Arrays.equals(review, article.review);
    }
}
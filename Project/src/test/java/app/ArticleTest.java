package app;

import app.Utils.StringUtils;
import app.models.Article;
import app.models.ArticleState;
import app.models.Role;
import app.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleTest {
    public static final String IN_REVIEW = "In Review";
    public static final String SUBMITTED = "Submitted";
    public static final String BASIC_DATE_FORMAT = "yyyy-MM-dd hh:mm a";
    public static final String REVIEW = "This article was absolute *#@#!";
    public static final String FORMATTED_DATE = "EEEE, MMMM d, yyyy h:m a";
    public static final String TEXT_PLAIN = "text/plain";
    private Article testArticle;
    private User testReviewer;
    private User testEditor;
    private User testSubmitter;

    @Before
    public void setup() {
        testArticle = new Article();
        testArticle.setId(1);
        testArticle.setDate(new Date());

        testReviewer = new User();
        testReviewer.setUsername("beee");
        testReviewer.setRole(Role.REVIEWER);

        testEditor = new User();
        testEditor.setUsername("weeeee");
        testEditor.setRole(Role.EDITOR);

        testSubmitter = new User();
        testSubmitter.setUsername("greeeed");
        testSubmitter.setRole(Role.SUBMITTER);
    }

    @Test
    public void testAddReviewer_ReviewerRole() {
        testArticle.addReviewer(Role.EDITOR, testReviewer);

        assert(testArticle.getUsers().contains(testReviewer));
        assert(testArticle.getState().equals(IN_REVIEW));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddReviewer_SubmitterRole() {
        testArticle.addReviewer(Role.EDITOR, testSubmitter);
    }

    @Test(expected = NullPointerException.class)
    public void testAddReviewer_NoUser() {
        testArticle.addReviewer(Role.EDITOR, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddReviewer_NotEditor() {
        testArticle.addReviewer(Role.REVIEWER, testReviewer);
    }

    @Test
    public void testGetReviewer() {
        testArticle.addReviewer(Role.EDITOR, testReviewer);
        User reviewer = testArticle.getReviewer();
        assertEquals(reviewer, testReviewer);
    }

    @Test
    public void testRemoveReviewer() {
        testArticle.addReviewer(Role.EDITOR, testReviewer);
        testArticle.removeReviewer();
        assertEquals(null, testArticle.getReviewer());
        assertEquals(SUBMITTED, testArticle.getState());
    }

    @Test
    public void testGetDate() {
        String pattern = BASIC_DATE_FORMAT;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date currDate = new Date();
        testArticle.setDate(currDate);

        assertEquals(format.format(currDate), testArticle.getDate());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetAccepted_NoEditor() {
        testArticle.setAccepted(true, Role.REVIEWER);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetAccepted_NoReview() {
        testArticle.setAccepted(true, Role.EDITOR);
    }

    @Test
    public void testSetAccepted() {
        byte[] review = REVIEW.getBytes();
        testArticle.setReview(review);

        testArticle.setAccepted(true, Role.EDITOR);
        assertTrue(testArticle.isAccepted());

        testArticle.setAccepted(false, Role.EDITOR);
        assertFalse(testArticle.isAccepted());
    }

    @Test
    public void testIsOnTime_NoReviewDueDate() {
        //If there's no due date, then the review is always on time
        //*taps forehead*
        assertTrue(testArticle.isOnTime());
    }

    @Test
    public void testIsOnTime_NoSubmissionDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);
        Date currDate = c.getTime();

        testArticle.setReviewDueDate(currDate);
        assertTrue(testArticle.isOnTime());

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -5);
        currDate = c.getTime();
        testArticle.setReviewDueDate(currDate);
        assertFalse(testArticle.isOnTime());
    }

    @Test
    public void testIsOnTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);
        Date currDate = c.getTime();
        Date submissionDate = new Date();

        testArticle.setReviewSubmissionDate(submissionDate);
        testArticle.setReviewDueDate(currDate);
        assertTrue(testArticle.isOnTime());

        c = Calendar.getInstance();
        c.add(Calendar.DATE, -5);
        currDate = c.getTime();
        testArticle.setReviewDueDate(currDate);
        assertFalse(testArticle.isOnTime());
    }

    @Test
    public void testGetFormattedDates() {
        String pattern = FORMATTED_DATE;
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date d = new Date();

        testArticle.setReviewSubmissionDate(d);
        testArticle.setReviewDueDate(d);

        String formattedDate = format.format(d);
        assertEquals(formattedDate, testArticle.getFormattedDueDate());
        assertEquals(formattedDate, testArticle.getFormattedReviewSubmissionDate());
    }

    @Test
    public void testDeleteReview() {
        byte[] review = REVIEW.getBytes();
        testArticle.setReview(review);
        testArticle.setReviewFileType(TEXT_PLAIN);

        testArticle.deleteReview();
        assertEquals(null, testArticle.getReview());
        assertEquals(null, testArticle.getReviewFileType());
    }
}

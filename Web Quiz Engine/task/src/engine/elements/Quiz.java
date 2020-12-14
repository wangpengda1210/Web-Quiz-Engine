package engine.elements;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "Quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "options")
    private String[] options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "answer")
    private int[] answer;

    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Quiz() {
        answer = new int[0];
    }

    public Quiz(String title, String text, String[] options, int[] answer) {
        this();
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;

        Arrays.sort(this.answer);
    }

    public Quiz(String title, String text, String[] options, int[] answer, User user) {
        this(title, text, options, answer);

        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public int getId() {
        return id;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

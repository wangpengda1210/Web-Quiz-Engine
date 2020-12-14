package engine.elements;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Completions")
public class Completion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "completionID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int completionID;

    @Column(name = "id")
    private int id;

    @Column(name = "completedAt")
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "userID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Completion() {

    }

    public Completion(int id, User user) {
        this.id = id;
        completedAt = LocalDateTime.now();
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCompletionID() {
        return completionID;
    }

    public void setCompletionID(int completionID) {
        this.completionID = completionID;
    }

}

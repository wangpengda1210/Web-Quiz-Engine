package engine.elements;

public class Feedback {

    private boolean success;
    private String feedback;

    private Feedback(boolean success, String feedback) {
        this.setSuccess(success);
        this.setFeedback(feedback);
    }

    public static final Feedback SUCCESS_FEEDBACK = new Feedback(true,
            "Congratulations, you're right!");
    public static final Feedback FAILURE_FEEDBACK = new Feedback(false,
            "Wrong answer! Please, try again.");

    public boolean isSuccess() {
        return success;
    }

    private void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    private void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

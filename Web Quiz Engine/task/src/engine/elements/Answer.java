package engine.elements;

import java.util.Arrays;

public class Answer {

    private int[] answer;

    public Answer() {
        answer = new int[0];
    }

    public Answer(int[] answer) {
        this();
        this.answer = answer;
        Arrays.sort(this.answer);
    }

    public int[] getAnswer() {
        return answer;
    }

}

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String stringWithHtmlTags = scanner.nextLine();

        // write your code here
        System.out.println(stringWithHtmlTags.replaceAll("<.*?>", ""));
    }
}
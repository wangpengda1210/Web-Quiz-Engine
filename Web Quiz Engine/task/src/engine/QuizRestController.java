package engine;

import engine.database.CompletionRepository;
import engine.database.QuizRepository;
import engine.database.UserRepository;
import engine.elements.*;
import engine.services.CompletionService;
import engine.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class QuizRestController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompletionRepository completionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private QuizService quizService;

    @Autowired
    private CompletionService completionService;

    @GetMapping("/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The quiz with id " + id + " not found");
        } else {
            return quiz.get();
        }
    }

    @GetMapping("/quizzes")
    public ResponseEntity<Page<Quiz>> getAllQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy) {

        Page<Quiz> quizzes = quizService.getAllQuizzes(page, pageSize, sortBy);

        return new ResponseEntity<>(quizzes, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = "/quizzes/{id}/solve", consumes = "application/json")
    public Feedback solveQuiz(@PathVariable int id, @RequestBody Answer answers, Principal principal) {

        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The quiz with id " + id + " not found");
        } else {
            int[] sortedAnswer = answers.getAnswer().clone();
            Arrays.sort(sortedAnswer);
            int[] sortedSolution = quiz.get().getAnswer();
            Arrays.sort(sortedSolution);
            if (Arrays.equals(sortedAnswer, sortedSolution)) {
                assert userRepository.findByEmail(principal.getName()).isPresent();
                User user = userRepository.findByEmail(principal.getName()).get();
                completionRepository.save(new Completion(id, user));
                return Feedback.SUCCESS_FEEDBACK;
            } else {
                return Feedback.FAILURE_FEEDBACK;
            }
        }
    }

    @PostMapping(value = "/quizzes", consumes = "application/json")
    public Quiz addQuiz(@RequestBody Quiz quiz, Principal principal) {
        if (quiz.getTitle() == null || quiz.getTitle().equals("")) {
            throw new QuizNotValidException("Quiz title cannot be empty");
        } else if (quiz.getText() == null || quiz.getText().equals("")) {
            throw new QuizNotValidException("Quiz text cannot be empty");
        } else if (quiz.getOptions() == null || quiz.getOptions().length < 2) {
            throw new QuizNotValidException("Quiz should have more than two options");
        } else if (Arrays.stream(quiz.getAnswer())
                .anyMatch(i -> i < 0 || i > quiz.getOptions().length - 1)) {
            throw new QuizNotValidException("The correct answers should be from the options");
        } else {
            assert userRepository.findByEmail(principal.getName()).isPresent();
            return quizRepository.save(new Quiz(quiz.getTitle(), quiz.getText(), quiz.getOptions(),
                    quiz.getAnswer(), userRepository.findByEmail(principal.getName()).get()));
        }
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public void register(@RequestBody User user) {
        if (!(user.getEmail().contains("@") && user.getEmail().contains("."))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email format not valid");
        } else if (user.getPassword().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Password should have at least five characters");
        } else if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The email has already been taken");
        } else {
            userRepository.save(new User(user.getEmail(), bCryptPasswordEncoder.encode(user.getPassword())));
        }
    }

    @DeleteMapping(value = "/quizzes/{id}")
    public void deleteQuiz(@PathVariable int id, Principal principal) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            if (quiz.getUser().getEmail().equals(principal.getName())) {
                quizRepository.deleteById(id);
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/quizzes/completed")
    public ResponseEntity<Page<Completion>> getCompletions(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "completionID") String sortBy,
                                                    Principal principal) {

        assert userRepository.findByEmail(principal.getName()).isPresent();
        Page<Completion> completions = completionService.getAllCompletionsByUser(page, pageSize, sortBy,
                userRepository.findByEmail(principal.getName()).get());

        return new ResponseEntity<>(completions, new HttpHeaders(), HttpStatus.OK);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class QuizNotValidException extends RuntimeException {

        private final String message;

        public QuizNotValidException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

    }

}

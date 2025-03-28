package com.quizhub.quizhub.controller;
import com.quizhub.quizhub.dto.QuizAttemptDTO;
import com.quizhub.quizhub.model.QuestionResponse;
import com.quizhub.quizhub.model.QuizAttempt;
import com.quizhub.quizhub.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/attempts")
public class QuizAttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;

    @PostMapping("/start")
    public ResponseEntity<QuizAttempt> startQuiz(
            @RequestParam Long quizId,
            @RequestParam Long studentId) {
        QuizAttempt attempt = quizAttemptService.startQuiz(quizId, studentId);
        return ResponseEntity.ok(attempt);
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizAttempt> submitQuiz(
            @RequestParam Long attemptId,
            @RequestBody List<QuestionResponse> responses) {
        // Log the received data for debugging
        System.out.println("Received attemptId: " + attemptId);
        System.out.println("Received responses: " + responses.size());
        
        QuizAttempt attempt = quizAttemptService.submitQuiz(attemptId, responses);
        return ResponseEntity.ok(attempt);
    }

    @GetMapping("/{quizId}/user/{studentId}")
    public ResponseEntity<List<QuizAttempt>> getAttempts(
            @PathVariable Long quizId,
            @PathVariable Long studentId) {
        // You would add a repository method like:
        // List<QuizAttempt> findByQuizIdAndStudentId(Long quizId, Long studentId);
        // For now, assume it's implemented in the service.
        List<QuizAttempt> attempts = quizAttemptService.getAttemptsByQuizAndStudent(quizId, studentId);
        return ResponseEntity.ok(attempts);
    }

    private float calculateScore(List<QuestionResponse> responses) {
        float total = 0F;

        for (QuestionResponse response : responses) {
            // Use 0 if score is null, otherwise convert to float
            float questionScore = response.getScore() != null ? response.getScore().floatValue() : 0F;

            // If a hint was used, reduce the score by 25%
            if (Boolean.TRUE.equals(response.getHintUsed())) {
                questionScore *= 0.75F; // 25% deduction (i.e., 75% of original)
            }

            total += questionScore;
        }

        return total;
    }

    @GetMapping("/user/{studentId}")
    public ResponseEntity<List<QuizAttempt>> getAttemptsByUser(@PathVariable("studentId") Long studentId) {
        List<QuizAttempt> attempts = quizAttemptService.getAttemptsByStudent(studentId);
        return ResponseEntity.ok(attempts);
    }


    @GetMapping("/all/{studentId}")
    public ResponseEntity<List<QuizAttemptDTO>> getAttemptsByStudent(@PathVariable("studentId") Long studentId) {
        List<QuizAttemptDTO> attemptsDto = quizAttemptService.getAttemptsWithQuizInfo(studentId);
        return ResponseEntity.ok(attemptsDto);
    }

    // (Optional) Implement a delete endpoint if needed:
    @DeleteMapping("/{attemptId}")
    public ResponseEntity<Void> deleteAttempt(@PathVariable("attemptId") Long attemptId) {
        quizAttemptService.deleteAttempt(attemptId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{attemptId}")
    public ResponseEntity<QuizAttemptDTO> getAttemptDetails(@PathVariable("attemptId") Long attemptId) {
        QuizAttemptDTO dto = quizAttemptService.getAttemptDetails(attemptId);
        return ResponseEntity.ok(dto);
    }

}

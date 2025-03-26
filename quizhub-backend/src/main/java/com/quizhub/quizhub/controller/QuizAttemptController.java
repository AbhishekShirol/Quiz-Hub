package com.quizhub.quizhub.controller;

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

    private int calculateScore(List<QuestionResponse> responses) {
        int total = 0;
        final int HINT_DEDUCTION = 2; // Deduct 2 points if a hint is used

        for (QuestionResponse response : responses) {
            int questionScore = response.getScore() != null ? response.getScore().intValue() : 0;
            if (Boolean.TRUE.equals(response.getHintUsed())) {
                questionScore = Math.max(0, questionScore - HINT_DEDUCTION);
            }
            total += questionScore;
        }
        return total;
    }

}

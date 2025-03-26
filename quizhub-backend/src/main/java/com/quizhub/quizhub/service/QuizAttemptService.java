package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.QuestionResponse;
import com.quizhub.quizhub.model.QuizAttempt;
import com.quizhub.quizhub.repository.QuestionResponseRepository;
import com.quizhub.quizhub.repository.QuizAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private QuestionResponseRepository questionResponseRepository;

    public QuizAttempt startQuiz(Long quizId, Long studentId) {
        QuizAttempt attempt = QuizAttempt.builder()
                .quizId(quizId)
                .studentId(studentId)
                .startTime(LocalDateTime.now())
                .build();
        return quizAttemptRepository.save(attempt);
    }

    public QuizAttempt submitQuiz(Long attemptId, List<QuestionResponse> responses) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        responses.forEach(r -> r.setQuizAttempt(attempt));
        questionResponseRepository.saveAll(responses);

        attempt.setEndTime(LocalDateTime.now());
        int score = calculateScore(responses);
        attempt.setScore(score);

        return quizAttemptRepository.save(attempt);
    }

    public List<QuizAttempt> getAttemptsByQuizAndStudent(Long quizId, Long studentId) {
        return quizAttemptRepository.findByQuizIdAndStudentId(quizId, studentId);
    }

    private int calculateScore(List<QuestionResponse> responses) {
        int total = 0;
        final int HINT_DEDUCTION = 2;
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
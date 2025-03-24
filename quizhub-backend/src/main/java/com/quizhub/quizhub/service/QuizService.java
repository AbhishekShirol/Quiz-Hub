package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.Quiz;
import com.quizhub.quizhub.model.User;
import com.quizhub.quizhub.repository.QuizRepository;
import com.quizhub.quizhub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    // Get authenticated user ID
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Authenticated Username: " + username); // Debugging log

        return userRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("User Found: ID = " + user.getId()); // Debugging log
                    return user.getId();
                })
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    // Create Quiz
    public Quiz createQuiz(Quiz quiz) {
        quiz.setCreatedBy(getAuthenticatedUserId()); // ✅ Correctly storing user ID
        return quizRepository.save(quiz);
    }

    // Get all quizzes for the logged-in user
    public List<Quiz> getAllQuizzesForUser() {
        return quizRepository.findByCreatedBy(getAuthenticatedUserId());
    }

    // Get a quiz by ID (only if created by the logged-in user)
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findByIdAndCreatedBy(id, getAuthenticatedUserId());
    }

    // Update a quiz (only if created by the logged-in user)
    public Optional<Quiz> updateQuiz(Long id, Quiz updatedQuiz) {
        Optional<Quiz> existingQuiz = quizRepository.findByIdAndCreatedBy(id, getAuthenticatedUserId());

        if (existingQuiz.isPresent()) {
            Quiz quiz = existingQuiz.get();
            quiz.setTitle(updatedQuiz.getTitle());
            quiz.setQuestions(updatedQuiz.getQuestions());
            return Optional.of(quizRepository.save(quiz));
        }
        return Optional.empty();
    }

    // Delete a quiz (only if created by the logged-in user)
    public boolean deleteQuiz(Long id) {
        Optional<Quiz> existingQuiz = quizRepository.findByIdAndCreatedBy(id, getAuthenticatedUserId());

        if (existingQuiz.isPresent()) {
            quizRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

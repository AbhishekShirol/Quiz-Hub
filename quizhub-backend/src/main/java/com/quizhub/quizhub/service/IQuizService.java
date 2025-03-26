package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.Quiz;

import java.util.List;
import java.util.Optional;

public interface IQuizService {
    Quiz saveQuiz(Quiz quiz);
    List<Quiz> getQuizzesByUser(Long userId);
    Optional<Quiz> getQuizByIdAndUser(Long id, Long userId);
    void deleteQuiz(Long id);
}

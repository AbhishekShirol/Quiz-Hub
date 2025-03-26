package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.Quiz;
import com.quizhub.quizhub.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService implements IQuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public List<Quiz> getQuizzesByUser(Long userId) {
        return quizRepository.findByGeneratedBy_Id(userId);
    }

    @Override
    public Optional<Quiz> getQuizByIdAndUser(Long id, Long userId) {
        return quizRepository.findByIdAndGeneratedBy_Id(id, userId);
    }

    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}

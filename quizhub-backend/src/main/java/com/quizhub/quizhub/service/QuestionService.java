package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.Question;
import com.quizhub.quizhub.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<String> getAllTopics() {
        return questionRepository.findAll().stream().map(Question::getTopic).distinct().toList();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        Question existingQuestion = getQuestionById(id);

        if (updatedQuestion.getQuestionText() != null) {
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
        }
        if (updatedQuestion.getQuestionType() != null) {
            existingQuestion.setQuestionType(updatedQuestion.getQuestionType());
        }
        if (updatedQuestion.getOptionA() != null) {
            existingQuestion.setOptionA(updatedQuestion.getOptionA());
        }
        if (updatedQuestion.getOptionB() != null) {
            existingQuestion.setOptionB(updatedQuestion.getOptionB());
        }
        if (updatedQuestion.getOptionC() != null) {
            existingQuestion.setOptionC(updatedQuestion.getOptionC());
        }
        if (updatedQuestion.getOptionD() != null) {
            existingQuestion.setOptionD(updatedQuestion.getOptionD());
        }
        if (updatedQuestion.getCorrectOption() != null) {
            existingQuestion.setCorrectOption(updatedQuestion.getCorrectOption());
        }
        if (updatedQuestion.getHint() != null) {
            existingQuestion.setHint(updatedQuestion.getHint());
        }
        if (updatedQuestion.getDifficulty() != null) {
            existingQuestion.setDifficulty(updatedQuestion.getDifficulty());
        }
        if (updatedQuestion.getTopic() != null) {
            existingQuestion.setTopic(updatedQuestion.getTopic());
        }
        if (updatedQuestion.getQuizId() != null) {
            existingQuestion.setQuizId(updatedQuestion.getQuizId());
        }

        return questionRepository.save(existingQuestion);
    }


    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}

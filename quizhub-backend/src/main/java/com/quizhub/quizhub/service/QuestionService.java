//package com.quizhub.quizhub.service;
//
//import com.quizhub.quizhub.model.Question;
//import com.quizhub.quizhub.model.User;
//import com.quizhub.quizhub.repository.QuestionRepository;
//import com.quizhub.quizhub.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class QuestionService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private QuestionRepository questionRepository;
//
//
//    public Question addQuestion(Question question) {
//        return questionRepository.save(question);
//    }
//
//
//    public List<Question> getAllQuestions() {
//        return questionRepository.findAll();
//    }
//
//
//    public List<String> getAllTopics() {
//        return questionRepository.findAll().stream().map(Question::getTopic).distinct().toList();
//    }
//
//    public Optional<Question> findById(Long questionId) {
//        return questionRepository.findById(questionId); // Assuming you're using JPA repository
//    }
//
//    // Save the updated question
//    public Question save(Question question) {
//        return questionRepository.save(question);
//    }
//
//    //get
//   //the questions accessed by the users who created
//    public List<Question> getQuestionsForUser(String username) {
//        try {
//            // Fetch the user based on the username
//            User user = userRepository.findByUsername(username)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Fetch questions associated with the user's ID
//            return questionRepository.findByUserId(user.getId());
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching questions for user: " + username, e);
//        }
//    }
//
//
//
////    public Question updateQuestion(Long id, Question updatedQuestion) {
////        Question existingQuestion = getQuestionById(id);
////
////        if (updatedQuestion.getQuestionText() != null) {
////            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
////        }
////        if (updatedQuestion.getQuestionType() != null) {
////            existingQuestion.setQuestionType(updatedQuestion.getQuestionType());
////        }
////        if (updatedQuestion.getOptionA() != null) {
////            existingQuestion.setOptionA(updatedQuestion.getOptionA());
////        }
////        if (updatedQuestion.getOptionB() != null) {
////            existingQuestion.setOptionB(updatedQuestion.getOptionB());
////        }
////        if (updatedQuestion.getOptionC() != null) {
////            existingQuestion.setOptionC(updatedQuestion.getOptionC());
////        }
////        if (updatedQuestion.getOptionD() != null) {
////            existingQuestion.setOptionD(updatedQuestion.getOptionD());
////        }
////        if (updatedQuestion.getCorrectOption() != null) {
////            existingQuestion.setCorrectOption(updatedQuestion.getCorrectOption());
////        }
////        if (updatedQuestion.getHint() != null) {
////            existingQuestion.setHint(updatedQuestion.getHint());
////        }
////        if (updatedQuestion.getDifficulty() != null) {
////            existingQuestion.setDifficulty(updatedQuestion.getDifficulty());
////        }
////        if (updatedQuestion.getTopic() != null) {
////            existingQuestion.setTopic(updatedQuestion.getTopic());
////        }
////        if (updatedQuestion.getQuizId() != null) {
////            existingQuestion.setQuizId(updatedQuestion.getQuizId());
////        }
////
////        return questionRepository.save(existingQuestion);
////    }
//
//
//    public void deleteQuestion(Long id) {
//        questionRepository.deleteById(id);
//    }
//
//    public List<Question> getQuestionsByUserId(Long userId) {
//        return questionRepository.findByUserId(userId);
//    }
//
//    public Question addQuestionForUser(Long userId, Question question) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//
//        question.setUser(user); // Set the user for the question
//        return questionRepository.save(question);
//    }
//
//
//    public Question getQuestionById(Long id) {
//        return questionRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Question not found with ID: " + id));
//    }
//
//}

package com.quizhub.quizhub.service;

import com.quizhub.quizhub.model.Question;
import com.quizhub.quizhub.model.Quiz;
import com.quizhub.quizhub.model.Topic;
import com.quizhub.quizhub.model.User;
import com.quizhub.quizhub.repository.QuestionRepository;
import com.quizhub.quizhub.repository.QuizRepository;
import com.quizhub.quizhub.repository.TopicRepository;
import com.quizhub.quizhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public Question createQuestion(Question question, Long topicId, Long userId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        User educator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Educator not found"));

        question.setTopic(topic);
        question.setCreatedBy(educator);

        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question updateQuestion(Long id, Question updatedData) {
        return questionRepository.findById(id)
                .map(question -> {
                    question.setQuestionText(updatedData.getQuestionText());
                    question.setOptions(updatedData.getOptions());
                    question.setCorrectOptions(updatedData.getCorrectOptions());
                    question.setHint(updatedData.getHint());
                    return questionRepository.save(question);
                })
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public List<Question> getQuestionsForTopic(Long topicId, int numOfQuestions) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        return questionRepository.findByTopic(topic, PageRequest.of(0, numOfQuestions));
    }

//    @Override
//    public Question assignQuestionToQuiz(Long questionId, Long quizId) {
//        Question question = questionRepository.findById(questionId)
//                .orElseThrow(() -> new RuntimeException("Question not found"));
//
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        quiz.getQuestions().add(question);
//        quizRepository.save(quiz);
//
//        return question;
//    }

    //the questions accessed by the users who created
    @Override
    public List<Question> getQuestionsForUser(String username) {
        try {
            // Fetch the user based on the username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch questions associated with the user's ID
            return questionRepository.findByCreatedBy_Id(user.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching questions for user: " + username, e);
        }
    }

}

package com.quizhub.quizhub.controller;

import com.quizhub.quizhub.model.Question;
import com.quizhub.quizhub.model.User;
import com.quizhub.quizhub.service.QuestionService;
import com.quizhub.quizhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    //adding question
    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        try {
            // Get the authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            // Retrieve the user based on the username
            User user = userService.findByUserName(userName);

            // Set the user to the question (use user object directly)
            question.setUser(user);  // user ID will be fetched from user object

            // Add the question
            Question savedQuestion = questionService.addQuestion(question);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding question: " + e.getMessage());
        }
    }



//    @GetMapping("/all")
//    public ResponseEntity<?> getAllQuestions() {
//        try {
//            List<Question> questions = questionService.getAllQuestions();
//            return ResponseEntity.ok(questions);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error fetching questions: " + e.getMessage());
//        }
//    }


    //  Getting the particular user questions
    @GetMapping
    public ResponseEntity<?> getAllQuestionsOfUser() {
        try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
            List<Question> questions = questionService.getQuestionsForUser(userName);
            if (questions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No questions found for user ID: " + userName);
            }
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching questions: " + e.getMessage());
        }
    }




//    Making entry in the table for the particular user questions

    @PutMapping("/{questionId}")
    public ResponseEntity<?> editQuestion(@PathVariable Long questionId, @RequestBody Question updatedQuestion) {
        try {
            // Get the authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            // Retrieve the user based on the username
            User user = userService.findByUsername(userName);

            // Retrieve the question by its ID
            Question existingQuestion = questionService.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            // Ensure that the authenticated user is the owner of the question
            if (!existingQuestion.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to edit this question.");
            }

            // Update the question with the new values
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

            // Save the updated question
            Question savedQuestion = questionService.save(existingQuestion);

            return ResponseEntity.status(HttpStatus.OK).body(savedQuestion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating question: " + e.getMessage());
        }
    }


    @GetMapping("/topics")
    public ResponseEntity<?> getAllTopics() {
        try {
            List<String> topics = questionService.getAllTopics();
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching topics: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching question: " + e.getMessage());
        }
    }

//    @PutMapping("/{id}/update")
//    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
//        try {
//            Question updatedQuestion = questionService.updateQuestion(id, question);
//            return ResponseEntity.ok(updatedQuestion);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error updating question: " + e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok("Question deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting question: " + e.getMessage());
        }
    }
}

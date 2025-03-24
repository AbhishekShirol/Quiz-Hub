//package com.quizhub.quizhub.controller;
//
//import com.quizhub.quizhub.model.Quiz;
//import com.quizhub.quizhub.service.QuizService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/quizzes")
//public class QuizController {
//    private final QuizService quizService;
//
//    public QuizController(QuizService quizService) {
//        this.quizService = quizService;
//    }
//
//    // Create a quiz
//    @PostMapping("/create")
//    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
//        return ResponseEntity.ok(quizService.createQuiz(quiz));
//    }
//
//    // Get all quizzes for the logged-in user
//    @GetMapping("get-quizzes")
//    public ResponseEntity<List<Quiz>> getAllQuizzes() {
//        return ResponseEntity.ok(quizService.getAllQuizzesForUser());
//    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<Object> getQuizById(@PathVariable Long id) {
////        Optional<Quiz> quiz = quizService.getQuizById(id);
////        if (quiz.isPresent()) {
////            return ResponseEntity.ok(quiz.get());
////        } else {
////            return ResponseEntity.status(403).body("Unauthorized or quiz not found");
////        }
////    }
//
//
//    // Update a quiz (only if created by the logged-in user)
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
//        Optional<Quiz> updated = quizService.updateQuiz(id, updatedQuiz);
//        if (updated.isPresent()) {
//            return ResponseEntity.ok(updated.get());
//        } else {
//            return ResponseEntity.status(403).body("Unauthorized or quiz not found");
//        }
//    }
//
//
//    // Delete a quiz (only if created by the logged-in user)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> deleteQuiz(@PathVariable Long id) {
//        boolean deleted = quizService.deleteQuiz(id);
//        if (deleted) {
//            return ResponseEntity.ok("Quiz deleted successfully");
//        } else {
//            return ResponseEntity.status(403).body("Unauthorized or quiz not found");
//        }
//    }
//
//}
//


package com.quizhub.quizhub.controller;

import com.quizhub.quizhub.model.Quiz;
import com.quizhub.quizhub.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Create a quiz
    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    // Get all quizzes for the logged-in user
    @GetMapping("/get-quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzesForUser());
    }

    // Get a quiz by ID (only if created by the logged-in user)
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getQuizById(@PathVariable Long id) {
//        Optional<Quiz> quiz = quizService.getQuizById(id);
//        return quiz.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(403).body("Unauthorized or quiz not found"));
//    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getQuizById(@PathVariable Long id) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            return ResponseEntity.ok(quiz.get());
        } else {
            return ResponseEntity.status(403).body("Unauthorized or quiz not found");
        }
    }

    // Update a quiz (only if created by the logged-in user)
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
        Optional<Quiz> updated = quizService.updateQuiz(id, updatedQuiz);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.status(403).body("Unauthorized or quiz not found");
        }
    }

    // Delete a quiz (only if created by the logged-in user)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable Long id) {
        boolean deleted = quizService.deleteQuiz(id);
        if (deleted) {
            return ResponseEntity.ok("Quiz deleted successfully");
        } else {
            return ResponseEntity.status(403).body("Unauthorized or quiz not found");
        }
    }
}

package com.quizhub.quizhub.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import com.quizhub.quizhub.model.QuestionType;

import java.util.List;

//
//@Entity
//@Table(name = "questions")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Question {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String questionText;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private QuestionType questionType; // MCQ or TRUE_FALSE
//
//    private String optionA;
//    private String optionB;
//    private String optionC;
//    private String optionD;
//
//    @Column(nullable = false)
//    private String correctOption; // A, B, C, D, T, or F
//
//    @Column(columnDefinition = "TEXT")
//    private String hint;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private DifficultyLevel difficulty;
//
//    @Column(nullable = false)
//    private String topic;
//
//    private Long quizId; // Nullable if not part of a quiz
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//}


@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType; // MCQ, TRUE_FALSE

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options; // Dynamic multiple choices

    @ElementCollection
    @CollectionTable(name = "correct_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "correct_option")
    private List<String> correctOptions; // Multiple correct answers

    @Column(columnDefinition = "TEXT")
    private String hint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzes; // Many-to-Many relationship with quizzes

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // Tracks which educator added the question
}
package com.quizhub.quizhub.model;

import jakarta.persistence.*;
import lombok.*;
import com.quizhub.quizhub.model.QuestionType;


@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType; // MCQ or TRUE_FALSE

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    @Column(nullable = false)
    private String correctOption; // A, B, C, D, T, or F

    @Column(columnDefinition = "TEXT")
    private String hint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    @Column(nullable = false)
    private String topic;

    private Long quizId; // Nullable if not part of a quiz

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

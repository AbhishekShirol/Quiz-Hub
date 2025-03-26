package com.quizhub.quizhub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    private Long quizId;  // or use @ManyToOne relationship if you prefer
    private Long studentId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer score;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<QuestionResponse> responses;

    // Methods for starting, submitting, and calculating score can be in a service layer
}


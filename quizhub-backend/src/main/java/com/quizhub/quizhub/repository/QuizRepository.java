package com.quizhub.quizhub.repository;

import com.quizhub.quizhub.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}

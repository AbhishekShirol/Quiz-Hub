package com.quizhub.quizhub.repository;

import com.quizhub.quizhub.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCreatedBy(Long createdBy);
    Optional<Quiz> findByIdAndCreatedBy(Long id, Long createdBy);

}

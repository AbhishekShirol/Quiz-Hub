package com.quizhub.quizhub.dto;

import com.quizhub.quizhub.model.Quiz;
import com.quizhub.quizhub.model.QuizVisibility;

public class QuizDTOFactory {

    public static BaseQuizDTO createQuizDTO(Quiz quiz) {
        BaseQuizDTO base = new BaseQuizDTO(quiz);

        if (quiz.getVisibility() == QuizVisibility.PRIVATE) {
            return new PrivateQuizDTO(quiz); // or wrap with decorator
        } else if (quiz.getFilterCriteria() != null) {
            return new FilteredQuizDTO(quiz); // or wrap with decorator
        } else {
            return new PublicQuizDTO(quiz); // or base
        }
    }
}

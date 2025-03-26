//package com.quizhub.quizhub.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.quizhub.quizhub.dto.FilteredQuizRequest;
//import com.quizhub.quizhub.model.Quiz;
//import com.quizhub.quizhub.model.QuizVisibility;
//import com.quizhub.quizhub.model.Question;
//import com.quizhub.quizhub.model.Topic;
//import com.quizhub.quizhub.model.User;
//import com.quizhub.quizhub.repository.QuizRepository;
//import com.quizhub.quizhub.repository.QuestionRepository;
//import com.quizhub.quizhub.repository.TopicRepository;
//import com.quizhub.quizhub.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class FilteredQuizService {
//
//    private final QuizRepository quizRepository;
//    private final QuestionRepository questionRepository;
//    private final TopicRepository topicRepository;
//    private final UserRepository userRepository;
//    private final ObjectMapper objectMapper;
//
////    /**
////     * Creates a filtered quiz based on the provided filter criteria and user ID.
////     *
////     * Process:
////     * 1. Retrieve the user.
////     * 2. Retrieve all topics corresponding to the provided topic IDs.
////     * 3. Fetch questions that match the selected topics (ignoring difficulty for now).
////     * 4. Shuffle the list to randomize question order.
////     * 5. Extract the required number of question IDs.
////     * 6. Convert the list of question IDs and filter criteria to JSON strings.
////     * 7. Create and save the Quiz entity with appropriate attributes.
////     *
////     * @param request the filtered quiz criteria.
////     * @param userId the ID of the user generating the quiz.
////     * @return the created Quiz entity.
////     * @throws Exception if any retrieval or conversion errors occur.
////     */
////    public Quiz createFilteredQuiz(FilteredQuizRequest request, Long userId) throws Exception {
////        // 1. Retrieve the user generating the quiz.
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        // 2. Retrieve all topics for the provided topic IDs.
////        List<Topic> topics = topicRepository.findAllById(request.getTopicIds());
////        if (topics.isEmpty()) {
////            throw new RuntimeException("No topics found for the provided IDs");
////        }
////
////        // 3. Fetch questions that match any of the selected topics.
////        List<Question> questions = questionRepository.findByTopic_IdIn(
////                request.getTopicIds(),
////                PageRequest.of(0, request.getNumberOfQuestions() * 2)  // Fetch extra for randomness if needed.
////        );
////        if (questions.isEmpty()) {
////            throw new RuntimeException("No questions available for the selected topics");
////        }
////
////        // 4. Randomize the question order.
////        Collections.shuffle(questions);
////
////        // 5. Extract the required number of question IDs (if available).
////        List<Long> questionIds = questions.stream()
////                .limit(request.getNumberOfQuestions())
////                .map(Question::getId)
////                .collect(Collectors.toList());
////        if (questionIds.size() < request.getNumberOfQuestions()) {
////            throw new RuntimeException("Not enough questions available to meet the requested count");
////        }
////
////        // 6. Convert the list of question IDs to a JSON string.
////        String questionIdsJson = objectMapper.writeValueAsString(questionIds);
////
////        // 7. Convert the entire filter criteria to JSON.
////        String filterCriteriaJson = objectMapper.writeValueAsString(request);
////
////        // 8. Create a comma-separated string of topic names.
////        String topicsString = topics.stream()
////                .map(Topic::getName)
////                .collect(Collectors.joining(","));
////
////        // 9. Build the Quiz entity.
////        Quiz quiz = Quiz.builder()
////                .generatedBy(user)
////                .numberOfQuestions(request.getNumberOfQuestions())
////                .difficulty(request.getDifficulty())
////                .topics(topicsString)
////                .timeLimit(request.getTimeLimit() != null ? request.getTimeLimit() : 0)
////                .questionIds(questionIdsJson)
////                .visibility(QuizVisibility.FILTERED)
////                .filterCriteria(filterCriteriaJson)
////                .build();
////
////        // 10. Save and return the quiz.
////        return quizRepository.save(quiz);
////    }
////
////    /**
////     * Demonstrates how to update the questionIds field of an existing quiz.
////     * This method converts the JSON string to a list, adds or removes a question ID,
////     * then converts the list back to a JSON string and updates the quiz.
////     *
////     * @param quizId the ID of the quiz to update.
////     * @param questionId the question ID to add or remove.
////     * @param add true to add the question, false to remove.
////     * @return the updated Quiz entity.
////     * @throws Exception if any conversion or retrieval errors occur.
////     */
////    public Quiz updateQuizQuestionIds(Long quizId, Long questionId, boolean add) throws Exception {
////        Quiz quiz = quizRepository.findById(quizId)
////                .orElseThrow(() -> new RuntimeException("Quiz not found"));
////
////        // Convert stored JSON string to a List<Long>
////        List<Long> questionIds = objectMapper.readValue(
////                quiz.getQuestionIds(),
////                objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class)
////        );
////
////        if (add) {
////            if (!questionIds.contains(questionId)) {
////                questionIds.add(questionId);
////            }
////        } else {
////            questionIds.remove(questionId);
////        }
////        // Convert back to JSON string and update the quiz.
////        quiz.setQuestionIds(objectMapper.writeValueAsString(questionIds));
////        return quizRepository.save(quiz);
////    }
//
//    /**
//     * Creates a filtered quiz based on the provided filter criteria and user ID.
//     *
//     * @param request the filtered quiz criteria.
//     * @param userId the ID of the user generating the quiz.
//     * @return the created Quiz entity.
//     * @throws Exception if any retrieval or conversion errors occur.
//     */
//    public Quiz createFilteredQuiz(FilteredQuizRequest request, Long userId) throws Exception {
//        // 1. Retrieve the user generating the quiz.
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2. Retrieve all topics for the provided topic IDs.
//        List<Topic> topics = topicRepository.findAllById(request.getTopicIds());
//        if (topics.isEmpty()) {
//            throw new RuntimeException("No topics found for the provided IDs");
//        }
//
//        // 3. Fetch questions that match any of the selected topics.
//        List<Question> questions = questionRepository.findByTopic_IdIn(
//                request.getTopicIds(),
//                PageRequest.of(0, request.getNumberOfQuestions() * 2)
//        );
//        if (questions.isEmpty()) {
//            throw new RuntimeException("No questions available for the selected topics");
//        }
//
//        // 4. Randomize the question order.
//        Collections.shuffle(questions);
//
//        // 5. Extract the required number of question IDs.
//        List<Long> questionIds = questions.stream()
//                .limit(request.getNumberOfQuestions())
//                .map(Question::getId)
//                .collect(Collectors.toList());
//        if (questionIds.size() < request.getNumberOfQuestions()) {
//            throw new RuntimeException("Not enough questions available to meet the requested count");
//        }
//
//        // 6. Convert the list of question IDs to a JSON string.
//        String questionIdsJson = objectMapper.writeValueAsString(questionIds);
//
//        // 7. Convert the entire filter criteria to JSON.
//        String filterCriteriaJson = objectMapper.writeValueAsString(request);
//
//        // 8. Create a comma-separated string of topic names.
//        String topicsString = topics.stream()
//                .map(Topic::getName)
//                .collect(Collectors.joining(","));
//
//        // 9. Build the Quiz entity including title and description if provided.
//        Quiz quiz = Quiz.builder()
//                .generatedBy(user)
//                .title(request.getTitle())         // Optional title provided by user
//                .description(request.getDescription()) // Optional description provided by user
//                .numberOfQuestions(request.getNumberOfQuestions())
//                .difficulty(request.getDifficulty())
//                .topics(topicsString)
//                .timeLimit(request.getTimeLimit() != null ? request.getTimeLimit() : 0)
//                .questionIds(questionIdsJson)
//                .visibility(QuizVisibility.FILTERED)
//                .filterCriteria(filterCriteriaJson)
//                .build();
//
//        // 10. Save and return the quiz.
//        return quizRepository.save(quiz);
//    }
//}



package com.quizhub.quizhub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizhub.quizhub.dto.FilteredQuizRequest;
import com.quizhub.quizhub.model.Quiz;
import com.quizhub.quizhub.model.QuizVisibility;
import com.quizhub.quizhub.model.Question;
import com.quizhub.quizhub.model.Topic;
import com.quizhub.quizhub.model.User;
import com.quizhub.quizhub.repository.QuizRepository;
import com.quizhub.quizhub.repository.QuestionRepository;
import com.quizhub.quizhub.repository.TopicRepository;
import com.quizhub.quizhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilteredQuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    /**
     * Creates a filtered quiz based on the provided filter criteria and user ID.
     * If fewer questions are available than requested (but at least one exists), the quiz is created with those available questions.
     * If no questions match the filters, an error is thrown.
     *
     * @param request the filtered quiz criteria.
     * @param userId  the ID of the user generating the quiz.
     * @return the created Quiz entity.
     * @throws Exception if any retrieval or conversion errors occur.
     */
    public Quiz createFilteredQuiz(FilteredQuizRequest request, Long userId) throws Exception {
        // 1. Retrieve the user generating the quiz.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Retrieve all topics for the provided topic IDs.
        List<Topic> topics = topicRepository.findAllById(request.getTopicIds());
        if (topics.isEmpty()) {
            throw new RuntimeException("No topics found for the provided IDs.");
        }

        // 3. Fetch questions that match the selected topics.
        List<Question> allQuestions = questionRepository.findByTopic_IdIn(
                request.getTopicIds(),
                PageRequest.of(0, request.getNumberOfQuestions() * 2) // Fetch extra for randomness.
        );

        if (allQuestions.isEmpty()) {
            throw new RuntimeException("No questions available for the selected topics.");
        }

        // 4. Filter questions based on difficulty.
        String requestedDifficulty = request.getDifficulty().toUpperCase(); // Convert to uppercase for enum comparison
        List<Question> filteredQuestions = allQuestions.stream()
                .filter(q -> q.getDifficulty() != null && q.getDifficulty().name().equalsIgnoreCase(requestedDifficulty))
                .collect(Collectors.toList());

        // 5. If no questions match, return an error (DO NOT CREATE A QUIZ).
        if (filteredQuestions.isEmpty()) {
            throw new RuntimeException("No questions available for difficulty: " + request.getDifficulty());
        }

        // 6. Randomize the filtered questions.
        Collections.shuffle(filteredQuestions);

        // 7. Determine the actual number of questions to use.
        int actualNumber = Math.min(request.getNumberOfQuestions(), filteredQuestions.size());
        List<Long> questionIds = filteredQuestions.stream()
                .limit(actualNumber)
                .map(Question::getId)
                .collect(Collectors.toList());

        if (questionIds.isEmpty()) {
            throw new RuntimeException("Error: No valid questions available after filtering.");
        }

        // 8. Convert question IDs and filter criteria to JSON.
        String questionIdsJson = objectMapper.writeValueAsString(questionIds);
        String filterCriteriaJson = objectMapper.writeValueAsString(request);

        // 9. Convert topic names to a comma-separated string.
        String topicsString = topics.stream()
                .map(Topic::getName)
                .collect(Collectors.joining(", "));

        // 10. Build and save the Quiz entity.
        Quiz quiz = Quiz.builder()
                .generatedBy(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .numberOfQuestions(actualNumber)
                .difficulty(request.getDifficulty())
                .topics(topicsString)
                .timeLimit(request.getTimeLimit() != null ? request.getTimeLimit() : 0)
                .questionIds(questionIdsJson)
                .visibility(QuizVisibility.FILTERED)
                .filterCriteria(filterCriteriaJson)
                .build();

        return quizRepository.save(quiz);
    }



    /**
     * Demonstrates how to update the questionIds field of an existing quiz.
     *
     * @param quizId     the ID of the quiz to update.
     * @param questionId the question ID to add or remove.
     * @param add        true to add the question, false to remove.
     * @return the updated Quiz entity.
     * @throws Exception if any conversion or retrieval errors occur.
     */
    public Quiz updateQuizQuestionIds(Long quizId, Long questionId, boolean add) throws Exception {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Convert stored JSON string to a List<Long>
        List<Long> questionIds = objectMapper.readValue(
                quiz.getQuestionIds(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class)
        );

        if (add) {
            if (!questionIds.contains(questionId)) {
                questionIds.add(questionId);
            }
        } else {
            questionIds.remove(questionId);
        }
        // Convert back to JSON string and update the quiz.
        quiz.setQuestionIds(objectMapper.writeValueAsString(questionIds));
        return quizRepository.save(quiz);
    }
}

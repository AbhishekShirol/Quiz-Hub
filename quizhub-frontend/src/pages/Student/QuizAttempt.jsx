// import React, { useEffect, useState } from 'react';
// import { useParams, useNavigate } from 'react-router-dom';
// import axios from 'axios';

// function QuizAttempt() {
//   const { quizId } = useParams();
//   const navigate = useNavigate();
//   const [quiz, setQuiz] = useState(null);
//   const [attemptId, setAttemptId] = useState(null);
//   const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
//   const [answers, setAnswers] = useState({});
//   const [timeLeft, setTimeLeft] = useState(null);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   // Fetch quiz details, questions, and start a new attempt on mount
//   useEffect(() => {
//     const fetchQuizAndStartAttempt = async () => {
//       try {
//         // 1. Fetch quiz details (QuizDTO)
//         const quizResponse = await axios.get(`http://localhost:8080/quizzes/details/${quizId}`);
//         let quizData = quizResponse.data;
//         // 2. If quizData doesn't include a questions array, fetch questions separately using questionIds
//         if (!quizData.questions && quizData.questionIds) {
//           // Assuming questionIds is stored as a JSON string, e.g., "[4,3]"
//           const questionIds = JSON.parse(quizData.questionIds);
//           const questionsResponse = await axios.get(`http://localhost:8080/questions/batch`, {
//             params: { ids: questionIds }
//           });
//           quizData.questions = questionsResponse.data;
//         }
//         if (!quizData.questions || quizData.questions.length === 0) {
//           throw new Error("No questions available for this quiz.");
//         }
//         setQuiz(quizData);
//         setTimeLeft(quizData.timeLimit);

//         // 3. Start a new quiz attempt using the quizId and logged-in user (from localStorage)
//         const userId = localStorage.getItem('userId');
//         const attemptResponse = await axios.post(`http://localhost:8080/attempts/start`, null, {
//           params: { quizId, studentId: userId },
//         });
//         setAttemptId(attemptResponse.data.attemptId);
//         setLoading(false);
//       } catch (err) {
//         console.error("Error fetching quiz or starting attempt:", err);
//         setError("Error loading quiz. Please try again later.");
//         setLoading(false);
//       }
//     };

//     fetchQuizAndStartAttempt();
//   }, [quizId]);

//   // Timer effect: count down seconds; auto-submit when time reaches zero
//   useEffect(() => {
//     if (timeLeft === null) return;
//     if (timeLeft <= 0) {
//       handleSubmit();
//       return;
//     }
//     const timer = setInterval(() => {
//       setTimeLeft(prevTime => prevTime - 1);
//     }, 1000);
//     return () => clearInterval(timer);
//   }, [timeLeft]);

//   const handleAnswerChange = (questionId, answer) => {
//     setAnswers(prev => ({ ...prev, [questionId]: answer }));
//   };

//   const handleNextQuestion = () => {
//     if (currentQuestionIndex < quiz.questions.length - 1) {
//       setCurrentQuestionIndex(prev => prev + 1);
//     }
//   };

//   const handlePrevQuestion = () => {
//     if (currentQuestionIndex > 0) {
//       setCurrentQuestionIndex(prev => prev - 1);
//     }
//   };

//   const handleSubmit = async () => {
//     try {
//       const responses = Object.entries(answers).map(([questionId, answer]) => ({
//         questionId: parseInt(questionId, 10),
//         studentResponse: answer,
//         hintUsed: false, // Update this if you support hint functionality
//       }));
//       await axios.post(`http://localhost:8080/attempts/submit`, responses, {
//         params: { attemptId },
//       });
//       navigate(`/quiz-history/${quizId}`);
//     } catch (err) {
//       console.error("Error submitting quiz:", err);
//       setError("Error submitting quiz. Please try again.");
//     }
//   };

//   if (loading) {
//     return (
//       <div className="bg-slate-900 text-slate-200 min-h-screen flex items-center justify-center">
//         <p className="text-xl">Loading quiz...</p>
//       </div>
//     );
//   }

//   if (error) {
//     return (
//       <div className="bg-slate-900 text-slate-200 min-h-screen flex items-center justify-center">
//         <p className="text-xl text-red-500">{error}</p>
//       </div>
//     );
//   }

//   // Ensure we have questions loaded
//   if (!quiz || !quiz.questions || quiz.questions.length === 0) {
//     return (
//       <div className="bg-slate-900 text-slate-200 min-h-screen flex items-center justify-center">
//         <p className="text-xl">No questions available for this quiz.</p>
//       </div>
//     );
//   }

//   const currentQuestion = quiz.questions[currentQuestionIndex];

//   return (
//     <div className="bg-slate-900 text-slate-200 min-h-screen p-6">
//       <div className="container mx-auto">
//         <h1 className="text-3xl font-bold mb-4">{quiz.title}</h1>
//         <div className="mb-4">
//           <span className="text-lg">
//             Time Left: {Math.floor(timeLeft / 60)}:
//             {timeLeft % 60 < 10 ? '0' : ''}{timeLeft % 60}
//           </span>
//         </div>
//         <div className="bg-slate-800 p-6 rounded shadow mb-4">
//           <h2 className="text-2xl font-semibold mb-2">
//             Question {currentQuestionIndex + 1}: {currentQuestion.content}
//           </h2>
//           <div>
//             {currentQuestion.options.map((option, idx) => (
//               <div key={idx} className="mb-2">
//                 <label className="flex items-center">
//                   <input
//                     type="radio"
//                     name={`question-${currentQuestion.id}`}
//                     value={option}
//                     checked={answers[currentQuestion.id] === option}
//                     onChange={() => handleAnswerChange(currentQuestion.id, option)}
//                     className="mr-2"
//                   />
//                   {option}
//                 </label>
//               </div>
//             ))}
//           </div>
//         </div>
//         <div className="flex justify-between mb-4">
//           <button
//             onClick={handlePrevQuestion}
//             disabled={currentQuestionIndex === 0}
//             className="bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded"
//           >
//             Previous
//           </button>
//           {currentQuestionIndex < quiz.questions.length - 1 ? (
//             <button
//               onClick={handleNextQuestion}
//               className="bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded"
//             >
//               Next
//             </button>
//           ) : (
    //             <button
    //               onClick={handleSubmit}
    //               className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded"
    //             >
    //               Submit
    //             </button>
    //           )}
    //         </div>
    //       </div>
    //     </div>
    //   );
    // }
    
    // export default QuizAttempt;
    
    
    
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import qs from 'qs';
    
function QuizAttempt() {
  const { quizId } = useParams();
  const navigate = useNavigate();

  // State variables
  const [quiz, setQuiz] = useState(null);
  const [attemptId, setAttemptId] = useState(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState({});
  const [timeLeft, setTimeLeft] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch quiz details and questions, then start a new attempt on mount
  useEffect(() => {
    const fetchQuizAndStartAttempt = async () => {
      try {
        // 1. Fetch quiz details from the QuizDTO endpoint
        const quizResponse = await axios.get(`http://localhost:8080/quizzes/details/${quizId}`);
        let quizData = quizResponse.data;

        // 2. If questions are not included, fetch them using the stored questionIds
        if (!quizData.questions && quizData.questionIds) {
          let questionIds = [];
          try {
            questionIds = JSON.parse(quizData.questionIds); // Expecting a JSON string like "[4,3]"
          } catch (parseError) {
            throw new Error("Error parsing question IDs.");
          }

        // Inside your fetchQuizAndStartAttempt function:
        if (questionIds.length > 0) {
        const questionsResponse = await axios.get(`http://localhost:8080/questions/batch`, {
            params: { ids: questionIds },
            paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
        });
        quizData.questions = questionsResponse.data;
        }

        }

        // 3. Ensure questions exist
        if (!quizData.questions || quizData.questions.length === 0) {
          throw new Error("No questions available for this quiz.");
        }

        setQuiz(quizData);
        // Initialize the timer (assumes timeLimit is in seconds)
        setTimeLeft(quizData.timeLimit);

        // 4. Start a new quiz attempt using the quizId and logged-in user from localStorage
        const userId = localStorage.getItem('userId');
        if (!userId) {
          throw new Error("User not logged in.");
        }
        const attemptResponse = await axios.post(`http://localhost:8080/attempts/start`, null, {
          params: { quizId, studentId: userId }
        });
        setAttemptId(attemptResponse.data.attemptId);
        setLoading(false);
      } catch (err) {
        console.error("Error fetching quiz or starting attempt:", err);
        setError(err.message || "Error loading quiz. Please try again later.");
        setLoading(false);
      }
    };

    fetchQuizAndStartAttempt();
  }, [quizId]);

  // Timer effect: count down every second; auto-submit when time reaches zero
  useEffect(() => {
    if (timeLeft === null) return;
    if (timeLeft <= 0) {
      handleSubmit();
      return;
    }
    const timer = setInterval(() => {
      setTimeLeft(prevTime => prevTime - 1);
    }, 1000);
    return () => clearInterval(timer);
  }, [timeLeft]);

  // Handle answer selection per question
  const handleAnswerChange = (questionId, answer) => {
    setAnswers(prev => ({ ...prev, [questionId]: answer }));
  };

  // Navigation: Next and Previous question
  const handleNextQuestion = () => {
    if (currentQuestionIndex < quiz.questions.length - 1) {
      setCurrentQuestionIndex(prev => prev + 1);
    }
  };

  const handlePrevQuestion = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(prev => prev - 1);
    }
  };

  // Submit the quiz attempt with the collected answers
  const handleSubmit = async () => {
    try {
      const responses = Object.entries(answers).map(([questionId, answer]) => ({
        questionId: parseInt(questionId, 10),
        studentResponse: answer,
        hintUsed: false // Extend this logic if hint functionality is added
      }));
      await axios.post(`http://localhost:8080/attempts/submit`, responses, {
        params: { attemptId }
      });
      navigate(`/quiz-history/${quizId}`);
    } catch (err) {
      console.error("Error submitting quiz:", err);
      setError("Error submitting quiz. Please try again.");
    }
  };

  // Render loading state
  if (loading) {
    return (
      <div className="bg-slate-900 text-slate-200 min-h-screen flex items-center justify-center">
        <p className="text-xl">Loading quiz...</p>
      </div>
    );
  }

  // Render error state
  if (error) {
    return (
      <div className="bg-slate-900 text-slate-200 min-h-screen flex items-center justify-center">
        <p className="text-xl text-red-500">{error}</p>
      </div>
    );
  }

  // Ensure questions are available before rendering the quiz attempt UI
  if (!quiz || !quiz.questions || quiz.questions.length === 0) {
    return (
      <div className="bg-slate-900 text-slate-200 min-h-screen flex items-center justify-center">
        <p className="text-xl">No questions available for this quiz.</p>
      </div>
    );
  }

  // Get the current question based on the current index
  const currentQuestion = quiz.questions[currentQuestionIndex];

  return (
    <div className="bg-slate-900 text-slate-200 min-h-screen p-6">
      <div className="container mx-auto">
        <h1 className="text-3xl font-bold mb-4">{quiz.title}</h1>
        <div className="mb-4">
          <span className="text-lg">
            Time Left: {Math.floor(timeLeft / 60)}:
            {timeLeft % 60 < 10 ? '0' : ''}{timeLeft % 60}
          </span>
        </div>

        <div className="bg-slate-800 p-6 rounded shadow mb-4">
          <h2 className="text-2xl font-semibold mb-2">
            Question {currentQuestionIndex + 1}: {currentQuestion.questionText || currentQuestion.content}
          </h2>
          <div>
            {currentQuestion.options && currentQuestion.options.length > 0 ? (
              currentQuestion.options.map((option, idx) => (
                <div key={idx} className="mb-2">
                  <label className="flex items-center">
                    <input
                      type="radio"
                      name={`question-${currentQuestion.id}`}
                      value={option}
                      checked={answers[currentQuestion.id] === option}
                      onChange={() => handleAnswerChange(currentQuestion.id, option)}
                      className="mr-2"
                    />
                    {option}
                  </label>
                </div>
              ))
            ) : (
              <p className="text-red-400">No options available for this question.</p>
            )}
          </div>
        </div>

        <div className="flex justify-between mb-4">
          <button
            onClick={handlePrevQuestion}
            disabled={currentQuestionIndex === 0}
            className="bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded"
          >
            Previous
          </button>
          {currentQuestionIndex < quiz.questions.length - 1 ? (
            <button
              onClick={handleNextQuestion}
              className="bg-indigo-600 hover:bg-indigo-700 px-4 py-2 rounded"
            >
              Next
            </button>
          ) : (
            <button
              onClick={handleSubmit}
              className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded"
            >
              Submit
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

export default QuizAttempt;


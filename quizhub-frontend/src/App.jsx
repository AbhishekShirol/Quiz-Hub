import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Registration from './pages/Registration';
import EducatorDashboard from './pages/Educator/EducatorDashboard';
import Question from './pages/Educator/Questions'
import AddQuestion from './pages/Educator/AddQuestion';
import StudentDashboard from './pages/Student/StudentDashboard';
import CreateQuiz from './pages/Educator/CreateQuiz'
import CreateFilteredQuiz from './pages/Student/CreateFilteredQuiz';
import FilteredQuiz from './pages/Student/FilteredQuiz';
import QuizAttempt from './pages/Student/QuizAttempt';

import AdminDashboard from './pages/Admin/AdminDashboard';
import ManageUsers from './pages/Admin/ManageUsers';
import QuizAttemptsList from './pages/Student/QuizAttemptsList';
import QuizAttemptView from './pages/Student/QuizAttemptView';



function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Registration />} />
      <Route path="/student-dashboard" element={<StudentDashboard/>} />

      {/* <Route path="/educator-dashboard" element={<EducatorDashboard/>}/> */}
      <Route path="/educator-dashboard" element={<EducatorDashboard/>}/>
      <Route path="/my-questions" element={<Question/>} />
      <Route path="/add-question" element={<AddQuestion/>} />
      <Route path="/create-quiz" element={<CreateQuiz/>} />

      <Route path='/create-filtered-quiz' element={<CreateFilteredQuiz/>} />
      <Route path='/filtered-quiz' element={<FilteredQuiz/>}/>

      <Route path="/take-quiz/:quizId" element={<QuizAttempt />} />
      <Route path="/my-quiz-attempts" element={<QuizAttemptsList/>} />
      <Route path="/quiz-attempt/view/:attemptId" element={<QuizAttemptView/>} />



      {/* Admin */}

      <Route path="/admin-dashboard" element={<AdminDashboard />} />
      <Route path="/manage-users" element={<ManageUsers />} />
      
    </Routes>
  );
}

export default App;

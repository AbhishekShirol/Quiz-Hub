import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Registration from './pages/Registration';
import Student from './pages/Student/Student';
// import EducatorDashboard from './pages/Educator/All';
import EducatorDashboard from './pages/Educator/EducatorDashboard';
import Question from './pages/Educator/Questions'


function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Registration />} />
      <Route path="/student-dashboard" element={<Student/>}/>
      {/* <Route path="/educator-dashboard" element={<EducatorDashboard/>}/> */}
      <Route path="/educator-dashboard" element={<EducatorDashboard/>}/>
      <Route path="/my-questions" element={<Question/>} />
    </Routes>
  );
}

export default App;

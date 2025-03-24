// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';

// function Question() {
//   const [questions, setQuestions] = useState([]);
//   const navigate = useNavigate();

//   // Fetch questions for the logged-in user
//   useEffect(() => {
//     axios.get('http://localhost:8080/questions/get-questions')
//       .then(response => setQuestions(response.data))
//       .catch(error => console.error('Error fetching questions:', error));
//   }, []);

//   const handleView = (id) => {
//     window.open(`/questions/view-question/${id}`, '_blank');
//   };

//   const handleUpdate = (id) => {
//     window.open(`/questions/update-question/${id}`, '_blank');
//   };

//   const handleDelete = (id) => {
//     axios.delete(`http://localhost:8080/questions/delete-question/${id}`)
//       .then(() => {
//         setQuestions(questions.filter(q => q.id !== id));
//       })
//       .catch(error => console.error('Error deleting question:', error));
//   };

//   const handleLogout = () => {
//     localStorage.removeItem('jwtToken');
//     localStorage.removeItem('username');
//     navigate('/login');
//   };

//   return (
//     <div className="bg-slate-900 text-slate-200 min-h-screen">
//       {/* Navbar */}
//       <header className="bg-slate-800 border-b border-slate-700">
//         <div className="container mx-auto px-5 max-w-7xl">
//           <nav className="flex flex-col md:flex-row justify-between items-center py-4 gap-4 md:gap-0">
//             {/* Logo */}
//             <a href="/dashboard" className="flex items-center gap-2 font-bold text-xl text-indigo-600">
//               <div className="bg-indigo-600 text-white w-8 h-8 rounded-md flex items-center justify-center font-bold">
//                 Q
//               </div>
//               <span>QuizHub</span>
//             </a>
//             {/* Navigation Links */}
//             <div className="flex gap-6 w-full md:w-auto justify-center flex-wrap">
//               <a href="/home" className="text-slate-400 font-medium text-lg hover:text-indigo-600 transition-colors">
//                 Home
//               </a>
//               <a href="/create-quiz" className="text-slate-400 font-medium text-lg hover:text-indigo-600 transition-colors">
//                 Create Quiz
//               </a>
//               <a href="/add-question" className="text-slate-400 font-medium text-lg hover:text-indigo-600 transition-colors">
//                 Add Question
//               </a>
//             </div>
//             {/* Logout & Educator Name */}
//             <div className="flex gap-4 items-center w-full md:w-auto justify-center">
//               <span className="text-slate-300 text-lg">
//                 Welcome, {localStorage.getItem('username') || 'Educator'}!
//               </span>
//               <button
//                 onClick={handleLogout}
//                 className="bg-indigo-600 px-4 py-2 rounded text-white hover:bg-indigo-700 transition-colors"
//               >
//                 Logout
//               </button>
//             </div>
//           </nav>
//         </div>
//       </header>

//       {/* Main Content Area */}
//       <main className="container mx-auto px-5 py-8">
//         <h2 className="text-3xl font-bold mb-4">Educator Dashboard</h2>
//         <div className="overflow-x-auto">
//           <table className="min-w-full bg-slate-800 border border-slate-700">
//             <thead>
//               <tr>
//                 <th className="px-4 py-2 border border-slate-700">ID</th>
//                 <th className="px-4 py-2 border border-slate-700">Question Text</th>
//                 <th className="px-4 py-2 border border-slate-700">Difficulty</th>
//                 <th className="px-4 py-2 border border-slate-700">Topic</th>
//                 <th className="px-4 py-2 border border-slate-700">Question Type</th>
//                 <th className="px-4 py-2 border border-slate-700">Correct Options</th>
//                 <th className="px-4 py-2 border border-slate-700">Hint</th>
//                 <th className="px-4 py-2 border border-slate-700">Actions</th>
//               </tr>
//             </thead>
//             <tbody>
//               {questions.map((q) => (
//                 <tr key={q.id} className="hover:bg-slate-700">
//                   <td className="px-4 py-2 border border-slate-700">{q.id}</td>
//                   <td className="px-4 py-2 border border-slate-700">{q.questionText}</td>
//                   <td className="px-4 py-2 border border-slate-700">{q.difficulty}</td>
//                   <td className="px-4 py-2 border border-slate-700">{q.topic?.name}</td>
//                   <td className="px-4 py-2 border border-slate-700">{q.questionType}</td>
//                   <td className="px-4 py-2 border border-slate-700">{q.correctOptions?.join(', ')}</td>
//                   <td className="px-4 py-2 border border-slate-700">{q.hint}</td>
//                   <td className="px-4 py-2 border border-slate-700 flex gap-2">
//                     <button 
//                       onClick={() => handleView(q.id)} 
//                       className="bg-blue-500 px-3 py-1 rounded hover:bg-blue-600 transition-colors"
//                     >
//                       View
//                     </button>
//                     <button 
//                       onClick={() => handleUpdate(q.id)} 
//                       className="bg-yellow-500 px-3 py-1 rounded hover:bg-yellow-600 transition-colors"
//                     >
//                       Update
//                     </button>
//                     <button 
//                       onClick={() => handleDelete(q.id)} 
//                       className="bg-red-500 px-3 py-1 rounded hover:bg-red-600 transition-colors"
//                     >
//                       Delete
//                     </button>
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>
//       </main>

//       <footer className="bg-slate-800 border-t border-slate-700 py-4 text-center text-slate-400 text-sm">
//         <p>© 2025 QuizHub. All rights reserved.</p>
//       </footer>
//     </div>
//   );
// }

// export default Question;



import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Question() {
  const [questions, setQuestions] = useState([]);
  const [selectedQuestion, setSelectedQuestion] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:8080/questions/get-questions')
      .then(response => setQuestions(response.data))
      .catch(error => console.error('Error fetching questions:', error));
  }, []);

  const handleView = (question) => {
    setSelectedQuestion({ ...question, mode: 'view' });
  };

  const handleUpdate = (question) => {
    setSelectedQuestion({ ...question, mode: 'update' });
  };

  const handleDelete = (id) => {
    if (window.confirm('Do you want to delete this question?')) {
      axios.delete(`http://localhost:8080/questions/delete-question/${id}`)
        .then(() => {
          setQuestions(questions.filter(q => q.id !== id));
        })
        .catch(error => console.error('Error deleting question:', error));
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    navigate('/login');
  };

  return (
    <div className="bg-slate-900 text-slate-200 min-h-screen">
      <header className="bg-slate-800 border-b border-slate-700">
        <div className="container mx-auto px-5 max-w-7xl">
          <nav className="flex flex-col md:flex-row justify-between items-center py-4 gap-4 md:gap-0">
            <a href="/dashboard" className="flex items-center gap-2 font-bold text-xl text-indigo-600">
              <div className="bg-indigo-600 text-white w-8 h-8 rounded-md flex items-center justify-center font-bold">Q</div>
              <span>QuizHub</span>
            </a>
            <div className="flex gap-6 w-full md:w-auto justify-center flex-wrap">
              <a href="/home" className="text-slate-400 font-medium text-lg hover:text-indigo-600">Home</a>
              <a href="/create-quiz" className="text-slate-400 font-medium text-lg hover:text-indigo-600">Create Quiz</a>
              <a href="/add-question" className="text-slate-400 font-medium text-lg hover:text-indigo-600">Add Question</a>
            </div>
            <div className="flex gap-4 items-center w-full md:w-auto justify-center">
              <span className="text-slate-300 text-lg">Welcome, {localStorage.getItem('username') || 'Educator'}!</span>
              <button onClick={handleLogout} className="bg-indigo-600 px-4 py-2 rounded text-white hover:bg-indigo-700">Logout</button>
            </div>
          </nav>
        </div>
      </header>
      
      <main className="container mx-auto px-5 py-8">
        {!selectedQuestion ? (
          <>
            <h2 className="text-3xl font-bold mb-4">Educator Dashboard</h2>
            <div className="overflow-x-auto">
              <table className="min-w-full bg-slate-800 border border-slate-700">
                <thead>
                  <tr>
                    <th className="px-4 py-2 border border-slate-700">ID</th>
                    <th className="px-4 py-2 border border-slate-700">Question Text</th>
                    <th className="px-4 py-2 border border-slate-700">Difficulty</th>
                    <th className="px-4 py-2 border border-slate-700">Topic</th>
                    <th className="px-4 py-2 border border-slate-700">Question Type</th>
                    <th className="px-4 py-2 border border-slate-700">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {questions.map((q) => (
                    <tr key={q.id} className="hover:bg-slate-700">
                      <td className="px-4 py-2 border border-slate-700">{q.id}</td>
                      <td className="px-4 py-2 border border-slate-700">{q.questionText}</td>
                      <td className="px-4 py-2 border border-slate-700">{q.difficulty}</td>
                      <td className="px-4 py-2 border border-slate-700">{q.topic?.name}</td>
                      <td className="px-4 py-2 border border-slate-700">{q.questionType}</td>
                      <td className="px-4 py-2 border border-slate-700 flex gap-2">
                        <button onClick={() => handleView(q)} className="bg-blue-500 px-3 py-1 rounded hover:bg-blue-600">View</button>
                        <button onClick={() => handleUpdate(q)} className="bg-yellow-500 px-3 py-1 rounded hover:bg-yellow-600">Update</button>
                        <button onClick={() => handleDelete(q.id)} className="bg-red-500 px-3 py-1 rounded hover:bg-red-600">Delete</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        ) : (
          <div className="p-6 bg-slate-800 rounded-lg shadow-lg">
            <h3 className="text-2xl font-bold mb-4">{selectedQuestion.mode === 'view' ? 'View' : 'Update'} Question</h3>
            <p><strong>Question Text:</strong> {selectedQuestion.questionText}</p>
            <p><strong>Difficulty:</strong> {selectedQuestion.difficulty}</p>
            <p><strong>Topic:</strong> {selectedQuestion.topic?.name}</p>
            <p><strong>Type:</strong> {selectedQuestion.questionType}</p>
            {selectedQuestion.mode === 'update' && (
              <button className="bg-green-500 px-4 py-2 rounded hover:bg-green-600 mt-4">Save Changes</button>
            )}
            <button onClick={() => setSelectedQuestion(null)} className="bg-gray-500 px-4 py-2 rounded hover:bg-gray-600 mt-4 ml-4">Back</button>
          </div>
        )}
      </main>
      
      <footer className="bg-slate-800 border-t border-slate-700 py-4 text-center text-slate-400 text-sm">
        <p>© 2025 QuizHub. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default Question;

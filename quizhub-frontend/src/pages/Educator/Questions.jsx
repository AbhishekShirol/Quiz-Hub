import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Question() {
  // List of questions and topics
  const [questions, setQuestions] = useState([]);
  const [topics, setTopics] = useState([]);
  // State to decide if we are in view/update mode.
  const [selectedQuestion, setSelectedQuestion] = useState(null);
  const [updateMode, setUpdateMode] = useState(false);
  const [loading, setLoading] = useState(false);
  // For adding new topic inline.
  const [addingTopic, setAddingTopic] = useState(false);
  const [newTopicName, setNewTopicName] = useState('');
  
  const navigate = useNavigate();

  // Fetch questions and topics when component loads
  useEffect(() => {
    axios.get('http://localhost:8080/questions/get-questions')
      .then(response => setQuestions(response.data))
      .catch(error => console.error('Error fetching questions:', error));
      
    fetchTopics();
  }, []);

  const fetchTopics = () => {
    axios.get('http://localhost:8080/topics/getall-topics')
      .then(response => setTopics(response.data))
      .catch(error => console.error('Error fetching topics:', error));
  };

  // For viewing detailed question
  const handleView = (id) => {
    setLoading(true);
    axios.get(`http://localhost:8080/questions/get-question/${id}`)
      .then(response => {
        setSelectedQuestion({ ...response.data });
        setLoading(false);
        setUpdateMode(false);
      })
      .catch(error => {
        console.error('Error fetching question details:', error);
        setLoading(false);
      });
  };

  // For loading question data into update form
  const handleUpdate = (id) => {
    setLoading(true);
    axios.get(`http://localhost:8080/questions/get-question/${id}`)
      .then(response => {
        // Ensure options are in an array for the form.
        const questionData = {
          ...response.data,
          // For correctOptions, we assume backend returns an array of strings (option text).
          // We'll work with the same text in the options array.
          // If you later decide to use indexes, you can adjust accordingly.
        };
        setSelectedQuestion(questionData);
        setUpdateMode(true);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching question details:', error);
        setLoading(false);
      });
  };

  // Handle deletion of a question
  const handleDelete = (id) => {
    if (window.confirm('Do you want to delete this question?')) {
      axios.delete(`http://localhost:8080/questions/delete-question/${id}`)
        .then(() => {
          // Remove the question from the state
          setQuestions(questions.filter(q => q.id !== id));
          // Show success message
          alert('Question deleted successfully');
        })
        .catch(error => {
          console.error('Error deleting question:', error);
          alert('Error deleting question: ' + (error.response?.data || error.message));
        });
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    navigate('/login');
  };

  // Handlers for update form fields
  const handleInputChange = (field, value) => {
    setSelectedQuestion(prev => ({
      ...prev,
      [field]: value
    }));
  };

  // Update question text, hint, difficulty, etc.
  const handleOptionChange = (index, value) => {
    const updatedOptions = [...selectedQuestion.options];
    updatedOptions[index] = value;
    setSelectedQuestion(prev => ({
      ...prev,
      options: updatedOptions
    }));
  };

  // Add new option input field (appends one empty option)
  const handleAddOption = () => {
    setSelectedQuestion(prev => ({
      ...prev,
      options: [...prev.options, '']
    }));
  };

  // Remove the last option (only if there's more than one option)
  const handleRemoveLastOption = () => {
    setSelectedQuestion(prev => {
      // Only remove if there's more than one option
      if (prev.options.length > 1) {
        const updatedOptions = [...prev.options];
        const removedOption = updatedOptions.pop(); // Remove the last option
        
        // If the removed option was marked as correct, remove it from correctOptions too
        const updatedCorrectOptions = prev.correctOptions.filter(opt => opt !== removedOption);
        
        return {
          ...prev,
          options: updatedOptions,
          correctOptions: updatedCorrectOptions
        };
      }
      return prev; // Don't change if only one option remains
    });
  };

  // Toggle checkbox for correct options.
  const handleCheckboxChange = (option) => {
    let updatedCorrectOptions = selectedQuestion.correctOptions ? [...selectedQuestion.correctOptions] : [];
    if (updatedCorrectOptions.includes(option)) {
      // Remove if unchecked
      updatedCorrectOptions = updatedCorrectOptions.filter(opt => opt !== option);
    } else {
      // Add if checked
      updatedCorrectOptions.push(option);
    }
    setSelectedQuestion(prev => ({
      ...prev,
      correctOptions: updatedCorrectOptions
    }));
  };

  // Handle topic dropdown change
  const handleTopicChange = (e) => {
    const value = e.target.value;
    if (value === "add_new") {
      setAddingTopic(true);
    } else {
      // value is the topic id (as string), so convert to number
      const topicId = parseInt(value, 10);
      // Find the topic object and update selectedQuestion.topic
      const selectedTopic = topics.find(t => t.id === topicId);
      setSelectedQuestion(prev => ({
        ...prev,
        topic: selectedTopic
      }));
      setAddingTopic(false);
    }
  };

  // Handle creation of new topic and update topics list
  const handleCreateTopic = () => {
    axios.post('http://localhost:8080/topics/create-topic', { name: newTopicName })
      .then(response => {
        // Refresh topics list and set the newly created topic to selectedQuestion.topic
        fetchTopics();
        setSelectedQuestion(prev => ({
          ...prev,
          topic: response.data
        }));
        setNewTopicName('');
        setAddingTopic(false);
      })
      .catch(error => console.error('Error creating new topic:', error));
  };

  // Handle update submit: PUT request to update the question.
  const handleUpdateSubmit = () => {
    axios.put(`http://localhost:8080/questions/update-question/${selectedQuestion.id}`, selectedQuestion)
      .then(response => {
        // After update, refresh questions list and exit update mode.
        axios.get('http://localhost:8080/questions/get-questions')
          .then(response => setQuestions(response.data))
          .catch(error => console.error('Error fetching questions:', error));
        setSelectedQuestion(response.data);
        setUpdateMode(false);
        alert("Question updated successfully!");
      })
      .catch(error => console.error('Error updating question:', error));
  };

  return (
    <div className="bg-slate-900 text-slate-200 min-h-screen">
      {/* HEADER */}
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

      {/* MAIN CONTENT */}
      <main className="container mx-auto px-5 py-8">
        {loading && <p className="text-center text-lg font-bold text-blue-500">Loading question details...</p>}
        {/* If no question is selected, show dashboard table */}
        {!selectedQuestion && (
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
                        <button onClick={() => handleView(q.id)} className="bg-blue-500 px-3 py-1 rounded hover:bg-blue-600">View</button>
                        <button onClick={() => handleUpdate(q.id)} className="bg-yellow-500 px-3 py-1 rounded hover:bg-yellow-600">Update</button>
                        <button onClick={() => handleDelete(q.id)} className="bg-red-500 px-3 py-1 rounded hover:bg-red-600">Delete</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}

        {/* If a question is selected, show view or update form */}
        {selectedQuestion && updateMode && (
          <div className="p-6 bg-slate-800 rounded-lg shadow-lg">
            <h3 className="text-2xl font-bold mb-4">Update Question</h3>
            <div className="mb-4">
              <label className="block font-semibold mb-1">Question Text:</label>
              <textarea
                className="w-full p-2 rounded bg-slate-700"
                value={selectedQuestion.questionText}
                onChange={(e) => handleInputChange('questionText', e.target.value)}
              />
            </div>

            <div className="mb-4">
              <label className="block font-semibold mb-1">Question Type:</label>
              <select
                value={selectedQuestion.questionType}
                onChange={(e) => handleInputChange('questionType', e.target.value)}
                className="w-full p-2 rounded bg-slate-700"
              >
                <option value="MCQ">MCQ</option>
                <option value="TRUE_FALSE">TRUE_FALSE</option>
              </select>
            </div>

            <div className="mb-4">
            <label className="block font-semibold mb-1">Difficulty:</label>
            <select
              value={selectedQuestion.difficulty}
              onChange={(e) => handleInputChange('difficulty', e.target.value)}
              className="w-full p-2 rounded bg-slate-700"
            >
              <option value="EASY">EASY</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HARD">HARD</option>
            </select>
            </div> 

            <div className="mb-4">
              <label className="block font-semibold mb-1">Hint:</label>
              <textarea
                className="w-full p-2 rounded bg-slate-700"
                value={selectedQuestion.hint}
                onChange={(e) => handleInputChange('hint', e.target.value)}
              />
            </div>

            {/* Topic dropdown with "Add New Topic" option */}
            <div className="mb-4">
              <label className="block font-semibold mb-1">Topic:</label>
              <select
                value={selectedQuestion.topic?.id || ""}
                onChange={handleTopicChange}
                className="w-full p-2 rounded bg-slate-700"
              >
                <option value="">Select Topic</option>
                {topics.map((t) => (
                  <option key={t.id} value={t.id}>{t.name}</option>
                ))}
                <option value="add_new">Add New Topic...</option>
              </select>
              {addingTopic && (
                <div className="mt-2 flex items-center">
                  <input
                    type="text"
                    placeholder="New Topic Name"
                    value={newTopicName}
                    onChange={(e) => setNewTopicName(e.target.value)}
                    className="p-2 rounded bg-slate-700 mr-2"
                  />
                  <button onClick={handleCreateTopic} className="bg-green-500 px-3 py-1 rounded hover:bg-green-600">Create Topic</button>
                </div>
              )}
            </div>

            {/* Options with checkboxes */}
            <div className="mb-4">
              <label className="block font-semibold mb-1">Options:</label>
              {selectedQuestion.options.map((option, index) => (
                <div key={index} className="flex items-center mb-2">
                  <span className="mr-2 font-bold">{String.fromCharCode(65 + index)}.</span>
                  <input
                    type="text"
                    value={option}
                    onChange={(e) => handleOptionChange(index, e.target.value)}
                    className="p-2 rounded bg-slate-700 mr-2 flex-1"
                  />
                  <input
                    type="checkbox"
                    checked={selectedQuestion.correctOptions && selectedQuestion.correctOptions.includes(option)}
                    onChange={() => handleCheckboxChange(option)}
                    className="mr-1"
                  />
                  <span>Correct</span>
                </div>
              ))}
              <div className="flex gap-2">
                <button onClick={handleAddOption} className="bg-blue-500 px-3 py-1 rounded hover:bg-blue-600">Add Option</button>
                <button 
                  onClick={handleRemoveLastOption} 
                  className="bg-red-500 px-3 py-1 rounded hover:bg-red-600"
                  disabled={selectedQuestion.options.length <= 1}
                >
                  Remove Last Option
                </button>
              </div>
            </div>

            {/* Display non-editable createdBy */}
            <div className="mb-4">
              <label className="block font-semibold mb-1">Created By:</label>
              <p className="p-2 rounded bg-slate-700">{selectedQuestion.createdBy?.username}</p>
            </div>

            <div className="flex gap-4">
              <button onClick={handleUpdateSubmit} className="bg-green-500 px-4 py-2 rounded hover:bg-green-600">Save Changes</button>
              <button onClick={() => { setSelectedQuestion(null); setUpdateMode(false); }} className="bg-gray-500 px-4 py-2 rounded hover:bg-gray-600">Cancel</button>
            </div>
          </div>
        )}

        {/* View mode: display question details */}
        {selectedQuestion && !updateMode && (
          <div className="p-6 bg-slate-800 rounded-lg shadow-lg">
            <h3 className="text-2xl font-bold mb-4">Question Details</h3>
            <p><strong>Question:</strong> {selectedQuestion.questionText}</p>
            <p><strong>Difficulty:</strong> {selectedQuestion.difficulty}</p>
            <p><strong>Type:</strong> {selectedQuestion.questionType}</p>
            <p><strong>Hint:</strong> {selectedQuestion.hint || "No hint available"}</p>
            <p><strong>Topic:</strong> {selectedQuestion.topic?.name}</p>
            <p><strong>Created By:</strong> {selectedQuestion.createdBy?.username}</p>
            <p><strong>Created At:</strong> {new Date(selectedQuestion.createdBy?.createdAt).toLocaleString()}</p>
            
            {selectedQuestion.options.length > 0 && (
              <div>
                <strong>Options:</strong>
                <ul className="list-disc ml-6">
                  {selectedQuestion.options.map((opt, index) => (
                    <li key={index}>{opt}</li>
                  ))}
                </ul>
              </div>
            )}

            {selectedQuestion.correctOptions.length > 0 && (
              <div>
                <strong>Correct Answers:</strong>
                <ul className="list-disc ml-6 text-green-400">
                  {selectedQuestion.correctOptions.map((opt, index) => (
                    <li key={index}>{opt}</li>
                  ))}
                </ul>
              </div>
            )}

            <button onClick={() => setSelectedQuestion(null)} className="bg-gray-500 px-4 py-2 rounded hover:bg-gray-600 mt-4">Back</button>
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

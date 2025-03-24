import React, { useState, useEffect } from 'react';
import axios from 'axios';

function EducatorDashboard() {
  // States for topics, questions, and form data.
  const [topics, setTopics] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [formData, setFormData] = useState({
    questionText: '',
    questionType: 'MCQ',
    difficulty: 'EASY',
    hint: '',
    topicId: '',
    options: [''],
    correctOptions: ['']
  });
  const [newTopic, setNewTopic] = useState('');
  const [showNewTopicInput, setShowNewTopicInput] = useState(false);

  // For demonstration, assume educator's id is 1.
  const educatorId = 1;

  // Fetch topics and educator's questions on component mount
  useEffect(() => {
    fetchTopics();
    fetchQuestions();
  }, []);

  const fetchTopics = async () => {
    try {
      const res = await axios.get('http://localhost:8080/topics');
      setTopics(res.data);
    } catch (error) {
      console.error('Error fetching topics:', error);
    }
  };

  const fetchQuestions = async () => {
    try {
      // Assume an endpoint that returns questions created by this educator.
      const res = await axios.get(`http://localhost:8080/questions`);
      setQuestions(res.data);
    } catch (error) {
      console.error('Error fetching questions:', error);
    }
  };

  // Handle changes in form fields
  const handleInputChange = (e) => {
    setFormData({...formData, [e.target.name]: e.target.value});
  };

  // Handle dynamic options / correctOptions change
  const handleArrayChange = (field, index, value) => {
    const updated = [...formData[field]];
    updated[index] = value;
    setFormData({...formData, [field]: updated});
  };

  // Add new empty field to options/correctOptions
  const addArrayField = (field) => {
    setFormData({...formData, [field]: [...formData[field], '']});
  };

  // Submit new question
  const handleSubmitQuestion = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(
        `http://localhost:8080/questions/create?topicId=${formData.topicId}&userId=${educatorId}`,
        formData
      );
      console.log('Question created:', res.data);
      fetchQuestions(); // refresh list
      // Clear form
      setFormData({
        questionText: '',
        questionType: 'MCQ',
        difficulty: 'EASY',
        hint: '',
        topicId: '',
        options: [''],
        correctOptions: ['']
      });
    } catch (error) {
      console.error('Error creating question:', error);
    }
  };

  // Delete a question
  const handleDeleteQuestion = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/questions/${id}/delete`);
      fetchQuestions();
    } catch (error) {
      console.error('Error deleting question:', error);
    }
  };

  // Submit a new topic
  const handleNewTopicSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8080/topics', { name: newTopic });
      console.log('New topic created:', res.data);
      setNewTopic('');
      setShowNewTopicInput(false);
      fetchTopics();
    } catch (error) {
      console.error('Error creating topic:', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-gray-200">
      {/* Navbar */}
      <nav className="bg-gray-800 p-4 flex justify-between items-center">
        <div className="flex space-x-4">
          <a href="/dashboard" className="text-indigo-500 font-bold">Dashboard</a>
          <a href="/create-quiz" className="text-gray-300 hover:text-indigo-500">Create Quiz</a>
          <a href="/add-question" className="text-gray-300 hover:text-indigo-500">Add Question</a>
          <a href="/topics" className="text-gray-300 hover:text-indigo-500">Topics</a>
        </div>
        <button className="bg-indigo-600 px-4 py-2 rounded hover:bg-indigo-700">Logout</button>
      </nav>

      <div className="p-8">
        <h2 className="text-2xl font-bold mb-4">Add New Question</h2>
        <form onSubmit={handleSubmitQuestion} className="space-y-4">
          <div>
            <label className="block mb-1">Question Text:</label>
            <textarea
              name="questionText"
              value={formData.questionText}
              onChange={handleInputChange}
              className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
              required
            />
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block mb-1">Question Type:</label>
              <select 
                name="questionType" 
                value={formData.questionType} 
                onChange={handleInputChange}
                className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
              >
                <option value="MCQ">MCQ</option>
                <option value="TRUE_FALSE">True/False</option>
              </select>
            </div>
            <div>
              <label className="block mb-1">Difficulty:</label>
              <select 
                name="difficulty" 
                value={formData.difficulty} 
                onChange={handleInputChange}
                className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
              >
                <option value="EASY">Easy</option>
                <option value="MEDIUM">Medium</option>
                <option value="HARD">Hard</option>
              </select>
            </div>
          </div>
          <div>
            <label className="block mb-1">Hint (Optional):</label>
            <textarea
              name="hint"
              value={formData.hint}
              onChange={handleInputChange}
              className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
            />
          </div>
          <div>
            <label className="block mb-1">Topic:</label>
            <select
              name="topicId"
              value={formData.topicId}
              onChange={handleInputChange}
              className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
              required
            >
              <option value="">Select Topic</option>
              {topics.map((topic) => (
                <option key={topic.id} value={topic.id}>{topic.name}</option>
              ))}
            </select>
            <button type="button" onClick={() => setShowNewTopicInput(true)} className="mt-2 text-indigo-500 underline">
              Add New Topic
            </button>
            {showNewTopicInput && (
              <form onSubmit={handleNewTopicSubmit} className="mt-2 flex gap-2">
                <input
                  type="text"
                  value={newTopic}
                  onChange={(e) => setNewTopic(e.target.value)}
                  placeholder="New Topic Name"
                  className="p-2 bg-gray-800 border border-gray-600 rounded"
                  required
                />
                <button type="submit" className="bg-indigo-600 px-3 py-2 rounded hover:bg-indigo-700">Save</button>
              </form>
            )}
          </div>
          <div>
            <label className="block mb-1">Options:</label>
            {formData.options.map((option, index) => (
              <div key={index} className="flex gap-2 mb-2">
                <input
                  type="text"
                  value={option}
                  onChange={(e) => handleArrayChange('options', index, e.target.value)}
                  placeholder={`Option ${index + 1}`}
                  className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
                  required
                />
              </div>
            ))}
            <button type="button" onClick={() => addArrayField('options')} className="text-indigo-500 underline">
              Add Option
            </button>
          </div>
          <div>
            <label className="block mb-1">Correct Options:</label>
            {formData.correctOptions.map((option, index) => (
              <div key={index} className="flex gap-2 mb-2">
                <input
                  type="text"
                  value={option}
                  onChange={(e) => handleArrayChange('correctOptions', index, e.target.value)}
                  placeholder={`Correct Option ${index + 1}`}
                  className="w-full p-2 bg-gray-800 border border-gray-600 rounded"
                  required
                />
              </div>
            ))}
            <button type="button" onClick={() => addArrayField('correctOptions')} className="text-indigo-500 underline">
              Add Correct Option
            </button>
          </div>
          <button type="submit" className="bg-indigo-600 px-4 py-2 rounded hover:bg-indigo-700 text-white">
            Save Question
          </button>
        </form>
      </div>

      <div className="p-8">
        <h2 className="text-2xl font-bold mb-4">My Questions</h2>
        {questions.length === 0 ? (
          <p>No questions added yet.</p>
        ) : (
          <ul className="space-y-4">
            {questions.map((q) => (
              <li key={q.id} className="p-4 bg-gray-800 border border-gray-600 rounded flex justify-between items-center">
                <div>
                  <p className="font-bold">{q.questionText}</p>
                  <p className="text-sm text-gray-400">Topic: {q.topic?.name}</p>
                </div>
                <div className="flex gap-2">
                  <button className="bg-yellow-500 px-3 py-1 rounded hover:bg-yellow-600">
                    Update
                  </button>
                  <button className="bg-red-500 px-3 py-1 rounded hover:bg-red-600" onClick={() => handleDeleteQuestion(q.id)}>
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default EducatorDashboard;

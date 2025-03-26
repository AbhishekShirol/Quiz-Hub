import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function CreateQuiz() {
  const navigate = useNavigate();

  // State for quiz details
  const [quizData, setQuizData] = useState({
    title: '',
    description: '',
    difficulty: 'EASY',
    timeLimit: 30, // in minutes
    topic: null,
    selectedQuestions: [],
  });

  // State for topics and questions
  const [topics, setTopics] = useState([]);
  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    fetchTopics();
  }, []);

  // Fetch available topics
  const fetchTopics = () => {
    axios.get('http://localhost:8080/topics/getall-topics')
      .then(response => setTopics(response.data))
      .catch(error => console.error('Error fetching topics:', error));
  };

  // Fetch questions when a topic is selected
  const fetchQuestions = (topicId) => {
    axios.get(`http://localhost:8080/questions/by-topic/${topicId}`)
      .then(response => setQuestions(response.data))
      .catch(error => console.error('Error fetching questions:', error));
  };

  // Handle quiz input change
  const handleInputChange = (field, value) => {
    setQuizData(prev => ({ ...prev, [field]: value }));
  };

  // Handle topic selection and load questions
  const handleTopicChange = (e) => {
    const topicId = e.target.value;
    const selectedTopic = topics.find(t => t.id === parseInt(topicId, 10));
    setQuizData(prev => ({ ...prev, topic: selectedTopic, selectedQuestions: [] }));
    fetchQuestions(topicId);
  };

  // Handle question selection
  const handleQuestionSelect = (questionId) => {
    setQuizData(prev => {
      const isSelected = prev.selectedQuestions.includes(questionId);
      return {
        ...prev,
        selectedQuestions: isSelected
          ? prev.selectedQuestions.filter(q => q !== questionId) // Remove if already selected
          : [...prev.selectedQuestions, questionId], // Add if not selected
      };
    });
  };

  // Submit handler
  const handleSubmit = (e) => {
    e.preventDefault();

    if (!quizData.topic) {
      alert('Please select a topic.');
      return;
    }
    if (quizData.selectedQuestions.length === 0) {
      alert('Please select at least one question.');
      return;
    }

    const userId = localStorage.getItem('userId');
    if (!userId) {
      alert('User ID not found!');
      return;
    }

    const payload = {
      title: quizData.title,
      description: quizData.description,
      difficulty: quizData.difficulty,
      timeLimit: quizData.timeLimit,
      questions: quizData.selectedQuestions,
    };

    axios.post('http://localhost:8080/quizzes/create', payload, {
      params: {
        topicId: quizData.topic.id,
        userId: userId,
      }
    })
    .then(response => {
      alert('Quiz created successfully!');
      navigate('/educator-dashboard');
    })
    .catch(error => {
      console.error('Error creating quiz:', error);
      alert('Error creating quiz. Please try again.');
    });
  };

  return (
    <div className="bg-slate-900 text-slate-200 min-h-screen p-6">
      <h2 className="text-3xl font-bold mb-4">Create New Quiz</h2>
      <form onSubmit={handleSubmit} className="space-y-4 max-w-3xl mx-auto">
        {/* Quiz Title */}
        <div>
          <label className="block font-semibold mb-1">Quiz Title:</label>
          <input
            type="text"
            value={quizData.title}
            onChange={(e) => handleInputChange('title', e.target.value)}
            className="w-full p-2 rounded bg-slate-700"
            required
          />
        </div>

        {/* Quiz Description */}
        <div>
          <label className="block font-semibold mb-1">Quiz Description:</label>
          <textarea
            value={quizData.description}
            onChange={(e) => handleInputChange('description', e.target.value)}
            className="w-full p-2 rounded bg-slate-700"
            rows={3}
          />
        </div>

        {/* Difficulty Level */}
        <div>
          <label className="block font-semibold mb-1">Difficulty:</label>
          <select
            value={quizData.difficulty}
            onChange={(e) => handleInputChange('difficulty', e.target.value)}
            className="w-full p-2 rounded bg-slate-700"
          >
            <option value="EASY">EASY</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HARD">HARD</option>
          </select>
        </div>

        {/* Time Limit */}
        <div>
          <label className="block font-semibold mb-1">Time Limit (minutes):</label>
          <input
            type="number"
            value={quizData.timeLimit}
            onChange={(e) => handleInputChange('timeLimit', e.target.value)}
            className="w-full p-2 rounded bg-slate-700"
            min={1}
            required
          />
        </div>

        {/* Topic Selection */}
        <div>
          <label className="block font-semibold mb-1">Select Topic:</label>
          <select
            value={quizData.topic?.id || ''}
            onChange={handleTopicChange}
            className="w-full p-2 rounded bg-slate-700"
            required
          >
            <option value="">Select Topic</option>
            {topics.map((t) => (
              <option key={t.id} value={t.id}>{t.name}</option>
            ))}
          </select>
        </div>

        {/* Question Selection */}
        <div>
          <label className="block font-semibold mb-1">Select Questions:</label>
          {questions.length > 0 ? (
            <div className="bg-slate-800 p-4 rounded">
              {questions.map((q) => (
                <div key={q.id} className="flex items-center mb-2">
                  <input
                    type="checkbox"
                    checked={quizData.selectedQuestions.includes(q.id)}
                    onChange={() => handleQuestionSelect(q.id)}
                    className="mr-2"
                  />
                  <span>{q.questionText}</span>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-red-400">No questions available for this topic.</p>
          )}
        </div>

        {/* Submit and Back Buttons */}
        <div className="flex gap-4">
          <button
            type="submit"
            className="bg-green-500 px-4 py-2 rounded hover:bg-green-600 text-white"
          >
            Create Quiz
          </button>
          <button
            type="button"
            onClick={() => navigate('/educator-dashboard')}
            className="bg-gray-500 px-4 py-2 rounded hover:bg-gray-600 text-white"
          >
            Back
          </button>
        </div>
      </form>
    </div>
  );
}

export default CreateQuiz;

# 📚 QuizHub

**QuizHub** is a full-featured Quiz Management System designed for educational platforms. It supports **role-based user interaction with auth**, **quiz creation**, **public ,private and filtered quizzes**, **real-time quiz attempts**, and **detailed quiz attempt history** — all in one seamless experience.

Built with:
- 🖥️ **Frontend**: React
- ⚙️ **Backend**: Java Spring Boot (MVC Architecture)
- 💾 **Database**: MySQL

---

## 🚀 Major Features

### 👥 Role-Based User Management with Auth
- **Admin**: 
  - Manage all users
  - Create new admin accounts
- **Educator**: 
  - Create quizzes (Private and Public)
  - View student analytics (quiz attempt history)
  - Add and manage questions with various difficulty levels
  - Set visibility and access options for quizzes as Public and Private
- **Student**: 
  - Attempt quizzes using direct access or quiz codes
  - Track performance and review past attempts
  - Generate filtered quizzes based on preferences
---

### 📝 Quiz Creation
- Educators can create:
  - **MCQs** with multiple options and one correct answer
  - **True/False Questions** with binary response options
- Create **Public** and **Private Quizzes**:
  - Private quizzes require a **quiz code** to join
  - Public quizzes are accessible to all users in the question pool
- Add **Hints** to questions (using them reduces score)
- Configure quiz settings:
  - Set **total time limit for Quiz**
  - Customize quiz title and description
  - Define difficulty level for the entire quiz
---

### 🎯 Filtered Quiz System
- Students can generate **custom quizzes** by selecting:
  - **Topic** of interest
  - **Difficulty Level** appropriate to their knowledge
  - **Number of Questions** to include
  - **Time for Quiz** completion
- System workflow:
  - Based on the selected filters, the system searches the question database
  - If enough questions are available, creates the filtered quiz
  - If question shortage occurs, notifies student to adjust criteria
  - Allows students to save generated quizzes for later or start immediately
---

### 📊 Quiz Attempt Process
- **Starting a Quiz**:
  - Students can find quizzes through browsing or enter a quiz code
  - Quiz timer begins once the attempt is initiated
  - Questions are presented sequentially to the student

- **During the Quiz**:
  - Students read questions and consider answers
  - Option to use hints with score reduction penalty
  - Submit answers which are automatically saved
  - Progress through all questions until completion

- **Quiz Completion**:
  - Quiz ends when all questions are answered or timer expires
  - Automatic submission occurs if time limit is reached
  - System calculates final score based on correct answers and hint usage
  - Students can review results including correct answers
  - Performance data is stored for future progress tracking


---

## 🧩 Minor Features

### 🧠 Hint System
- Each question can include a hint.
- Students can use hints at the cost of reduced points.
---

### 🗃️ Question Bank Management
- Educators manage a question bank with:
  - Question & Options
  - Question type(MCQ & True or False)
  - Topic
  - Difficulty
  - Hints
---

### ⏲️ Timer Functionality
- Total quiz time can be enforced.
- Time taken on that Question
---
### 🔍 Search Functionality for PrivateQuizzes
- Users can search quizzes by:
  - **The private Code Provided By the Educator**
---
### 📈 Analytics Dashboard
- Educators can view detailed analytics of:
  - Quiz attempts for the quizzes
---

## 🛠 Tech Stack

| Layer        | Technology         |
|--------------|--------------------|
| Frontend     | React              |
| Backend      | Java Spring Boot (MVC) |
| Database     | MySQL              |

---
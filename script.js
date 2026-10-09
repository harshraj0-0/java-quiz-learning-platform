
/* ==========================================
   JAVAQUEST - QUIZ APPLICATION
   ========================================== */

// Java quiz questions
const questionBank = {
    basics: [
        {
            q: "Which keyword is used to declare a class in Java?",
            options: ["define", "class", "struct", "object"],
            answer: 1,
            explanation: "The class keyword is used to declare a class in Java."
        },
        {
            q: "Which data type stores true or false?",
            options: ["int", "char", "boolean", "String"],
            answer: 2,
            explanation: "The boolean data type stores either true or false."
        },
        {
            q: "Which method is the usual entry point of a Java application?",
            options: ["start()", "run()", "main()", "init()"],
            answer: 2,
            explanation: "The main method is the standard entry point of a Java application."
        },
        {
            q: "Which operator compares two primitive values for equality?",
            options: ["=", "==", "!=", "&&"],
            answer: 1,
            explanation: "The == operator compares primitive values for equality."
        }
    ],

    loops: [
        {
            q: "Which loop is useful when the number of iterations is known?",
            options: ["for", "if", "switch", "try"],
            answer: 0,
            explanation: "A for loop is useful when the initialization, condition, and update are known."
        },
        {
            q: "Which loop executes its body at least once?",
            options: ["for", "while", "do-while", "enhanced for"],
            answer: 2,
            explanation: "A do-while loop checks its condition after executing the body."
        },
        {
            q: "Which statement immediately terminates the current loop?",
            options: ["skip", "break", "continue", "next"],
            answer: 1,
            explanation: "The break statement terminates the current loop."
        },
        {
            q: "What does continue do inside a loop?",
            options: [
                "Ends the program",
                "Exits the loop completely",
                "Skips to the next iteration",
                "Declares a new variable"
            ],
            answer: 2,
            explanation: "The continue statement skips the remaining statements in the current iteration."
        }
    ],

    arrays: [
        {
            q: "What is the index of the first element in a Java array?",
            options: ["1", "-1", "0", "It depends on the array"],
            answer: 2,
            explanation: "Java arrays use zero-based indexing, so the first index is 0."
        },
        {
            q: "Which property gives the length of an array named nums?",
            options: [
                "nums.size()",
                "nums.length",
                "nums.count()",
                "nums.length()"
            ],
            answer: 1,
            explanation: "Use nums.length to get the number of elements in a Java array."
        },
        {
            q: "Which statement creates an integer array of five elements?",
            options: [
                "int nums = new int[5];",
                "int[] nums = new int[5];",
                "array nums = 5;",
                "int[5] nums;"
            ],
            answer: 1,
            explanation: "int[] nums = new int[5]; declares an integer array with five elements."
        },
        {
            q: "What happens if nums[5] is accessed in an array of length 5?",
            options: [
                "It returns zero",
                "It creates another element",
                "It accesses the fifth element",
                "It throws ArrayIndexOutOfBoundsException"
            ],
            answer: 3,
            explanation: "A five-element array has valid indexes from 0 to 4."
        }
    ],

    oop: [
        {
            q: "Which OOP concept combines data and methods and controls access to data?",
            options: [
                "Encapsulation",
                "Iteration",
                "Compilation",
                "Casting"
            ],
            answer: 0,
            explanation: "Encapsulation bundles data and related methods while controlling access to an object's state."
        },
        {
            q: "Which keyword is used for class inheritance in Java?",
            options: ["implements", "inherits", "extends", "super"],
            answer: 2,
            explanation: "The extends keyword allows a class to inherit from another class."
        },
        {
            q: "What is an object in Java?",
            options: [
                "A loop condition",
                "An instance of a class",
                "A primitive data type",
                "A package name"
            ],
            answer: 1,
            explanation: "An object is an instance of a class."
        },
        {
            q: "Which OOP concept allows behavior to vary based on the object or implementation?",
            options: [
                "Inheritance",
                "Polymorphism",
                "Encapsulation",
                "Initialization"
            ],
            answer: 1,
            explanation: "Polymorphism allows one interface or method name to represent different behaviors."
        }
    ]
};


// Topic names displayed in the quiz
const topicNames = {
    basics: "Java Basics",
    loops: "Loops",
    arrays: "Arrays",
    oop: "OOP Concepts"
};


// Find HTML elements
const topicsSection = document.getElementById("topics");
const quizSection = document.getElementById("quiz");
const resultsSection = document.getElementById("results");

const questionText = document.getElementById("question-text");
const optionsContainer = document.getElementById("options");
const feedback = document.getElementById("feedback");
const nextButton = document.getElementById("next-btn");


// Quiz state
let currentTopic = "basics";
let currentQuestions = [];
let questionIndex = 0;
let score = 0;
let selectedAnswer = null;
let answerChecked = false;


// Calculate the total number of questions
const totalQuestions = Object.values(questionBank).reduce(
    (total, questions) => total + questions.length,
    0
);

document.getElementById("question-count").textContent = totalQuestions;


// Read and display the best score
function updateBestScore() {
    let bestScore = "--";

    try {
        const savedScore = localStorage.getItem("javaquest-best");

        if (savedScore !== null) {
            bestScore = `${savedScore}%`;
        }
    } catch (error) {
        console.warn("Local score storage is unavailable.");
    }

    document.getElementById("best-score").textContent = bestScore;
}


// Start a selected topic quiz
function startQuiz(topic) {
    if (!questionBank[topic]) {
        return;
    }

    currentTopic = topic;
    currentQuestions = questionBank[topic];
    questionIndex = 0;
    score = 0;

    topicsSection.classList.add("hidden");
    resultsSection.classList.add("hidden");
    quizSection.classList.remove("hidden");

    document.getElementById("quiz-title").textContent =
        topicNames[topic];

    renderQuestion();

    quizSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}


// Display the current question
function renderQuestion() {
    selectedAnswer = null;
    answerChecked = false;

    const question = currentQuestions[questionIndex];
    const total = currentQuestions.length;
    const progress = ((questionIndex + 1) / total) * 100;

    document.getElementById("question-number").textContent =
        `Question ${questionIndex + 1} of ${total}`;

    document.getElementById("progress-percent").textContent =
        `${Math.round(progress)}%`;

    document.getElementById("progress-bar").style.width =
        `${progress}%`;

    questionText.textContent = question.q;
    optionsContainer.innerHTML = "";

    feedback.textContent = "";
    feedback.classList.add("hidden");

    // Create answer buttons
    question.options.forEach((optionText, index) => {
        const button = document.createElement("button");

        button.type = "button";
        button.className = "option";

        button.textContent =
            `${String.fromCharCode(65 + index)}. ${optionText}`;

        button.addEventListener("click", () => {
            if (answerChecked) {
                return;
            }

            selectedAnswer = index;

            document.querySelectorAll(".option").forEach(option => {
                option.classList.remove("selected");
            });

            button.classList.add("selected");

            nextButton.disabled = false;
            nextButton.textContent = "Check Answer →";
        });

        optionsContainer.appendChild(button);
    });

    nextButton.disabled = true;
    nextButton.textContent = "Check Answer →";
}


// Check the selected answer and display an explanation
function checkAnswer() {
    const question = currentQuestions[questionIndex];

    if (selectedAnswer === null || answerChecked) {
        return;
    }

    answerChecked = true;

    if (selectedAnswer === question.answer) {
        score++;
    }

    const optionButtons = document.querySelectorAll(".option");

    optionButtons.forEach((button, index) => {
        button.disabled = true;

        if (index === question.answer) {
            button.classList.add("correct");
        } else if (index === selectedAnswer) {
            button.classList.add("wrong");
        }
    });

    const isCorrect = selectedAnswer === question.answer;

    feedback.textContent =
        `${isCorrect ? "Correct! " : "Not quite. "}${question.explanation}`;

    feedback.classList.remove("hidden");

    nextButton.textContent =
        questionIndex === currentQuestions.length - 1
            ? "View Results →"
            : "Next Question →";

    nextButton.disabled = false;
}


// Display the final quiz results
function showResults() {
    quizSection.classList.add("hidden");
    resultsSection.classList.remove("hidden");

    const total = currentQuestions.length;
    const percentage = Math.round((score / total) * 100);

    document.getElementById("result-score").textContent =
        `${percentage}%`;

    document.getElementById("result-detail").textContent =
        `${score} out of ${total} correct`;

    let title;
    let message;

    if (percentage === 100) {
        title = "Perfect score!";
        message = "Excellent work! You answered every question correctly.";
    } else if (percentage >= 70) {
        title = "Well done!";
        message = "Great work. Keep building your Java skills.";
    } else if (percentage >= 40) {
        title = "Good effort!";
        message = "You're making progress. Review the concepts and try again.";
    } else {
        title = "Keep practicing!";
        message = "Review the explanations and give the quiz another try.";
    }

    document.getElementById("result-title").textContent = title;
    document.getElementById("result-message").textContent = message;

    // Save the highest percentage achieved
    try {
        const savedScore = localStorage.getItem("javaquest-best");
        const previousBest = savedScore === null
            ? 0
            : Number(savedScore);

        if (percentage > previousBest || savedScore === null) {
            localStorage.setItem("javaquest-best", percentage);
        }
    } catch (error) {
        console.warn("Could not save the best score.");
    }

    updateBestScore();

    resultsSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}


// Handle Check Answer and Next Question
nextButton.addEventListener("click", () => {
    if (!answerChecked) {
        checkAnswer();
        return;
    }

    if (questionIndex < currentQuestions.length - 1) {
        questionIndex++;
        renderQuestion();
    } else {
        showResults();
    }
});


// Connect all topic buttons
document.querySelectorAll("[data-topic]").forEach(button => {
    button.addEventListener("click", () => {
        startQuiz(button.dataset.topic);
    });
});


// Exit the quiz and return to the topics
document.getElementById("exit-quiz").addEventListener("click", () => {
    quizSection.classList.add("hidden");
    topicsSection.classList.remove("hidden");

    topicsSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
});


// Restart the current topic quiz
document.getElementById("retry-btn").addEventListener("click", () => {
    startQuiz(currentTopic);
});


// Return from results to the topics
document.getElementById("home-btn").addEventListener("click", () => {
    resultsSection.classList.add("hidden");
    topicsSection.classList.remove("hidden");

    topicsSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
});


// Initialize the dashboard
updateBestScore();

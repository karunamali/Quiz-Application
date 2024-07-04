package task_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApplication {
    private JFrame frame;
    private JLabel questionLabel;
    private JRadioButton[] optionsButtons;
    private JButton submitButton;
    private JButton startButton;
    private ButtonGroup optionsGroup;
    private JLabel timerLabel;
    private Timer timer;
    private int timeLeft;

    private QuizQuestion[] questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int[] userAnswers;

    public QuizApplication() {
        // Initialize the questions
        questions = new QuizQuestion[] {
            new QuizQuestion("Who is known as the father of computer science?", new String[] {"Charles Babbage", "Alan Turing", "John von Neumann", "George Boole"}, 1),
            new QuizQuestion("Who invented the World Wide Web?", new String[] {"Bill Gates", "Steve Jobs", "Tim Berners-Lee", "Mark Zuckerberg"}, 2),
            new QuizQuestion("Who is the co-founder of Microsoft?", new String[] {"Steve Jobs", "Paul Allen", "Bill Gates", "Jeff Bezos"}, 2),
            new QuizQuestion("Who is the founder of Apple Inc.?", new String[] {"Steve Jobs", "Bill Gates", "Elon Musk", "Larry Page"}, 0),
            new QuizQuestion("Who developed the Linux kernel?", new String[] {"Linus Torvalds", "Dennis Ritchie", "Ken Thompson", "Brian Kernighan"}, 0)
        };

        // Initialize user answers array
        userAnswers = new int[questions.length];

        // Set up the frame
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set frame size
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(255, 182, 193)); // Light Pink background

        // Center the frame
        frame.setLocationRelativeTo(null);

        // Set up the start button
        startButton = new JButton("Start Quiz");
        startButton.addActionListener(new StartButtonListener());
        startButton.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        frame.add(startButton, gbc);

        frame.setVisible(true);
    }

    private void startQuiz() {
        // Remove the start button
        frame.remove(startButton);

        // Set up the question label
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font
        GridBagConstraints gbcQuestionLabel = new GridBagConstraints();
        gbcQuestionLabel.gridx = 0;
        gbcQuestionLabel.gridy = 0;
        gbcQuestionLabel.anchor = GridBagConstraints.CENTER;
        gbcQuestionLabel.insets = new Insets(10, 10, 10, 10); // Padding
        frame.add(questionLabel, gbcQuestionLabel);

        // Set up the options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1));
        optionsButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionsButtons[i] = new JRadioButton();
            optionsGroup.add(optionsButtons[i]);
            optionsPanel.add(optionsButtons[i]);
            optionsButtons[i].setFont(new Font("Arial", Font.PLAIN, 18)); // Larger font
        }
        GridBagConstraints gbcOptionsPanel = new GridBagConstraints();
        gbcOptionsPanel.gridx = 0;
        gbcOptionsPanel.gridy = 1;
        gbcOptionsPanel.anchor = GridBagConstraints.CENTER;
        gbcOptionsPanel.insets = new Insets(10, 10, 10, 10); // Padding
        frame.add(optionsPanel, gbcOptionsPanel);

        // Set up the bottom panel with timer and submit button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        timerLabel = new JLabel("Time left: 10");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Larger font
        bottomPanel.add(timerLabel, BorderLayout.WEST);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        submitButton.setFont(new Font("Arial", Font.BOLD, 18)); // Larger font
        bottomPanel.add(submitButton, BorderLayout.EAST);

        GridBagConstraints gbcBottomPanel = new GridBagConstraints();
        gbcBottomPanel.gridx = 0;
        gbcBottomPanel.gridy = 2;
        gbcBottomPanel.anchor = GridBagConstraints.CENTER;
        gbcBottomPanel.insets = new Insets(10, 10, 10, 10); // Padding
        frame.add(bottomPanel, gbcBottomPanel);

        // Refresh the frame
        frame.revalidate();
        frame.repaint();

        // Display the first question
        displayQuestion();
    }

    private void displayQuestion() {
        // Check if there are no more questions
        if (currentQuestionIndex >= questions.length) {
            showResult();
            return;
        }

        // Display the current question and options
        QuizQuestion question = questions[currentQuestionIndex];
        questionLabel.setText(question.getQuestion());
        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            optionsButtons[i].setText(options[i]);
        }

        // Clear previous selection and start the timer
        optionsGroup.clearSelection();
        startTimer();
    }

    private void startTimer() {
        // Initialize the timer with 10 seconds
        timeLeft = 10;
        timerLabel.setText("Time left: " + timeLeft);

        // Stop any existing timer
        if (timer != null) {
            timer.stop();
        }

        // Create a new timer that counts down every second
        timer = new Timer(1000, new TimerListener());
        timer.start();
    }

    private void submitAnswer() {
        // Stop the timer
        timer.stop();

        // Get the selected option
        int selectedOption = -1;
        for (int i = 0; i < optionsButtons.length; i++) {
            if (optionsButtons[i].isSelected()) {
                selectedOption = i;
                break;
            }
        }

        // Store the user's answer
        userAnswers[currentQuestionIndex] = selectedOption;

        // Check if the selected option is correct
        if (selectedOption == questions[currentQuestionIndex].getCorrectAnswer()) {
            score++;
        }

        // Move to the next question
        currentQuestionIndex++;
        displayQuestion();
    }

    private void showResult() {
        // Display the final score and correct/incorrect answers
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("<html>");
        resultMessage.append("<h1 style='color:#FF1493;'>Quiz Over!</h1>");
        resultMessage.append("<p style='font-size:18px;'>Your score: <span style='font-weight:bold;'>").append(score).append(" out of ").append(questions.length).append("</span></p>");

        for (int i = 0; i < questions.length; i++) {
            resultMessage.append("<p>").append("Q").append(i + 1).append(": ").append(questions[i].getQuestion()).append("<br>");
            resultMessage.append("Your Answer: ").append(userAnswers[i] == -1 ? "No answer" : questions[i].getOptions()[userAnswers[i]]).append("<br>");
            resultMessage.append("Correct Answer: ").append(questions[i].getOptions()[questions[i].getCorrectAnswer()]).append("</p>");
        }

        resultMessage.append("</html>");

        // Show the result in a message dialog
        JOptionPane.showMessageDialog(frame, resultMessage.toString(), "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    // Listener for the start button
    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startQuiz();
        }
    }

    // Listener for the submit button
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            submitAnswer();
        }
    }

    // Listener for the timer
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft);
            if (timeLeft <= 0) {
                submitAnswer();
            }
        }
    }

    public static void main(String[] args) {
        // Create and show the quiz application
        SwingUtilities.invokeLater(() -> new QuizApplication());
    }
}

 

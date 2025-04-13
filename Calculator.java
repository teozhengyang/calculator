import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

// Use Swing for GUI due to wider compatibility and less platform-specific issues compared to JavaFX in some environments.
public class Calculator {

    private JFrame frame;
    private JTextField display;
    private JPanel buttonPanel;
    private JTextArea historyArea;
    private JScrollPane historyScrollPane;
    private boolean isDarkMode = false;
    private ArrayList<String> history = new ArrayList<>();
    
    // Main method to launch the calculator
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Calculator().createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Method to create and show the GUI
    private void createAndShowGUI() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800); 
        frame.setLocationRelativeTo(null);

        // Main panel to hold display, buttons, and history
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Display for the calculator
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setForeground(Color.BLACK); 
        mainPanel.add(display, BorderLayout.NORTH);

        // Panel for the buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // History area
        historyArea = new JTextArea();
        historyArea.setFont(new Font("Arial", Font.PLAIN, 16));
        historyArea.setEditable(false);
        historyScrollPane = new JScrollPane(historyArea);
        historyScrollPane.setPreferredSize(new Dimension(600, 200));
        mainPanel.add(historyScrollPane, BorderLayout.SOUTH);

        // Add buttons to the button panel
        addButtons();
        frame.add(mainPanel);
        frame.setVisible(true);
        toggleDarkMode(true);
    }

    // Method to add buttons to the button panel
    private void addButtons() {
        String[] buttonLabels = {
                "C", "±", "%", "÷", "(",
                "7", "8", "9", "×", ")",
                "4", "5", "6", "-", "nPr",
                "1", "2", "3", "+", "nCr",
                "0", ".", "="
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
        // Add light/dark mode toggle button
        JButton toggleModeButton = new JButton("Light/Dark");
        toggleModeButton.setFont(new Font("Arial", Font.BOLD, 16));
        toggleModeButton.addActionListener(e -> {
            isDarkMode = !isDarkMode;
            toggleDarkMode(isDarkMode);
        });
        buttonPanel.add(toggleModeButton);
    }

    // Method to handle button clicks
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            try {
                handleButtonAction(command);
            } catch (Exception ex) {
                display.setText("Error");
                updateHistory("Error: " + ex.getMessage());
            }
        }
    }

    private void handleButtonAction(String command) {
        switch (command) {
            case "C":
                display.setText("");
                break;
            case "±":
                negate();
                break;
            case "%":
                calculatePercentage();
                break;
            case "÷":
                appendOperator("/");
                break;
            case "×":
                appendOperator("*");
                break;
            case "-":
                appendOperator("-");
                break;
            case "+":
                appendOperator("+");
                break;
            case "=":
                calculateResult();
                break;
            case ".":
                appendDecimal();
                break;
            case "nPr":
                appendOperator("P");
                break;
            case "nCr":
                appendOperator("C");
                break;
            case "(":
                display.setText(display.getText() + "(");
                break;
            case ")":
                display.setText(display.getText() + ")");
                break;
            default:
                display.setText(display.getText() + command);
        }
    }

    // Method to toggle between dark and light mode
    private void toggleDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            frame.getContentPane().setBackground(Color.DARK_GRAY);
            display.setBackground(Color.DARK_GRAY);
            display.setForeground(Color.WHITE);
            buttonPanel.setBackground(Color.DARK_GRAY);
            historyArea.setBackground(Color.DARK_GRAY);
            historyArea.setForeground(Color.WHITE);
            historyScrollPane.getViewport().setBackground(Color.DARK_GRAY); //set the JScrollPane background
            for (Component c : buttonPanel.getComponents()) {
                if (c instanceof JButton) {
                    ((JButton) c).setForeground(Color.WHITE);
                    ((JButton) c).setBackground(new Color(64, 64, 64));
                }
            }
        } else {
            frame.getContentPane().setBackground(Color.WHITE);
            display.setBackground(Color.WHITE);
            display.setForeground(Color.BLACK);
            buttonPanel.setBackground(Color.WHITE);
            historyArea.setBackground(Color.WHITE);
            historyArea.setForeground(Color.BLACK);
            historyScrollPane.getViewport().setBackground(Color.WHITE);
            for (Component c : buttonPanel.getComponents()) {
                if (c instanceof JButton) {
                    ((JButton) c).setForeground(Color.BLACK);
                    ((JButton) c).setBackground(UIManager.getColor("Button.background")); 
                }
            }
        }
    }

    // Method to append operator
    private void appendOperator(String operator) {
        String currentText = display.getText();
        if (!currentText.isEmpty() && !isOperator(currentText.substring(currentText.length() - 1))) {
            display.setText(currentText + operator);
        }
    }

    private boolean isOperator(String text) {
        return "+".equals(text) || "-".equals(text) || "*".equals(text) || "/".equals(text) || "^".equals(text) || "P".equals(text) || "C".equals(text);
    }

    // Method to append decimal point
    private void appendDecimal() {
        String currentText = display.getText();
        if (!currentText.contains(".")) {
            display.setText(currentText + ".");
        }
    }

    // Method to negate the current number
    private void negate() {
        String currentText = display.getText();
        if (!currentText.isEmpty() && !currentText.equals("0")) {
            if (currentText.startsWith("-")) {
                display.setText(currentText.substring(1));
            } else {
                display.setText("-" + currentText);
            }
        }
    }

    // Method to calculate percentage
    private void calculatePercentage() {
        String currentText = display.getText();
        if (!currentText.isEmpty()) {
            double value = Double.parseDouble(currentText);
            display.setText(String.valueOf(value / 100));
            updateHistory(currentText + " % = " + display.getText());
        }
    }

    // Method to calculate nPr
    private void calculatePermutation(int n, int r) {
        if (n < 0 || r < 0 || r > n) {
            throw new ArithmeticException("Invalid input for nPr");
        }
        long result = 1;
        for (int i = n; i > n - r; i--) {
            result *= i;
        }
        display.setText(String.valueOf(result));
        updateHistory(n + "P" + r + " = " + result);
    }

    // Method to calculate nCr
    private void calculateCombination(int n, int r) {
        if (n < 0 || r < 0 || r > n) {
            throw new ArithmeticException("Invalid input for nCr");
        }
        if (r == 0 || r == n) {
            display.setText("1");
            updateHistory(n + "C" + r + " = 1");
            return;
        }
        if (r > n / 2) {
            r = n - r;
        }
        long result = 1;
        for (int i = 1; i <= r; i++) {
            result = result * (n - i + 1) / i;
        }
        display.setText(String.valueOf(result));
        updateHistory(n + "C" + r + " = " + result);
    }

    // Method to evaluate the expression and calculate the result
    private void calculateResult() {
        String expression = display.getText();
        if (expression.isEmpty()) return;

        try {
            // Tokenize the expression
            ArrayList<String> tokens = tokenize(expression);
            // Evaluate tokens
            double result = evaluateExpression(tokens);

            display.setText(String.valueOf(result));
            updateHistory(expression + " = " + result);
        } catch (ArithmeticException e) {
            display.setText("Error");
            updateHistory("Error: " + e.getMessage());
        } catch (Exception e) {
            display.setText("Error");
            updateHistory("Error: Invalid Expression");
        }
    }
    
    // Method to tokenise the input expression
    private ArrayList<String> tokenize(String expression) {
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                currentNumber.append(c);
            } else if (c == '-' && (i == 0 || isOperator(String.valueOf(expression.charAt(i - 1))) || expression.charAt(i-1) == '(')) {
                currentNumber.append(c);
            }
            else if (isOperator(String.valueOf(c)) || c == '(' || c == ')') {
                if (currentNumber.length() > 0) {
                    tokens.add(currentNumber.toString());
                    currentNumber.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (Character.isLetter(c)) {
                 StringBuilder functionName = new StringBuilder();
                 while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                    functionName.append(expression.charAt(i));
                    i++;
                 }
                 i--;
                 tokens.add(functionName.toString());

            } else {
                if (currentNumber.length() > 0) {
                    tokens.add(currentNumber.toString());
                    currentNumber.setLength(0);
                }
            }
        }
        if (currentNumber.length() > 0) {
            tokens.add(currentNumber.toString());
        }
        return tokens;
    }

    // Method to evaluate a list of tokens
    private double evaluateExpression(ArrayList<String> tokens) {
        // 1. Evaluate functions
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            switch (token) {
                case "nPr":
                    int n = Integer.parseInt(tokens.get(i - 1));
                    int r = Integer.parseInt(tokens.get(i + 1));
                    calculatePermutation(n, r);
                    tokens.set(i-1, display.getText());
                    tokens.remove(i);
                    tokens.remove(i);
                    i--;
                    break;
                case "nCr":
                     n = Integer.parseInt(tokens.get(i - 1));
                     r = Integer.parseInt(tokens.get(i + 1));
                    calculateCombination(n, r);
                    tokens.set(i-1, display.getText());
                    tokens.remove(i);
                    tokens.remove(i);
                    i--;
                    break;
                case "Abs":
                    double value = Double.parseDouble(tokens.get(i+1));
                    tokens.set(i, String.valueOf(Math.abs(value)));
                    tokens.remove(i + 1);
                    i--;
                    break;
            }
        }

        // 2. Handle parentheses
        while (tokens.contains("(")) {
            int openParenIndex = tokens.indexOf("(");
            int closeParenIndex = findMatchingParen(tokens, openParenIndex);
            ArrayList<String> subExpression = new ArrayList<>(tokens.subList(openParenIndex + 1, closeParenIndex));
            double subResult = evaluateExpression(subExpression);
            tokens.set(openParenIndex, String.valueOf(subResult));
            tokens.subList(openParenIndex + 1, closeParenIndex + 1).clear();
        }

        // 3. Handle multiplication and division
        for (int i = 1; i < tokens.size() - 1; i++) {
            if ("*".equals(tokens.get(i)) || "/".equals(tokens.get(i))) {
                double num1 = Double.parseDouble(tokens.get(i - 1));
                double num2 = Double.parseDouble(tokens.get(i + 1));
                if ("/".equals(tokens.get(i)) && num2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                double result = "*".equals(tokens.get(i)) ? num1 * num2 : num1 / num2;
                tokens.set(i - 1, String.valueOf(result));
                tokens.remove(i);
                tokens.remove(i);
                i--;
            }
        }

        // 4. Handle addition and subtraction
        double result = Double.parseDouble(tokens.get(0));
        for (int i = 1; i < tokens.size() - 1; i += 2) {
            double num = Double.parseDouble(tokens.get(i + 1));
            if ("+".equals(tokens.get(i))) {
                result += num;
            } else if ("-".equals(tokens.get(i))) {
                result -= num;
            }
        }
        return result;
    }

    private int findMatchingParen(ArrayList<String> tokens, int openParenIndex) {
        int count = 1;
        for (int i = openParenIndex + 1; i < tokens.size(); i++) {
            if ("(".equals(tokens.get(i))) {
                count++;
            } else if (")".equals(tokens.get(i))) {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1; // Should not happen in a balanced expression
    }

    // Method to update history
    private void updateHistory(String entry) {
        history.add(entry);
        historyArea.append(entry + "\n");
        // Keep only the last 10 entries in history
        if (history.size() > 10) {
            history.remove(0);
            historyArea.setText(""); // Clear the history area
            for(String h : history){
                historyArea.append(h + "\n");
            }

        }
    }
}

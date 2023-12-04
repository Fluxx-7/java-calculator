// used for implementing GUI.
import javax.swing.*;
import java.awt.*;

// used for button listeners.
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BasicCalculator extends JFrame
{
    private double total; //returns the total value from the given input
    private JTextField inputField;

    public BasicCalculator() {

        // Exit program when calculator is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set size and layout for the border of calculator
        setSize(300, 400);
        setLayout(new BorderLayout());

        // Sets an input field above the buttons 
        inputField = new JTextField();
        add(inputField, BorderLayout.NORTH);

        // Initiates grid layout for JPanel
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4)); // Sets the grid to 5 rows and 4 columns
        String[] buttonLabels = {
                "y^x","\u215f", "\u221a", "clear", // removed "x\u00b2"
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        // For loop to create a button for each item in buttonLabels
        for (String label : buttonLabels) {
            JButton button = new JButton(label); // Creates new instance of JButton
            button.addActionListener(new ButtonClickListener()); // Adds action listen to buttons
            buttonPanel.add(button); // Creates a button for each item in the buttonLabels array
        }
        // Adds buttonPanel as the component and the contraint 
        add(buttonPanel, BorderLayout.CENTER);
    }
        
    /**
     * Method that listens to button presses and performs functions
     * depending on the button 
     * 
     * @param ActionEvent A high level event generated by the button
     */
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource(); // Finds the source of the button press
            String buttonText = source.getText(); // Finds the text of the button that was pressed

            if (buttonText.equals("=")) {
                evaluateExpression(); // Calls method to evaluate function

            } else if (buttonText.equals("clear")) {
                inputField.setText(null); // Sets the text of input field to null, acting as a clear button

            } else if (buttonText.equals("y^x")) {
                inputField.setText(inputField.getText() + "^"); // Adds an exponent symbol to the input field

            } else {
                inputField.setText(inputField.getText() + buttonText); // Displays the buttons' values on inputField
            }
        }

        private void evaluateExpression() {
            String expression = inputField.getText(); // Retrieves text from input field

            try {
                double result = evaluate(expression); // Calls method to evaluate the expression
                inputField.setText(Double.toString(result)); // Converts text from inputField to a string for display

            } catch (Exception ex) {
                inputField.setText("Error");
                System.out.print(ex);
            }
        }

        private double evaluate(String expression) {

            // Creates anonymous inner class, elimates the need for seperate method names
            return new Object() {
                int pos = -1, ch; // Sets position to -1 to signal to the code that it has not begun parsing yet
        
                // Move to the next character in the expression
                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1; // Ternary operator to check if condition 2 is true
                }
        
                // Check if the current character is a digit
                boolean isDigitChar() {
                    return Character.isDigit(ch);
                }
        
                // Parse the entire expression
                double parse() {
                    nextChar(); // Moves to the next character
                    double x = parseExpression(); // passes the expression (x) to parseExpression
                    return x;
                }
        
                // Parse an expression, handling addition and subtraction
                double parseExpression() {
                    double x = parseTerm(); // Calls parseTerm to handle operators with higher precedence

                    // While true loop so it goes on infinitely
                    while (true) {

                        if (isOperator('+')) {
                            nextChar(); // moves to next character
                            x += parseTerm(); // Performs addition

                        } else if (isOperator('-')) {
                            nextChar(); // Moves to the next character
                            x -= parseTerm(); // Performs sbtraction

                        } else {
                            return x; // Returns x if no addition or subtraction are found
                        }
                    }
                }
        
                // Parse a term, handling multiplication and division
                double parseTerm() {

                    double x = parseFactor(); // Calls parseFactor to ensure PEMDOS is being followed

                    while (true) {
                        if (isOperator('*')) {
                            nextChar(); // Moves to next character

                            x *= parseFactor(); // Performs multiplication

                        } else if (isOperator('/')) {
                            nextChar(); // Moves to next character

                            x /= parseFactor(); // Performs division

                        }
                        else if (isOperator('^')) {
                            nextChar(); // Moves to next character

                            x = Math.pow(x, parseFactor()); // Performs exponential powers

                        }else {
                            return x; // Returns x if no mult or division is found
                        }
                    }
                }
        
                // Parse a factor, handling unary plus, unary minus, parentheses, and numbers
                double parseFactor() {
                    if (isOperator('+')) {
                        nextChar();
                        return parseFactor(); 
                    }
                    if (isOperator('-')) {
                        nextChar();
                        return -parseFactor();
                    }
        
                    double x;

                    // startPos keeps track of the starting point in the expression
                    int startPos = this.pos;


                    if (isDigitChar() || ch == '.') { // Condition checks if the character is a digit (number) or a '.'
                        while (isDigitChar() || ch == '.') nextChar(); // Moves to the next character if char is a digit or '.'
                        x = Double.parseDouble(expression.substring(startPos, this.pos)); // Parses substring starting at startPos and ending at this.pos
                        
                    } else {
                        throw new RuntimeException("Unexpected Error");
                    }
                    return x; // Returns x to be further parsed
                }
        
                // Check if the current character is the expected operator
                boolean isOperator(int charToCheck) {
                    return !Character.isWhitespace(ch) && ch == charToCheck; // Checks if ch is whitespace and is used to check what operator is encountered
                }
            }.parse(); // Initiates the parse method
        }
    }

    // Allows the equals function
    public double result() 
    {
        total = double finalTotal;
        return finalTotal;
    }
    
    // Allows the calculator screen to be cleared
    public double clear()
    {
        total = 0;
        inputField = "";
    }
    
    public static void main(String[] args) {
            BasicCalculator calculator = new BasicCalculator();
            calculator.setVisible(true);
    }

}

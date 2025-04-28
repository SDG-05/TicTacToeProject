import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class View {

    private String currentPlayer;
    private String[] board;
    private String loggedInEmail;
    private SqlTask db;

    public void LoginView() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginFrame.add(panel);
        loginFrame.setVisible(true);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            try {
                db = new SqlTask();
                if (db.checkLogin(email, password)) {
                    JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                    loginFrame.dispose();
                    this.loggedInEmail = email;
                    startGame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Login failed. Check your credentials.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        registerButton.addActionListener(e -> {
            loginFrame.dispose();
            RegisterView();
        });
    }

    public void RegisterView() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(400, 400);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(7, 1));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(submitButton);

        registerFrame.add(panel);
        registerFrame.setVisible(true);

        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            try {
                db = new SqlTask();
                db.AddNewUsers(username, email, password);
                JOptionPane.showMessageDialog(registerFrame, "Registered Successfully!");
                registerFrame.dispose();
                LoginView();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void startGame() {
        try {
            board = db.loadGameState(loggedInEmail);
            currentPlayer = db.loadCurrentTurn(loggedInEmail);
        } catch (SQLException e) {
            e.printStackTrace();
            board = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
            currentPlayer = "X";
        }

        JFrame gameFrame = new JFrame("Tic-Tac-Toe");
        gameFrame.setSize(400, 400);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new GridLayout(3, 3));

        JButton[] buttons = new JButton[9];

        for (int i = 0; i < 9; i++) {
            int index = i;
            buttons[i] = new JButton(" ");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (buttons[index].getText().equals("X") || buttons[index].getText().equals("O")) {
                        return;
                    }

                    board[index] = currentPlayer;
                    buttons[index].setText(currentPlayer);

                    try {
                        db.saveGameState(loggedInEmail, board, currentPlayer);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    String winner = GameUtils.checkWinner(board);
                    if (winner != null) {
                        if (winner.equals("draw")) {
                            JOptionPane.showMessageDialog(gameFrame, "It's a draw!");
                        } else {
                            JOptionPane.showMessageDialog(gameFrame, "Congratulations! Player " + winner + " wins!");
                        }

                        try {
                            db.resetGameState(loggedInEmail);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        int option = JOptionPane.showConfirmDialog(gameFrame, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            gameFrame.dispose();
                            startGame();
                        } else {
                            gameFrame.dispose();
                            System.exit(0);
                        }
                    } else {
                        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
                    }
                }
            });
            gameFrame.add(buttons[i]);
        }

        gameFrame.setVisible(true);
    }
}

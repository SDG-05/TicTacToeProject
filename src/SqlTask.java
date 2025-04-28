import java.sql.*;

public class SqlTask {

    private final Connection connection;

    public SqlTask() throws SQLException {
        connection = DatabaseHandler.getConnection();
    }
    public boolean checkLogin(String email, String password) throws SQLException {
        Connection conn = DatabaseHandler.getConnection();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        boolean exists = rs.next();

        rs.close();
        stmt.close();
        conn.close();

        return exists;
    }


    public void AddNewUsers(String userName, String email, String password) throws SQLException {
        String query = "INSERT INTO users (name, email, password, board_state, current_turn) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, userName);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, "1,2,3,4,5,6,7,8,9");
        ps.setString(5, "X");

        int success = ps.executeUpdate();
        if (success == 1) System.out.println("User added successfully");
        else System.out.println("User already exists/Invalid");
    }


    public String[] loadGameState(String email) throws SQLException {
        String query = "SELECT board_state FROM users WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String boardString = rs.getString("board_state");
            return boardString.split(",");
        }
        return null;
    }

    public String loadCurrentTurn(String email) throws SQLException {
        String query = "SELECT current_turn FROM users WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("current_turn");
        }
        return "X";
    }

    public void saveGameState(String email, String[] board, String currentPlayer) throws SQLException {
        String query = "UPDATE users SET board_state = ?, current_turn = ? WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, GameUtils.boardToString(board));
        ps.setString(2, currentPlayer);
        ps.setString(3, email);

        ps.executeUpdate();
    }

    public void resetGameState(String email) throws SQLException {
        String query = "UPDATE users SET board_state = ?, current_turn = ? WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, "1,2,3,4,5,6,7,8,9");
        ps.setString(2, "X");
        ps.setString(3, email);

        ps.executeUpdate();
    }
}

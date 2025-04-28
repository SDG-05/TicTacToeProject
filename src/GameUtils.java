public class GameUtils {

    public static String checkWinner(String[] board) {
        String[][] winningPositions = {
                {board[0], board[1], board[2]},
                {board[3], board[4], board[5]},
                {board[6], board[7], board[8]},
                {board[0], board[3], board[6]},
                {board[1], board[4], board[7]},
                {board[2], board[5], board[8]},
                {board[0], board[4], board[8]},
                {board[2], board[4], board[6]}
        };

        for (String[] line : winningPositions) {
            if (line[0].equals(line[1]) && line[1].equals(line[2])) {
                return line[0];
            }
        }

        for (String s : board) {
            if (s.matches("\\d")) {
                return null;
            }
        }

        return "draw";
    }

    public static String boardToString(String[] board) {
        return String.join(",", board);
    }
}

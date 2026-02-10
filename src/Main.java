import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.generate();

        board.removeRandomWalls(10);

        board.setStartAndEnd();

        board.BFS();

        JFrame mainFrame = new JFrame("Labirynt - Porównanie Algorytmów");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 500);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());

        MazePanel mazePanel = new MazePanel(board);
        mainFrame.add(mazePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnBFS = new JButton("Rozwiąż BFS");
        JButton btnDFS = new JButton("Rozwiąż DFS");
        JButton btnLetters = new JButton("Zbierz Literki (A-B-C-D)");
        JButton btnNewMaze = new JButton("Nowy Labirynt");

        btnBFS.addActionListener(e -> {
            board.BFS();
            mazePanel.repaint();
        });

        btnDFS.addActionListener(e -> {
            board.DFS();
            mazePanel.repaint();
        });

        btnLetters.addActionListener(e -> {
            board.placeLetters();
            board.solveWithLetters();
            mazePanel.repaint();
        });

        btnNewMaze.addActionListener(e -> {
            board.resetBoard();
            board.generate();
            board.removeRandomWalls(10);
            board.setStartAndEnd();
            board.BFS();
            mazePanel.repaint();
        });

        buttonPanel.add(btnBFS);
        buttonPanel.add(btnDFS);
        buttonPanel.add(btnLetters);
        buttonPanel.add(btnNewMaze);

        mainFrame.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }
}
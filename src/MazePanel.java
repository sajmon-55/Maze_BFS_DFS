import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MazePanel extends JPanel {
    private final Board board;
    public MazePanel(Board board) {
        this.board = board;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Field[][] fields = board.getFields();
        int cellSize = 35;

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                Field currentField = fields[i][j];

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.black);

                if(currentField.isTopWallStanding())
                    g.drawLine(x, y, x+cellSize, y);
                if(currentField.isLeftWallStanding())
                    g.drawLine(x, y, x, y+cellSize);

                if (j == 9 && currentField.isRightWallStanding())
                    g.drawLine(x+cellSize, y, x+cellSize, y+cellSize);
                if (i == 9 && currentField.isBottomWallStanding())
                    g.drawLine(x, y+cellSize, x+cellSize, y+cellSize);
            }
        }


        g.setColor(Color.black);
        g.drawRect(0, 0, 10*cellSize, 10*cellSize);

        List<Node> fullPath = board.getFullPath();
        Node[][] parents = board.getParents();
        int currX = board.getEndX();
        int currY = board.getEndY();

        if(parents != null) {
            g.setColor(Color.BLUE);

            while (currX != board.getStartX() || currY != board.getStartY()) {
                Node parent = parents[currY][currX];
                if (parent == null) {
                    break;
                }

                int nextX = parent.getX();
                int nextY = parent.getY();
                g.drawLine(currX*cellSize + 17, currY*cellSize + 17,
                        nextX*cellSize + 17, nextY*cellSize + 17);

                currX = nextX;
                currY = nextY;
            }
        } else if (!fullPath.isEmpty()) {
            g.setColor(new Color(255, 111, 0, 255));
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            for (int i = 0; i < fullPath.size() - 1; i++) {
                Node current = fullPath.get(i);
                Node next = fullPath.get(i + 1);

                g.drawLine(
                        current.getX()*cellSize + 17,
                        current.getY()*cellSize + 17,
                        next.getX()*cellSize + 17,
                        next.getY()*cellSize + 17
                );
            }
        }
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                Field currentField = fields[i][j];
                if(currentField.getType() == Field.FieldType.START){
                    g.setColor(Color.GREEN);
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.drawString("S", x + 10, y + 25);
                } else if (currentField.getType() == Field.FieldType.KONIEC) {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.drawString("K", x + 10, y + 25);
                } else if(currentField.getType() == Field.FieldType.LITERA_A) {
                    g.setColor(Color.MAGENTA);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString("A", x + 12, y + 25);
                } else if(currentField.getType() == Field.FieldType.LITERA_B) {
                    g.setColor(Color.MAGENTA);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString("B", x + 12, y + 25);
                } else if(currentField.getType() == Field.FieldType.LITERA_C) {
                    g.setColor(Color.MAGENTA);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString("C", x + 12, y + 25);
                } else if(currentField.getType() == Field.FieldType.LITERA_D) {
                    g.setColor(Color.MAGENTA);
                    g.setFont(new Font("Arial", Font.BOLD, 18));
                    g.drawString("D", x + 12, y + 25);
                }
            }
        }
    }
}

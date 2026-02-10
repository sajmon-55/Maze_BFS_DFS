import java.util.*;

public class Board {
    private Field[][] fields;
    private Stack stack;
    private int startX, startY;
    private int endX, endY;
    private Node[][] parents;
    private int ax, ay, bx, by, cx, cy, dx, dy;
    private List<Node> fullPath = new ArrayList<>();

    public Board() {
        resetBoard();
    }

    public void setStartAndEnd(){
        Random rand = new Random();
        this.startX = 0;
        this.startY = rand.nextInt(10);
        fields[startY][startX].setType(Field.FieldType.START);

        this.endX = 9;
        this.endY = rand.nextInt(10);
        fields[endY][endX].setType(Field.FieldType.KONIEC);
    }

    public void placeLetters(){
        System.out.println("   [GENERATOR]: Losowanie nowych pozycji dla liter A, B, C, D...");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Field.FieldType type = fields[i][j].getType();
                if (type == Field.FieldType.LITERA_A || type == Field.FieldType.LITERA_B ||
                        type == Field.FieldType.LITERA_C || type == Field.FieldType.LITERA_D) {
                    fields[i][j].setType(Field.FieldType.PUSTE);
                }
            }
        }

        Random rand = new Random();
        Field.FieldType[] letterFields = {
            Field.FieldType.LITERA_A,
            Field.FieldType.LITERA_B,
            Field.FieldType.LITERA_C,
            Field.FieldType.LITERA_D
        };

        for (int i = 0; i < letterFields.length; i++) {
            int rX, rY;
            do {
                rX = rand.nextInt(10);
                rY = rand.nextInt(10);
            } while (fields[rY][rX].getType() != Field.FieldType.PUSTE);

            fields[rY][rX].setType(letterFields[i]);

            if (i == 0) {
                ax = rX; ay = rY;
            } else if (i == 1) {
                bx = rX; by = rY;
            } else if (i == 2) {
                cx = rX; cy = rY;
            } else if (i == 3) {
                dx = rX; dy = rY;
            }
        }
    }
    public List<Node> getPathFragment(int sx, int sy, int ex, int ey, String cel) {
        System.out.println(">>> Szukam drogi z (" + sx + "," + sy + ") do " + cel + " (" + ex + "," + ey + ")");

        if (ex == 0 && ey == 0 && sx != 0) {
            System.out.println("BŁĄD KRYTYCZNY! Współrzędne celu " + cel + " to (0,0). Czy uruchomiłeś placeLetters()?");
            return new ArrayList<>();
        }

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) fields[i][j].setChecked(false);

        Queue queue = new Queue();
        Node[][] localParents = new Node[10][10];

        queue.enqueue(sx, sy);
        fields[sy][sx].setChecked(true);

        boolean found = false;
        while (!queue.isEmpty()) {
            Node current = queue.dequeue();
            int cx = current.getX();
            int cy = current.getY();

            if (cx == ex && cy == ey) {
                found = true;
                break;
            }

            if(cy > 0 && !fields[cy][cx].isTopWallStanding() && !fields[cy-1][cx].isChecked()) {
                queue.enqueue(cx, cy-1);
                fields[cy-1][cx].setChecked(true);
                localParents[cy-1][cx] = current;
            }
            if (cy < 9 && !fields[cy][cx].isBottomWallStanding() && !fields[cy+1][cx].isChecked()) {
                queue.enqueue(cx, cy+1);
                fields[cy+1][cx].setChecked(true);
                localParents[cy+1][cx] = current;
            }
            if (cx > 0 && !fields[cy][cx].isLeftWallStanding() && !fields[cy][cx-1].isChecked()) {
                queue.enqueue(cx-1, cy);
                fields[cy][cx-1].setChecked(true);
                localParents[cy][cx-1] = current;
            }
            if (cx < 9 && !fields[cy][cx].isRightWallStanding() && !fields[cy][cx+1].isChecked()) {
                queue.enqueue(cx+1, cy);
                fields[cy][cx+1].setChecked(true);
                localParents[cy][cx+1] = current;
            }
        }

        List<Node> pathFragment = new ArrayList<>();
        if (found) {
            System.out.println("    -> Sukces! Znaleziono drogę do " + cel);
            int currX = ex;
            int currY = ey;
            while (currX != sx || currY != sy) {
                pathFragment.add(0, new Node(currX, currY, null));
                Node p = localParents[currY][currX];
                if (p == null) break;
                currX = p.getX();
                currY = p.getY();
            }
            pathFragment.add(0, new Node(sx, sy, null));
        } else {
            System.out.println("    -> BŁĄD! Nie znaleziono drogi do " + cel);
        }
        return pathFragment;
    }

    public void solveWithLetters() {
        fullPath.clear();
        System.out.println("\n--- [DIAGNOSTYKA LITERKI] ---");
        System.out.println("   [MISJA]: Start -> A -> B -> C -> D -> Koniec");
        System.out.println("   [POZYCJE]: A(" + ax + "," + ay + ") B(" + bx + "," + by + ") C(" + cx + "," + cy + ") D(" + dx + "," + dy + ")");

        runStage("Start -> A", startX, startY, ax, ay, "A");
        runStage("A -> B", ax, ay, bx, by, "B");
        runStage("B -> C", bx, by, cx, cy, "C");
        runStage("C -> D", cx, cy, dx, dy, "D");
        runStage("D -> Koniec", dx, dy, endX, endY, "KONIEC");

        System.out.println("   [KONIEC MISJI]: Całkowita długość trasy: " + fullPath.size() + " kroków.");
        parents = null;
    }

    private void runStage(String stageName, int sx, int sy, int ex, int ey, String targetLabel) {
        System.out.print("   [ETAP " + stageName + "]: ");
        List<Node> fragment = getPathFragment(sx, sy, ex, ey, targetLabel);

        if (!fragment.isEmpty()) {
            System.out.println("OK! (Długość: " + fragment.size() + ")");
            if (!fullPath.isEmpty() && !fragment.isEmpty()) fragment.remove(0);
            fullPath.addAll(fragment);
        } else {
            System.out.println("BŁĄD! Nie znaleziono przejścia.");
        }
    }

    public void BFS() {
        System.out.println("\n--- [DIAGNOSTYKA BFS] ---");
        System.out.println("   [START]: Szukam najkrótszej drogi z (" + startX + "," + startY + ") do (" + endX + "," + endY + ")");

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) fields[i][j].setChecked(false);

        parents = new Node[10][10];
        Queue queue = new Queue();

        queue.enqueue(startX, startY);
        fields[startY][startX].setChecked(true);

        int visitedCounter = 0;
        boolean found = false;

        while (!queue.isEmpty()) {
            Node current = (Node) queue.dequeue();
            visitedCounter++;

            int cx = current.getX();
            int cy = current.getY();

            if (cx == endX && cy == endY) {
                found = true;
                break;
            }

            if(cy > 0 && !fields[cy][cx].isTopWallStanding() && !fields[cy-1][cx].isChecked()) {
                queue.enqueue(cx, cy-1);
                fields[cy-1][cx].setChecked(true);
                parents[cy-1][cx] = current;
            }
            if (cy < 9 && !fields[cy][cx].isBottomWallStanding() && !fields[cy+1][cx].isChecked()) {
                queue.enqueue(cx, cy+1);
                fields[cy+1][cx].setChecked(true);
                parents[cy+1][cx] = current;
            }
            if (cx > 0 && !fields[cy][cx].isLeftWallStanding() && !fields[cy][cx-1].isChecked()) {
                queue.enqueue(cx-1, cy);
                fields[cy][cx-1].setChecked(true);
                parents[cy][cx-1] = current;
            }
            if (cx < 9 && !fields[cy][cx].isRightWallStanding() && !fields[cy][cx+1].isChecked()) {
                queue.enqueue(cx+1, cy);
                fields[cy][cx+1].setChecked(true);
                parents[cy][cx+1] = current;
            }
        }

        if (found) {
            printPathTrace("BFS", visitedCounter);
        } else {
            System.out.println("   [BFS] Porażka. Nie znaleziono drogi (sprawdzono " + visitedCounter + " pól).");
        }
    }
    public void DFS() {
        System.out.println("\n--- [DIAGNOSTYKA DFS] ---");
        System.out.println("   [START]: Szukam drogi w głąb z (" + startX + "," + startY + ") do (" + endX + "," + endY + ")");

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) fields[i][j].setChecked(false);

        parents = new Node[10][10];
        Stack stack = new Stack();

        stack.push(startX, startY);
        fields[startY][startX].setChecked(true);

        int visitedCounter = 0;
        boolean found = false;

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            visitedCounter++;

            int cx = current.getX();
            int cy = current.getY();

            if (cx == endX && cy == endY) {
                found = true;
                break;
            }

            if(cy > 0 && !fields[cy][cx].isTopWallStanding() && !fields[cy-1][cx].isChecked()) {
                stack.push(cx, cy-1);
                fields[cy-1][cx].setChecked(true);
                parents[cy-1][cx] = current;
            }
            if (cy < 9 && !fields[cy][cx].isBottomWallStanding() && !fields[cy+1][cx].isChecked()) {
                stack.push(cx, cy+1);
                fields[cy+1][cx].setChecked(true);
                parents[cy+1][cx] = current;
            }
            if (cx > 0 && !fields[cy][cx].isLeftWallStanding() && !fields[cy][cx-1].isChecked()) {
                stack.push(cx-1, cy);
                fields[cy][cx-1].setChecked(true);
                parents[cy][cx-1] = current;
            }
            if (cx < 9 && !fields[cy][cx].isRightWallStanding() && !fields[cy][cx+1].isChecked()) {
                stack.push(cx+1, cy);
                fields[cy][cx+1].setChecked(true);
                parents[cy][cx+1] = current;
            }
        }

        if (found) {
            printPathTrace("DFS", visitedCounter);
        } else {
            System.out.println("   [DFS] Porażka. Stos pusty, brak drogi.");
        }
    }
    public List<Node> getUnvisitedNeighbors(int x, int y) {
        List<Node> list = new ArrayList<>();

        if(y - 1 >= 0 && !fields[y-1][x].isChecked()){
            list.add(new Node(x, y - 1, null));
        }
        if(y + 1 < fields.length && !fields[y+1][x].isChecked()){
            list.add(new Node(x, y + 1, null));
        }
        if(x + 1 < fields.length && !fields[y][x+1].isChecked()){
            list.add(new Node(x + 1, y, null));
        }
        if(x - 1 >= 0 && !fields[y][x-1].isChecked()){
            list.add(new Node(x - 1, y, null));
        }
        return list;
    }

    public void generate() {
        resetBoard();
        if (fullPath != null) fullPath.clear();
        parents = null;

        this.stack = new Stack();
        fields[0][0].setChecked(true);
        stack.push(0,0);

        while (!stack.isEmpty()) {
            Node current = stack.peek();
            List<Node> list = getUnvisitedNeighbors(current.getX(), current.getY());

            if (!list.isEmpty()) {
                Node neighbor = list.get((int) (Math.random() * list.size()));

                int dx = neighbor.getX() - current.getX();
                int dy = neighbor.getY() - current.getY();


                int cx = current.getX();
                int cy = current.getY();
                int nx = neighbor.getX();
                int ny = neighbor.getY();

                if (dx == 1){
                    fields[cy][cx].removeRightWall();
                    fields[ny][nx].removeLeftWall();
                } else if (dx == -1){
                    fields[cy][cx].removeLeftWall();
                    fields[ny][nx].removeRightWall();
                } else if (dy == 1){
                    fields[cy][cx].removeBottomWall();
                    fields[ny][nx].removeTopWall();
                } else if (dy == -1){
                    fields[cy][cx].removeTopWall();
                    fields[ny][nx].removeBottomWall();
                }

                fields[ny][nx].setChecked(true);
                stack.push(nx, ny);
            } else {
                stack.pop();
            }
        }
    }

    public void print() {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                System.out.print("+");
                if (fields[i][j].isTopWallStanding())
                    System.out.print("---");
                else
                    System.out.print("   ");
            }
            System.out.println("+");
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j].isLeftWallStanding())
                    System.out.print("|   ");
                else
                    System.out.print("    ");
            }
            System.out.println("|");
        }
        for (int i = 0; i < fields.length; i++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }

    private void printPathTrace(String algoName, int stepsVisited) {
        System.out.println("   [" + algoName + "] ZAKOŃCZONO SUKCESEM!");
        System.out.println("   [" + algoName + "] Odwiedzono łącznie pól: " + stepsVisited);
        System.out.println("   [" + algoName + "] Odtwarzanie ścieżki (Backtracking):");

        int cx = endX;
        int cy = endY;
        int pathLength = 0;

        if (parents[cy][cx] == null && (cx != startX || cy != startY)) {
            System.out.println("   [BŁĄD] Ścieżka przerwana! Nie można odtworzyć trasy.");
            return;
        }

        String pathLog = "";
        while (cx != startX || cy != startY) {
            Node p = parents[cy][cx];
            if (p == null) break;

            pathLog = " -> (" + cx + "," + cy + ")" + pathLog;

            cx = p.getX();
            cy = p.getY();
            pathLength++;
        }
        pathLog = "(" + startX + "," + startY + ")" + pathLog;

        System.out.println("   [TRASA]: " + pathLog);
        System.out.println("   [DŁUGOŚĆ TRASY]: " + pathLength + " kroków.");
    }

    public void removeRandomWalls(int count) {
        Random rand = new Random();
        int removed = 0;

        while (removed < count) {
            int y = rand.nextInt(8) + 1;
            int x = rand.nextInt(8) + 1;

            int direction = rand.nextInt(4);

            boolean wallRemoved = false;

            if (direction == 0 && fields[y][x].isTopWallStanding()) {
                fields[y][x].removeTopWall();
                fields[y-1][x].removeBottomWall();
                wallRemoved = true;
            } else if (direction == 1 && fields[y][x].isBottomWallStanding()) {
                fields[y][x].removeBottomWall();
                fields[y+1][x].removeTopWall();
                wallRemoved = true;
            } else if (direction == 2 && fields[y][x].isLeftWallStanding()) {
                fields[y][x].removeLeftWall();
                fields[y][x-1].removeRightWall();
                wallRemoved = true;
            } else if (direction == 3 && fields[y][x].isRightWallStanding()) {
                fields[y][x].removeRightWall();
                fields[y][x+1].removeLeftWall();
                wallRemoved = true;
            }

            if (wallRemoved) {
                removed++;
            }
        }
    }

    public Field[][] getFields() {
        return fields;
    }

    public void setFields(Field[][] fields) {
        this.fields = fields;
    }

    public Stack getStack() {
        return stack;
    }

    public Node[][] getParents() {
        return parents;
    }
    public void resetBoard() {
        this.fields = new Field[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                fields[i][j] = new Field();
            }
        }
    }
    public int getEndX() {
        return endX;
    }
    public int getEndY() {
        return endY;
    }
    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }

    public List<Node> getFullPath() {
        return fullPath;
    }
}
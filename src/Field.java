public class Field {
    public enum FieldType {
        START,KONIEC,PUSTE,LITERA_A, LITERA_B, LITERA_C, LITERA_D,
    }
    private boolean top;
    private boolean bottom;
    private boolean left;
    private boolean right;
    private FieldType type;
    private boolean checked;

    public Field() {
        top = true;
        bottom = true;
        left = true;
        right = true;
        type = FieldType.PUSTE;
        checked = false;
    }

    public FieldType getType() {
        return type;
    }
    public void setType(FieldType type) {
        this.type = type;
    }
    public boolean isTopWallStanding() {
        return top;
    }
    public void removeTopWall() {
        this.top = false;
    }
    public boolean isBottomWallStanding() {
        return bottom;
    }
    public void removeBottomWall() {
        this.bottom = false;
    }
    public boolean isLeftWallStanding() {
        return left;
    }
    public void removeLeftWall() {
        this.left = false;
    }
    public boolean isRightWallStanding() {
        return right;
    }
    public void removeRightWall() {
        this.right = false;
    }
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

package marsrover.entity;

public enum Heading {

    NORTH(0, 'N', 0),
    EAST(1, 'E', 270),
    SOUTH(2, 'S', 180),
    WEST(3, 'W', 90);

    public final int id;
    public final char character;
    public final int degreesFromNorth;

    Heading(int id, char character, int degreesFromNorth) {
        this.id = id;
        this.character = character;
        this.degreesFromNorth = degreesFromNorth;
    }

    public Heading getHeadingRight() {
        if (this.id == 3) return NORTH;
        else return getByID(this.id + 1);
    }

    public Heading getHeadingLeft() {
        if (this.id == 0) return WEST;
        else return getByID(this.id - 1);
    }

    public Heading getByID(int id) {
        for (Heading heading : Heading.values()) if (heading.id == id) return heading; return null;
    }

}

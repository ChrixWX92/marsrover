package marsrover.terrain;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import marsrover.entity.Entity;
import marsrover.model.models.GridModel;
import marsrover.model.models.Model;
import marsrover.model.Buildable;
import marsrover.model.Xform;

import java.util.Arrays;

public class GridSquare {

    private boolean occupied = false;
    private Entity occupant;
    int[] coordinates;
    double size;
    GridModel model;
    private boolean rendered = false;

    protected GridSquare(int x, int z, double size) {
        this.coordinates = new int[]{x, z};
        this.size = size;
    }

    public void render() {
        if (rendered) System.out.println("Re-rendering GridSquare @ " + Arrays.toString(coordinates) + ".");
        else {
            this.model = new GridModel(0, this.size);
            this.model.build();
        }
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Entity getOccupant() {
        return occupant;
    }

    public void setOccupant(Entity occupant) {
        this.occupant = occupant;
    }
}

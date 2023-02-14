package marsrover;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Grid extends Model implements Buildable {

    private boolean occupied = false;
    private Model occupant;
    int[] coordinates;
    double size;

    protected Grid(double baseY, int x, int z, double size) {
        super(baseY);
        this.coordinates = new int[]{x, z};
        this.size = size;
    }

    @Override
    public Xform build() {
        Box surface = new Box(this.size, 1, this.size);
        final PhongMaterial surfaceMaterial = new PhongMaterial();
        surfaceMaterial.setDiffuseMap(new Image("file:src/main/resources/Mars CH16.png"));
        surfaceMaterial.setSpecularColor(Color.MOCCASIN);
        surface.setMaterial(surfaceMaterial);
        Xform xform = new Xform();
        xform.getChildren().add(surface);
        this.xform = xform;
        return this.xform;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Model getOccupant() {
        return occupant;
    }

    public void setOccupant(Model occupant) {
        this.occupant = occupant;
    }
}

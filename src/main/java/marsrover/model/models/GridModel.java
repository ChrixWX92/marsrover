package marsrover.model.models;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import marsrover.model.Xform;

import java.awt.event.MouseEvent;

public class GridModel extends Model {

    double size;

    public GridModel(double baseY, double size) {
        super(baseY);
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

}

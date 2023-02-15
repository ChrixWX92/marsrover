package marsrover.model.models;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import marsrover.model.Xform;
import org.fxyz3d.shapes.Torus;

public class RoverModel extends Model {

    double wheelRotationSpeed;

    public RoverModel() {
        super(10, true);
        this.wheelRotationSpeed = -10;
    }

    @Override
    public Xform build() {
        Xform roverGroup =  new Xform();

        Xform roverBodyXform =  new Xform();
        Xform[] roverWheelXforms = new Xform[] {new Xform(), new Xform(), new Xform()};

        // Rover

        // Wheels
        final PhongMaterial wheelMaterial = new PhongMaterial();
        wheelMaterial.setDiffuseMap(new Image("file:src/main/resources/img_4.png"));
        final PhongMaterial tyreMaterial = new PhongMaterial();
        tyreMaterial.setDiffuseMap(new Image("file:src/main/resources/img_5.png"));

        for (int i = 0; i < 6 ; i++) {
            Cylinder wheel = new Cylinder(7, 6);
            wheel.setRotationAxis(Rotate.X_AXIS);
            wheel.setRotate(90);
            wheel.setTranslateX(-27 + ((i % 3) * 27));
            wheel.setTranslateY(this.baseY);
            wheel.setTranslateZ(i < 3 ? -31.5 : 31.5);
            wheel.setMaterial(wheelMaterial);
            roverWheelXforms[i % 3].getChildren().add(wheel);

            Torus tyre = new Torus(7, 3);
            tyre.setTranslateX(-27 + ((i % 3) * 27));
            tyre.setTranslateY(this.baseY);
            tyre.setTranslateZ(i < 3 ? -31.5 : 31.5);
            tyre.setMaterial(tyreMaterial);
            roverWheelXforms[i % 3].getChildren().add(tyre);
        }

        // Wheel Assembly

        final PhongMaterial waMaterial = new PhongMaterial();
        waMaterial.setDiffuseMap(new Image("file:src/main/resources/img_2.png"));

        for (int i = 0; i < 2 ; i++) {

            int inst = i == 0 ? 1 : -1;

            Cylinder wheelStrut = new Cylinder(1.9, 18);
            wheelStrut.setRotationAxis(Rotate.X_AXIS);
            wheelStrut.setRotate(130 * inst);
            wheelStrut.setTranslateY(this.baseY + 7);
            wheelStrut.setTranslateZ(16.5 * inst);
            wheelStrut.setMaterial(waMaterial);
            roverBodyXform.getChildren().add(wheelStrut);

            Cylinder wheelBar = new Cylinder(1.7, 54);
            wheelBar.setRotationAxis(Rotate.Z_AXIS);
            wheelBar.setRotate(90);
            wheelBar.setTranslateY(this.baseY);
            wheelBar.setTranslateZ(25.5 * inst);
            wheelBar.setMaterial(waMaterial);
            roverBodyXform.getChildren().add(wheelBar);

            Sphere junction = new Sphere(3);
            junction.setTranslateY(this.baseY);
            junction.setTranslateZ(25 * inst);
            junction.setMaterial(waMaterial);
            roverBodyXform.getChildren().add(junction);

            for (int j = 0; j < 3 ; j++) {
                Cylinder axle = new Cylinder(1.7, 5);
                axle.setRotationAxis(Rotate.X_AXIS);
                axle.setRotate(90);
                axle.setTranslateX(-27 + (j * 27));
                axle.setTranslateY(this.baseY);
                axle.setTranslateZ(28 * inst);
                axle.setMaterial(waMaterial);
                roverBodyXform.getChildren().add(axle);
            }

            for (int j = 0; j < 2 ; j++) {
                Sphere subJunction = new Sphere(2);
                subJunction.setTranslateX(j == 0 ? 27 : -27);
                subJunction.setTranslateY(this.baseY);
                subJunction.setTranslateZ(25 * inst);
                roverBodyXform.getChildren().add(subJunction);
            }

        }

        Box body = new Box(40, 20, 30);
        body.setTranslateY(20);
        final PhongMaterial bodyMaterial = new PhongMaterial();
        bodyMaterial.setDiffuseMap(new Image("file:src/main/resources/img.png"));
        body.setMaterial(bodyMaterial);

        Cylinder antenna = new Cylinder(1, 50);
        antenna.setTranslateX(-10);
        antenna.setTranslateY(this.baseY + 35);
        antenna.setTranslateZ(-5);
        antenna.setMaterial(waMaterial);

        Cylinder antenna2 = new Cylinder(0.5, 20);
        antenna2.setTranslateX(-5);
        antenna2.setTranslateY(this.baseY + 30);
        antenna2.setTranslateZ(6);
        antenna2.setMaterial(waMaterial);

        Cylinder neck = new Cylinder(1.75, 30);
        neck.setTranslateX(5);
        neck.setTranslateY(this.baseY + 25);
        neck.setMaterial(waMaterial);

        Box head = new Box(8, 9, 15);
        head.setTranslateX(10);
        head.setTranslateY(this.baseY + 40);
        head.setRotationAxis(Rotate.Y_AXIS);
        head.setRotate(90);
        final PhongMaterial headMaterial = new PhongMaterial();
        headMaterial.setDiffuseMap(new Image("file:src/main/resources/img_6.png"));
        head.setMaterial(headMaterial);

        Cylinder eye = new Cylinder(3, 3);
        eye.setTranslateX(18.5);
        eye.setTranslateY(this.baseY + 40);
        eye.setRotationAxis(Rotate.Z_AXIS);
        eye.setRotate(90);
        final PhongMaterial eyeMaterial = new PhongMaterial();
        eyeMaterial.setDiffuseColor(Color.BLACK);
        eyeMaterial.setSpecularColor(Color.DARKBLUE);
        eye.setMaterial(eyeMaterial);

        roverBodyXform.getChildren().addAll(body, eye, head, neck, antenna, antenna2);

        roverGroup.getChildren().add(roverBodyXform);
        roverGroup.getChildren().addAll(roverWheelXforms);

        roverGroup.setRotationAxis(Rotate.Y_AXIS);

        roverGroup.setOnMouseClicked( e -> {/*TODO: SET ACTIVE ROVER*/} );

        this.xform = roverGroup;

        this.built = true;

        return roverGroup;
    }

}

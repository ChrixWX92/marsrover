package marsrover;

import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import marsrover.controllers.Controller;
import marsrover.entity.entities.Rover;
import marsrover.terrain.Plateau;

public class Stage {

    private Scene scene;

    private final Group group;
    private Camera camera;
    private World world;

    public Stage() {
        this.group = new Group();
        group.setDepthTest(DepthTest.ENABLE);
    }

    public void addWorld(World world) {
        this.world = world;
        this.group.getChildren().add(world.xform);
    }

    public void addCamera(Camera camera) {
        this.camera = camera;
        this.group.getChildren().add(camera.getXforms()[0]);
    }

    public void buildLight() {
        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(90000);
        pointLight.setTranslateY(90000);
        pointLight.setTranslateZ(90000);
        this.world.xform.getChildren().addAll(pointLight);
    }

    public void generateScene() {
        // Only Mars available right now
        this.scene = new Scene(group, 1024, 768, true);
        scene.setFill(      new ImagePattern(
                new Image("file:src/main/resources/stars.jpg_large"), 0, 0, 1, 1, true
        ));
    }

    public void showPrimaryStage(javafx.stage.Stage primaryStage) {
        primaryStage.setTitle("Mars Rover");
        primaryStage.setScene(scene);

        primaryStage.show();

        scene.setCamera(camera.camera);
    }

    public void engageControl(int x, int z, int roverX, int roverZ, int facing) {
        world.setTerrain(new Plateau(x, z));
        Rover initialRover = new Rover(new int[]{roverX,roverZ}, facing);
        world.addEntity(initialRover);
        world.setActiveMovable(initialRover);
        Controller controller =  new Controller(this);
        controller.engagePeripheralHandlers();
    }

    public Scene getScene() {
        return scene;
    }

    public Camera getCamera() {
        return camera;
    }


    public Group getGroup() {
        return group;
    }

    public World getWorld() {
        return world;
    }

}

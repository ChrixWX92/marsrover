package marsrover;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application {

    final Group root = new Group();
    final Xform axisGroup = new Xform();

    final Xform world = new Xform();

    Plateau plateau;
    Rover rover;
    final PerspectiveCamera camera = new PerspectiveCamera(true);

    public static int[][] rootCoordinates;

    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    double roverRotation = 0;


    private void buildCamera() {
        System.out.println("buildCamera()");
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 1.0;

            if (me.isControlDown()) {
                modifier = CONTROL_MULTIPLIER;
            }
            if (me.isShiftDown()) {
                modifier = SHIFT_MULTIPLIER;
            }
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
            }
            else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                camera.setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
            }
        });
    }

    private void handleKeyboard(Scene scene, final Node root) {
        Xform roverGroup = this.rover.xform;
        scene.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                            synchronized (this) {
                                try {
                                    rover.move(Movement.MovementType.FORWARD, Plateau.GRID_SIZE);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        break;
                    case A:
                        try {
                            rover.move(Movement.MovementType.TURN_LEFT, 90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case S:
                        synchronized (this) {
                            try {
                                rover.move(Movement.MovementType.BACKWARD, Plateau.GRID_SIZE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case D:
                        try {
                            rover.move(Movement.MovementType.TURN_RIGHT, 90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case N:
                        roverRotation = roverRotation + 90;
                        roverGroup.setRotateY(roverRotation);
                        break;
                    case M:
                        roverRotation = roverRotation - 90;
                        roverGroup.setRotateY(roverRotation);
                        break;

                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                }
            }
        });
    }

    private Plateau buildPlateau(int x, int z) {
        Plateau plateau = new Plateau(5, 5);
        world.getChildren().add(plateau.build());
        return plateau;    }

    private Rover buildRover(Terrain terrain) {
        Rover rover = new Rover();
        rover.setSurface(terrain);
        world.getChildren().addAll(rover.build());
        return rover;
    }

    private void buildLight() {
        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(90000);
        pointLight.setTranslateY(90000);
        pointLight.setTranslateZ(90000);
        world.getChildren().addAll(pointLight);
    }

    private void menu () {

        WelcomePane frame = new WelcomePane();
        frame.setTitle("Generate Plateau");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBounds(500, 100, 500, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }

    @Override
    public void start(Stage primaryStage) {

        System.out.println("start()");

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        // buildScene();
        buildCamera();
        buildAxes();
//        this.plateau = buildPlateau(rootCoordinates[0][0], rootCoordinates[0][1]);
        this.rover = buildRover(this.plateau);
        buildLight();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(      new ImagePattern(
            new Image("file:src/main/resources/stars.jpg_large"), 0, 0, 1, 1, true
        ));



        handleKeyboard(scene, world);
        handleMouse(scene, world);

        primaryStage.setTitle("Mars Rover");
        primaryStage.setScene(scene);

        primaryStage.show();

        scene.setCamera(camera);

        // TODO: Menu should be repositioned and all the above should be placed in an execute method
        // TODO: This method will either be called by a terminal listener or Generate Plateau

        menu();

    }

    public void generatePlateau() {

    }

    public static void main(String[] args) {
        launch(args);
    }

}

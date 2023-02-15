package marsrover;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import marsrover.entity.Movable;
import marsrover.model.Movement;
import marsrover.model.Xform;
import marsrover.terrain.Plateau;

public class Stage {

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

    private Scene scene;

    private final Group group;

    Camera camera;
    World world;

    public Stage() {
        this.group = new Group();
        group.setDepthTest(DepthTest.ENABLE);
    }

    private void handleMouse() {
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
                camera.xforms[0].ry.setAngle(camera.xforms[0].ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                camera.xforms[0].rx.setAngle(camera.xforms[0].rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
            }
            else if (me.isSecondaryButtonDown()) {
                double z = camera.getPerspectiveCamera().getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                camera.getPerspectiveCamera().setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                camera.xforms[1].t.setX(camera.xforms[1].t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                camera.xforms[1].t.setY(camera.xforms[1].t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
            }
        });
    }

    private void handleKeyboard() {
        Movable subject = world.getActiveMovable();
        scene.setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        synchronized (this) {
                            try {
                                subject.move(Movement.MovementType.FORWARD, world.getMovementQuantum());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case A:
                        try {
                            subject.move(Movement.MovementType.TURN_LEFT, 90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case S:
                        synchronized (this) {
                            try {
                                subject.move(Movement.MovementType.BACKWARD, world.getMovementQuantum());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case D:
                        try {
                            subject.move(Movement.MovementType.TURN_RIGHT, 90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Z:
                        camera.reset();
                        break;
                    case X:
//                        axisGroup.setVisible(!axisGroup.isVisible()); TODO: Enable axes
                        break;
                }
            }
        });
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

    public void engagePeripheralHandlers() {
        this.handleKeyboard();
        this.handleMouse();
    }

    public void showPrimaryStage(javafx.stage.Stage primaryStage) {
        primaryStage.setTitle("Mars Rover");
        primaryStage.setScene(scene);

        primaryStage.show();

        scene.setCamera(camera.camera);
    }

    public Group getGroup() {
        return group;
    }

}

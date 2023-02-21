package marsrover.controllers;

import javafx.scene.Scene;
import marsrover.Camera;
import marsrover.Stage;
import marsrover.World;
import marsrover.entity.Movable;
import marsrover.model.Actionable;
import marsrover.model.Movement;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Controller {

    ExecutorService actionExecutor = Executors.newSingleThreadExecutor();
    Future<?> futureAction;

    private Stage stage;
    private Scene scene;
    private World world;

    private Camera camera;

    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 3.3;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    public Controller(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
        this.world = stage.getWorld();
        this.camera = stage.getCamera();
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
                camera.getXforms()[0].ry.setAngle(camera.getXforms()[0].ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                camera.getXforms()[0].rx.setAngle(camera.getXforms()[0].rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
            }
            else if (me.isSecondaryButtonDown()) {
                double z = camera.getPerspectiveCamera().getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier*7;
                camera.getPerspectiveCamera().setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                camera.getXforms()[1].t.setX(camera.getXforms()[1].t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                camera.getXforms()[1].t.setY(camera.getXforms()[1].t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
            }
        });
    }

    private void handleKeyboard() {

        Movable subject = world.getActiveMovable();
        scene.setOnKeyPressed(event -> {
            Movement.MovementType movementType = switch (event.getCode()) {
                case L, A, LEFT -> Movement.MovementType.TURN_LEFT;
                case R, D, RIGHT -> Movement.MovementType.TURN_RIGHT;
                case M, W, UP -> Movement.MovementType.FORWARD;
                case B, S, DOWN -> Movement.MovementType.BACKWARD;
//                    case Z -> camera.reset(); //camera.snapToEntity((Entity) world.getActiveMovable());
                case X -> null;//axisGroup.setVisible(!axisGroup.isVisible()); TODO: Enable axes
                default -> null;
            };

            subject.move(movementType, world.getMovementQuantum());

            System.out.println(Actionable.actionDeque.size());

            // TODO: actionDeque is only polled on key press, rather than whenever it isn't empty
            if (futureAction == null || futureAction.isDone()) futureAction = actionExecutor.submit(Objects.requireNonNull(Actionable.actionDeque.poll()));

        });

    }

    public void engagePeripheralHandlers() {
        this.handleKeyboard();
        this.handleMouse();
    }

}

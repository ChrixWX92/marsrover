package marsrover.model;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import marsrover.entity.Entity;
import marsrover.entity.entities.Rover;

import java.util.Arrays;

import static marsrover.entity.Heading.EAST;
import static marsrover.entity.Heading.NORTH;
import static marsrover.model.Movement.MovementType.*;

public class Movement implements Runnable {

    MovementType type;
    Entity entity;
    Xform xform;
    double distance;
    double speed;
    Duration duration;
    Transition transition;
    volatile boolean active;

    int xOffset;
    int zOffset;

    public Movement(Entity entity, MovementType type, double distance, double speed) {
        this.type = type;
        this.entity = entity;
        this.xform = entity.model.getXform();
        this.distance = distance;
        this.speed = speed;
        this.duration = Duration.seconds((16 - speed) * 0.25);
    }

    @Override
    public void run() {

        this.active = true;

        // Creating the transition
        switch (type) {
            case FORWARD, BACKWARD -> this.transition = linearMove();
            case TURN_RIGHT, TURN_LEFT -> this.transition = axialMove();
        }
        movePeripherals();

        // Running the transition  - this runnable will not finish executing until the transition has finished running
        this.transition.setOnFinished((e) -> active = false);
        this.transition.play();

        // Will not allow the Runnable to end until the transition has completed
        while (active) Thread.onSpinWait();

    }

    private TranslateTransition linearMove() {

        double amount = type == FORWARD ? distance : distance - (distance * 2);

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(xform);
        transition.setDuration(duration);

        if (entity.getHeading().id % 2 == 0) {
            transition.setByX(entity.getHeading() == NORTH ? amount : -amount);
        } else {
            transition.setByZ(entity.getHeading() == EAST ? amount : -amount);
        }

        // Defining offset to update location information
        int offsetAmount = type == FORWARD ? 1 : -1;
        this.xOffset = switch (entity.getHeading().id) {
            case 0 -> offsetAmount;
            case 2 -> -offsetAmount;
            default -> 0;
        };
        this.zOffset = switch (entity.getHeading().id) {
            case 1 -> offsetAmount;
            case 3 -> -offsetAmount;
            default -> 0;
        };

        // Updating location information
        entity.updateTerrainLocation(this.xOffset, this.zOffset);
        entity.updateCoordinates(this.xOffset, this.zOffset);

        return transition;

    }

    private RotateTransition axialMove() {

        RotateTransition transition = new RotateTransition();

        transition.setNode(xform);
        transition.setAxis(Rotate.Y_AXIS);
        transition.setDuration(duration);

        double amount = type == TURN_RIGHT ? -90 : 90;

        transition.setByAngle(amount);

        // Changing Entity heading if it has turned
        if (type == TURN_RIGHT) {
            entity.setHeading(entity.getHeading().getHeadingRight());
        } else {
            entity.setHeading(entity.getHeading().getHeadingLeft());
        }

        return transition;

    }

    private void movePeripherals() {
        if (entity instanceof Rover) {
            for (int i = 1; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Node wheelComponent = ((Xform) xform.getChildren().get(i)).getChildren().get(j);
                    RotateTransition transition = new RotateTransition();
                    transition.setNode(wheelComponent);
                    switch (type) {
                        case FORWARD -> transition.setByAngle(-distance * Math.PI);
                        case BACKWARD -> transition.setByAngle(distance * Math.PI);
                        case TURN_LEFT -> {
                            if (j < 2) {
                                transition.setByAngle(distance * Math.PI);
                            }
                            else {
                                transition.setByAngle(-distance * Math.PI);
                            }
                        }
                        case TURN_RIGHT -> {
                            if (j < 2) {
                                transition.setByAngle(-distance * Math.PI);
                            }
                            else {
                                transition.setByAngle(distance * Math.PI);
                            }
                        }
                    }
                    transition.setDuration(this.transition.getTotalDuration());
                    transition.play();
                }
            }
        }
    }

    public int getxOffset() {
        return this.xOffset;
    }

    public int getzOffset() {
        return this.zOffset;
    }

    public enum MovementType {
        FORWARD,
        BACKWARD,
        TURN_RIGHT,
        TURN_LEFT
    }

}

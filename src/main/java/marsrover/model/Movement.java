package marsrover.model;

import javafx.animation.*;
import javafx.scene.transform.Rotate;
import marsrover.entity.Entity;
import marsrover.entity.Heading;
import marsrover.entity.entities.Rover;

import static marsrover.model.Movement.MovementType.*;

public class Movement implements Runnable {

    MovementType type;
    Entity entity;
    Xform xform;
    double distance;
    double speed;
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
    }

    @Override
    public void run() {

        this.active = true;

        // Creating the transition
        switch (type) {
            case FORWARD, BACKWARD -> this.transition = linearMove();
            case TURN_RIGHT, TURN_LEFT -> this.transition = axialMove();
        }
//                movePeripherals(speed); TODO: Move wheels too

        // Running the transition  - this runnable will not finish executing until the transition has finished running
        this.transition.setOnFinished((e) -> active = false);
        this.transition.play();

        if (type == FORWARD || type == BACKWARD) {
            // Defining offset to update location information
            this.xOffset = switch (entity.getHeading().id) {
                case 0 -> 1;
                case 2 -> -1;
                default -> 0;
            };
            this.zOffset = switch (entity.getHeading().id) {
                case 1 -> 1;
                case 3 -> -1;
                default -> 0;
            };

            // Updating location information
            entity.updateTerrainLocation(this.xOffset, this.zOffset);
            entity.updateCoordinates(this.xOffset, this.zOffset);
        }
        else if (type == TURN_LEFT || type == TURN_RIGHT) {
            // Changing Entity heading if it has turned
            if (type == TURN_RIGHT) {
                entity.setHeading(entity.getHeading().getHeadingRight());
            } else {
                entity.setHeading(entity.getHeading().getHeadingLeft());
            }
        }

        // Will not allow the Runnable to end until the transition has completed
        while (active) Thread.onSpinWait();

    }

    private TranslateTransition linearMove() {

        double amount = type == FORWARD ? distance : distance - (distance * 2);

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(xform);

        if (entity.getHeading().id % 2 == 0) {
            transition.setByX(entity.getHeading() == Heading.NORTH ? amount : -amount);
        } else {
            transition.setByZ(entity.getHeading() == Heading.EAST ? amount : -amount);
        }

        return transition;

    }

    private RotateTransition axialMove() {

        RotateTransition transition = new RotateTransition();
        transition.setNode(xform);
        transition.setAxis(Rotate.Y_AXIS);

        double amount = type == TURN_RIGHT ? -90 : 90;

        transition.setByAngle(amount);

        return transition;

    }

    private void movePeripherals(double speed) {
        if (entity instanceof Rover) {
//            double distance = (((Cylinder) xform.getChildren().get(4)).getRadius() * 2) * Math.PI;
            for (int j = 1; j < 4; j++) {
                xform.getChildren().get(j).setRotate(xform.getChildren().get(j).getRotate() - (speed*5));
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

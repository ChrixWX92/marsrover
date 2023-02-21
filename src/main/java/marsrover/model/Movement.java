package marsrover.model;

import javafx.animation.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import marsrover.entity.Entity;
import marsrover.entity.Heading;
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

        System.out.println((entity.getHeading()));

        this.active = true;

        // Creating the transition
        switch (type) {
            case FORWARD, BACKWARD -> this.transition = linearMove(calculateSpeed(5));
            case TURN_RIGHT, TURN_LEFT -> this.transition = axialMove(calculateSpeed(5));
        }
                movePeripherals(speed); //TODO: Move wheels too

        // Running the transition  - this runnable will not finish executing until the transition has finished running
        this.transition.setOnFinished((e) -> active = false);
        this.transition.play();

        if (type == FORWARD || type == BACKWARD) {

        }
        else if (type == TURN_LEFT || type == TURN_RIGHT) {

        }

        // Will not allow the Runnable to end until the transition has completed
        while (active) Thread.onSpinWait();

    }

    private TranslateTransition linearMove(Duration speed) {

        double amount = type == FORWARD ? distance : distance - (distance * 2);

        System.out.println(type.toString());
        System.out.println(Arrays.toString(entity.getCoordinates()));

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(xform);
        transition.setDuration(speed);

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

    private RotateTransition axialMove(Duration speed) {

        RotateTransition transition = new RotateTransition();

        transition.setNode(xform);
        transition.setAxis(Rotate.Y_AXIS);
        transition.setDuration(speed);

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

    private void movePeripherals(double speed) {
        if (entity instanceof Rover) {
//            double distance = (((Cylinder) xform.getChildren().get(4)).getRadius() * 2) * Math.PI;
            for (int j = 1; j < 4; j++) {
                RotateTransition transition = new RotateTransition();
                transition.setNode(xform.getChildren().get(j));
//                transition.setAxis(Rotate.Y_AXIS);
                transition.setByAngle(-360);
                transition.play();
            }
        }
    }

    private Duration calculateSpeed(int speedFactor) {
        return Duration.seconds(speedFactor * 0.25);
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

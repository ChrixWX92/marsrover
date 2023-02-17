package marsrover.model;

import javafx.animation.AnimationTimer;
import marsrover.TerminalThread;
import marsrover.entity.Entity;
import marsrover.entity.Heading;

import java.util.Arrays;

import static marsrover.model.Movement.MovementType.*;

public class Movement extends Thread {

    MovementType type;
    Entity entity;
    Xform xform;
    double distance;
    double speed;
    double degree;

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

        TerminalThread.movementFlag = true;

        degree = distance / speed;
        double origin = xform.getRotate();
        System.out.println(entity.getHeading());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                switch (type) {
                    case FORWARD, BACKWARD -> linearMove(type == FORWARD ? speed : speed - (speed * 2));
                    case TURN_RIGHT, TURN_LEFT -> xform.rotateProperty().set(type == TURN_RIGHT ? xform.getRotate() - speed : xform.getRotate() + speed);
                }
                degree--;
                if (degree <= 0) {
                    switch (type) {
                        case TURN_RIGHT -> {
                            xform.setRotate(origin - distance);
                            entity.setHeading(entity.getHeading().getHeadingRight());
                        }
                        case TURN_LEFT -> {
                            xform.setRotate(origin + distance);
                            entity.setHeading(entity.getHeading().getHeadingLeft());
                        }
                    }
                    this.stop();
                }
            }
        };
        timer.start();

        if (type == FORWARD || type == BACKWARD) {
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
            entity.updateTerrainLocation(this.xOffset, this.zOffset);
            entity.updateCoordinates(this.xOffset, this.zOffset);
        }

        System.out.println(Arrays.toString(entity.getCoordinates()));

        TerminalThread.movementFlag = false;

    }

    private void linearMove(double amount) {
        if (entity.getHeading().id % 2 == 0) {
            xform.translateXProperty().set(entity.getHeading() == Heading.NORTH ? xform.getTranslateX() + amount : xform.getTranslateX() - amount);
        } else {
            xform.translateZProperty().set(entity.getHeading() == Heading.EAST ? xform.getTranslateZ() + amount : xform.getTranslateZ() - amount);
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

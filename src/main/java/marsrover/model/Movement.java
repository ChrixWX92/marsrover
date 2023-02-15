package marsrover.model;

import marsrover.Main;
import marsrover.TerminalThread;
import marsrover.entity.Entity;
import marsrover.model.models.Model;
import marsrover.terrain.GridSquare;
import marsrover.terrain.Plateau;

import java.util.Arrays;
import java.util.List;

public class Movement extends Thread {

    MovementType type;
    Entity entity;
    Xform xform;
    double distance;
    long speed;

    int xOffset;
    int zOffset;

    public Movement(Entity entity, MovementType type, double distance, long speed) {
        this.type = type;
        this.entity = entity;
        this.xform = entity.model.getXform();
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public void run() {

        TerminalThread.movementFlag = true;

//        System.out.println("heading = " + entity.getHeading());
//        System.out.println("movement xform address = " + this.xform);
        int xOffset = 0;
        int zOffset = 0;
        switch (this.type) {
            case FORWARD -> {
                for (int i = 0 ; i < distance ; i++) {
                    switch (entity.getHeading()) {
                        case 0 -> xform.setTranslateX(xform.getTranslateX() + 1);
                        case 1 -> xform.setTranslateZ(xform.getTranslateZ() + 1);
                        case 2 -> xform.setTranslateX(xform.getTranslateX() - 1);
                        case 3 -> xform.setTranslateZ(xform.getTranslateZ() - 1);
                    }
                    for (int j = 1; j < 4; j++) {
                        xform.getChildren().get(j).setRotate(xform.getChildren().get(j).getRotate() - 5);}
                    try {
                        sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                xOffset = switch (entity.getHeading()) {
                    case 0 -> 1;
                    case 2 -> -1;
                    default -> 0;
                };
                zOffset = switch (entity.getHeading()) {
                    case 1 -> 1;
                    case 3 -> -1;
                    default -> 0;
                };
            }
            case BACKWARD -> {
                for (int i = 0 ; i < distance ; i++) {
                    switch (entity.getHeading()) {
                        case 0 -> xform.setTranslateX(xform.getTranslateX() - 1);
                        case 1 -> xform.setTranslateZ(xform.getTranslateZ() - 1);
                        case 2 -> xform.setTranslateX(xform.getTranslateX() + 1);
                        case 3 -> xform.setTranslateZ(xform.getTranslateZ() + 1);
                    }
                    for (int j = 1; j < 4; j++) {
                        xform.getChildren().get(j).setRotate(xform.getChildren().get(j).getRotate() + 5);}
                    try {
                        sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                xOffset = switch (entity.getHeading()) {
                    case 0 -> -1;
                    case 2 -> 1;
                    default -> 0;
                };
                zOffset = switch (entity.getHeading()) {
                    case 1 -> -1;
                    case 3 -> 1;
                    default -> 0;
                };
            }
            case TURN_RIGHT -> {
                distance = 90; //TODO: Shouldn't be overridden - temporarily magic to avoid inaccurate values passed to the method
                for (int i = 0 ; i < distance ; i++) {
                    xform.setRotate(xform.getRotate() - 1);
                    for (int j = 1; j < 4; j++) {
                        xform.getChildren().get(j).setRotate(xform.getChildren().get(j).getRotate() + 5);}
                    try {
                        sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                entity.setHeading(entity.getHeading() >= 3 ? 0 : entity.getHeading() + 1);
            }
            case TURN_LEFT -> {
                distance = 90; //TODO: Shouldn't be overridden - temporarily magic to avoid inaccurate values passed to the method
                for (int i = 0 ; i < distance ; i++) {
                    xform.setRotate(xform.getRotate() + 1);
                    for (int j = 1; j < 4; j++) {
                        xform.getChildren().get(j).setRotate(xform.getChildren().get(j).getRotate() + 5);}
                    try {
                        sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                entity.setHeading(entity.getHeading() <= 0 ? 3 : entity.getHeading() - 1);
            }
        }
        this.xOffset = xOffset;
        this.zOffset = zOffset;

        entity.updateTerrainLocation(this.xOffset, this.zOffset);
        entity.updateCoordinates(this.xOffset, this.zOffset);

//        System.out.println("coords post-movement = " + Arrays.toString(entity.getCoordinates()));
        TerminalThread.movementFlag = false;

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

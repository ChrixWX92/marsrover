package marsrover;

import java.util.List;

public class Movement extends Thread {

    MovementType type;
    Model subject;
    Xform xform;
    double distance;
    long speed;

    public Movement(Model subject, MovementType type, double distance, long speed) {
        this.type = type;
        this.subject = subject;
        this.xform = subject.xform;
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public void run() {

        int xOffset = 0;
        int zOffset = 0;
        switch (this.type) {
            case FORWARD -> {
                for (int i = 0 ; i < distance ; i++) {
                    switch (subject.getHeading()) {
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
                xOffset = switch (subject.getHeading()) {
                    case 0 -> 1;
                    case 2 -> -1;
                    default -> 0;
                };
                zOffset = switch (subject.getHeading()) {
                    case 1 -> 1;
                    case 3 -> -1;
                    default -> 0;
                };
            }
            case BACKWARD -> {
                for (int i = 0 ; i < distance ; i++) {
                    switch (subject.getHeading()) {
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
                xOffset = switch (subject.getHeading()) {
                    case 0 -> -1;
                    case 2 -> 1;
                    default -> 0;
                };
                zOffset = switch (subject.getHeading()) {
                    case 1 -> -1;
                    case 3 -> 1;
                    default -> 0;
                };
            }
            case TURN_RIGHT -> {
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
                subject.setHeading(subject.getHeading() >= 3 ? 0 : subject.getHeading() + 1);
            }
            case TURN_LEFT -> {
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
                subject.setHeading(subject.getHeading() <= 0 ? 3 : subject.getHeading() - 1);
            }
        }
        updateTerrainLocation(subject, xOffset, zOffset);
    }

    void updateTerrainLocation(Model model, int x, int z) {
        // TODO: This shouldn't need to be iterative - can be made much more efficient by saving the Model's coordinates in its class
        if (model.getSurface() instanceof Plateau plateau) {
            for (List<Grid> row : plateau.grids) {
                for (Grid grid : row) {
                    if (grid.getOccupant() == model) {
                        grid.setOccupant(null);
                        plateau.grids.get(grid.coordinates[0] + x).get(grid.coordinates[1 + z]).setOccupant(model);
                        return;
                    }
                }
            }
        }
    }

    enum MovementType {
        FORWARD,
        BACKWARD,
        TURN_RIGHT,
        TURN_LEFT
    }

}

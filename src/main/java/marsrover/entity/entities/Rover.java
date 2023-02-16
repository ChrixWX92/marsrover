package marsrover.entity.entities;

import marsrover.entity.Entity;
import marsrover.entity.Movable;
import marsrover.model.Movement;
import marsrover.model.models.RoverModel;
import marsrover.terrain.Plateau;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rover extends Entity implements Movable {

    public Rover(int[] coordinates, int facing) {
        super(coordinates, new RoverModel());
        if (coordinates[0] > 0) this.model.getXform().setTranslateX(this.getCoordinates()[0]*100);
        if (coordinates[1] > 0) this.model.getXform().setTranslateZ(this.getCoordinates()[1]*100);
        switch ((char) facing) {
            case 'E' -> {
                this.model.getXform().setRotate(90);
                this.setHeading(1);
            }
            case 'S' -> {
                this.model.getXform().setRotate(180);
                this.setHeading(2);
            }
            case 'W' -> {
                this.model.getXform().setRotate(270);
                this.setHeading(3);
            }
        }
        updateTerrainLocation(coordinates[0], coordinates[1]);
    }

    @Override
    public void move(Movement.MovementType direction, double distance) {

        if (this.getSurface() instanceof Plateau plateau) {

            int destinationX = this.getCoordinates()[0];
            int destinationZ = this.getCoordinates()[1];

            switch (direction) {
                case FORWARD -> {
                    switch (this.getHeading()) {
                        case 0 -> destinationX++;
                        case 1 -> destinationZ++;
                        case 2 -> destinationX--;
                        case 3 -> destinationZ--;
                    }
                }
                case BACKWARD -> {
                    switch (this.getHeading()) {
                        case 0 -> destinationX--;
                        case 1 -> destinationZ--;
                        case 2 -> destinationX++;
                        case 3 -> destinationZ++;
                    }
                }
            }

            if (
                    destinationX < 0 ||
                    destinationZ < 0 ||
                    destinationX > plateau.grid.size() - 1 ||
                    destinationZ > plateau.grid.get(0).size() - 1  ||
                    plateau.grid.get(destinationX).get(destinationZ) == null ||
                    plateau.grid.get(destinationX).get(destinationZ).isOccupied()
            ) {
                System.out.println("COLLISION");
                // DISALLOWED
                return;
            }


//            System.out.println("coords pre-movement = " + Arrays.toString(this.getCoordinates()));

            ExecutorService executor = Executors.newFixedThreadPool(2);

            Movement movement = new Movement(this, direction, distance, 5);
//
//        threadPool.submit(movement);
//
//        threadPool.execute(movement);

            movement.start();

//            System.out.println("offsets = " + movement.getxOffset() + ", " + movement.getzOffset());

        }
    }

}

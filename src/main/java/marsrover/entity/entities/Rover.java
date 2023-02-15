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

    public Rover(int[] coordinates) {
        super(coordinates, new RoverModel());
        System.out.println("rover xform address = " + this.model.getXform());
    }

    @Override
    public void move(Movement.MovementType direction, double distance) {

        System.out.println("inmove, terrain = " + this.getSurface());

        if (this.getSurface() instanceof Plateau plateau) {

            System.out.println("inif");

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

            if (destinationX < 0 || destinationZ < 0 || plateau.grid.get(destinationX).get(destinationZ) == null || plateau.grid.get(destinationX).get(destinationZ).isOccupied()) {
                System.out.println("COLLISION");
                // DISALLOWED
                return;
            }


            System.out.println("coords pre-movement = " + Arrays.toString(this.getCoordinates()));

            ExecutorService executor = Executors.newFixedThreadPool(2);

            Movement movement = new Movement(this, direction, distance, 5);
//
//        threadPool.submit(movement);
//
//        threadPool.execute(movement);

            movement.start();

            System.out.println("offsets = " + movement.getxOffset() + ", " + movement.getzOffset());

        }
    }

}

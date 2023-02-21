package marsrover.entity.entities;

import marsrover.controllers.Controller;
import marsrover.entity.Entity;
import marsrover.entity.Heading;
import marsrover.entity.Movable;
import marsrover.model.Movement;
import marsrover.model.models.RoverModel;
import marsrover.terrain.Plateau;

public class Rover extends Entity implements Movable {

    public Rover(int[] coordinates, int facing) {
        super(coordinates, new RoverModel());
        if (coordinates[0] > 0) this.model.getXform().setTranslateX(this.getCoordinates()[0]*100);
        if (coordinates[1] > 0) this.model.getXform().setTranslateZ(this.getCoordinates()[1]*100);
        for (Heading heading : Heading.values()) {
            if (heading.character == (char) facing) {
                this.setHeading(heading, true);
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
                    switch (this.getHeading().id) {
                        case 0 -> destinationX++;
                        case 1 -> destinationZ++;
                        case 2 -> destinationX--;
                        case 3 -> destinationZ--;
                    }
                }
                case BACKWARD -> {
                    switch (this.getHeading().id) {
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

            Movement movement = new Movement(this, direction, distance, 2.3);

            Controller.actionQueue.offer(movement);

        }
    }

}

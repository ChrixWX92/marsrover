package marsrover.entity.entities;

import marsrover.entity.Entity;
import marsrover.entity.Movable;
import marsrover.model.Movement;
import marsrover.model.models.RoverModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rover extends Entity implements Movable {

    public Rover(int[] coordinates) {
        super(coordinates, new RoverModel());
        System.out.println("rover xform address = " + this.model.getXform());
    }

    @Override
    public void move(Movement.MovementType direction, double distance) {
        System.out.println("moving rover");
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        System.out.println("passed xform address = " + this.model.getXform());
        Movement movement = new Movement(this, direction, distance, 5);
//
//        threadPool.submit(movement);
//
//        threadPool.execute(movement);

        movement.start();

        this.updateTerrainLocation(movement.getxOffset(), movement.getzOffset());

    }

}

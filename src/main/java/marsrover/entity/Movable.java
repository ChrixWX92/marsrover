package marsrover.entity;

import marsrover.model.Movement;
import marsrover.model.models.Model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Movable {

    // Should be synchronized
    void move(Movement.MovementType direction, double distance) throws InterruptedException;

}

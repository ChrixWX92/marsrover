package marsrover.entity;

import javafx.scene.input.KeyCode;
import marsrover.model.Actionable;
import marsrover.model.Movement;

public interface Movable extends Actionable {

    void move(Movement.MovementType direction, double distance);

}

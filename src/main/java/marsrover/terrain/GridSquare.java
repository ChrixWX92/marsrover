package marsrover.terrain;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import marsrover.Main;
import marsrover.entity.Entity;
import marsrover.entity.entities.Rover;
import marsrover.model.models.GridModel;

import java.util.Arrays;

public class GridSquare {

    private boolean occupied = false;
    private Entity occupant;
    int[] coordinates;
    double size;
    GridModel model;
    private boolean rendered = false;

    protected GridSquare(int x, int z, double size) {
        this.coordinates = new int[]{x, z};
        this.size = size;
    }

    public void render() {
        if (rendered) System.out.println("Re-rendering GridSquare @ " + Arrays.toString(coordinates) + ".");
        else {
            this.model = new GridModel(0, this.size);


            EventHandler<MouseEvent> setAnchor = event -> {
                if (event.isPrimaryButtonDown()) {
                    // TODO: Click event to add rover
//                    System.out.println(Arrays.toString(this.coordinates));
//                    Rover rover = new Rover(this.coordinates, 'N');
//                    Main.stage.getWorld().addEntity(rover);
                }
            };

//            DragController dragController = new DragController(circle, true);
//            dragController.isDraggableProperty().bind(isDraggableBox.selectedProperty());
//
//            circle.setOnMousePressed(event -> {
//                circle.setFill(Color.RED);
//            });
//            circle.setOnMouseReleased(event -> {
//                circle.setFill(Color.DODGERBLUE);
//            });

            this.model.build().addEventFilter(MouseEvent.MOUSE_PRESSED, setAnchor);

        }
    }


    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Entity getOccupant() {
        return occupant;
    }

    public void setOccupant(Entity occupant) {
        this.occupant = occupant;
    }
}

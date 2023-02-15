package marsrover.terrain;

import marsrover.model.Xform;

import java.util.ArrayList;
import java.util.List;

public class Plateau extends Terrain {

    final double gridSize = 100;
    public List<List<GridSquare>> grid;

    private final int[] size;

    public Plateau(int x, int z) {
        this.size = new int[]{x, z};
        this.grid = new ArrayList<>();
        for (int i = 0 ; i < x ; i++) this.grid.add(new ArrayList<>());
    }

    @Override
    public Xform build() {

        Xform plateauXform = new Xform();

        for (int i = 0; i < this.size[0] ; i++) {
            for (int j = 0; j < this.size[1] ; j++) {
                GridSquare gridSquare = new GridSquare(i, j, gridSize);
                gridSquare.render();
                gridSquare.model.getXform().setTranslateX(i* gridSize);
                gridSquare.model.getXform().setTranslateZ(j* gridSize);
                plateauXform.getChildren().add(gridSquare.model.getXform());
                this.grid.get(i).add(j, gridSquare);
            }
        }

        this.xform.getChildren().add(plateauXform);
        return xform;

    }

    public double getGridSize() {
        return gridSize;
    }

    public int[] getSize() {
        return size;
    }

}

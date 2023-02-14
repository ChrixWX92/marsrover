package marsrover;

import java.util.ArrayList;
import java.util.List;

public class Plateau extends Terrain implements Buildable {

    static final double GRID_SIZE = 100;
    final Xform plateauGroup = new Xform();
    List<List<Grid>> grids;

    private final int[] size;

    public Plateau(int x, int z) {
        this.size = new int[]{x, z};
        this.grids = new ArrayList<>();
        for (int i = 0 ; i < x ; i++) this.grids.add(new ArrayList<>());
    }

    @Override
    public Xform build() {

        Xform surfaceXform = new Xform();

        for (int i = 0; i < this.size[0] ; i++) {
            for (int j = 0; j < this.size[1] ; j++) {

                Grid grid = new Grid(0, i, j, GRID_SIZE);
                grid.build();
                grid.xform.setTranslateX(i*GRID_SIZE);
                grid.xform.setTranslateZ(j*GRID_SIZE);
                surfaceXform.getChildren().add(grid.xform);
                this.grids.get(i).add(j, grid);

            }
        }

        plateauGroup.getChildren().add(surfaceXform);
        this.terrainGroup.getChildren().add(plateauGroup);
        return plateauGroup;

    }
}

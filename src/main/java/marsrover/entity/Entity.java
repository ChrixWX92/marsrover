package marsrover.entity;

import marsrover.model.models.Model;
import marsrover.terrain.GridSquare;
import marsrover.terrain.Plateau;
import marsrover.terrain.Terrain;

public abstract class Entity {

    /**
     * 0 = N
     * 1 = E
     * 2 = S
     * 3 = W
     */
    private int heading = 0;
    public final Model model;
    Terrain surface;
    int[] coordinates;

    public Entity(int[] coordinates, Model model) {
        this.coordinates = coordinates;
        this.model = model;
    }

    public void updateTerrainLocation(int x, int z) {
        if (this.surface instanceof Plateau plateau) {
            GridSquare oldSquare = plateau.grid.get(this.coordinates[0]).get(this.coordinates[1]);
            GridSquare newSquare = plateau.grid.get(this.coordinates[0] + x).get(this.coordinates[1] + z);
            oldSquare.setOccupant(null);
            newSquare.setOccupant(this);
        }
    }

    public Terrain getSurface() {
        return surface;
    }

    public void setSurface(Terrain surface) {
        this.surface = surface;
        if (surface instanceof Plateau plateau) {
            //TODO: What to do if grid space occupied?
            plateau.grid.get(0).get(0).setOccupant(this); //TODO: Problem is that array addresses don't match
        }
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public void updateCoordinates(int x, int z) {
        this.setCoordinates(new int[]{getCoordinates()[0] + x, getCoordinates()[1] + z});
    }

}

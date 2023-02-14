package marsrover;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Model {

    Xform xform;
    /**
     * 0 = N
     * 1 = E
     * 2 = S
     * 3 = W
     */
    private int heading = 0;
    final double baseY;

    Terrain surface;

    protected Model(double baseY) {
        this.baseY = baseY;
    }

    public synchronized void move(Movement.MovementType direction, double distance) throws InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        Movement movement = new Movement(this, direction, distance, 5);
//
//        threadPool.submit(movement);
//
//        threadPool.execute(movement);

        movement.start();

    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public Terrain getSurface() {
        return surface;
    }

    public void setSurface(Terrain surface) {
        this.surface = surface;
        if (surface instanceof Plateau plateau) {
            //TODO: What to do if grid space occupied?
            plateau.grids.get(0).get(0).setOccupant(this); //TODO: Problem is that array addresses don't match
        }
    }

}

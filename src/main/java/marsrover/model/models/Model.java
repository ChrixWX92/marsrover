package marsrover.model.models;

import marsrover.model.Buildable;
import marsrover.model.Movement;
import marsrover.model.Xform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Model implements Buildable {

    Xform xform;
    final double baseY;
    boolean built;

    public Model(){this(0, false);}

    public Model(double baseY) {this(baseY, false);}

    public Model(boolean build){this(0, build);}

    public Model(double baseY, boolean build) {
        this.baseY = baseY;
        if (build) this.build();
    }

    public Xform getXform() {
        return xform;
    }



}

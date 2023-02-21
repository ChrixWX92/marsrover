package marsrover;

import marsrover.entity.Entity;
import marsrover.entity.Movable;
import marsrover.entity.entities.Rover;
import marsrover.model.Buildable;
import marsrover.model.Xform;
import marsrover.model.models.RoverModel;
import marsrover.terrain.Plateau;
import marsrover.terrain.Terrain;

import java.util.HashSet;
import java.util.Set;

public class World {

    Xform xform;

    Terrain terrain;
    Set<Entity> entities;
    Set<Buildable> buildables;
    Movable activeMovable;
    double movementQuantum;

    public World(){
        this.xform = new Xform();
        this.entities = new HashSet<>();
        this.buildables = new HashSet<>();
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        this.xform.getChildren().add(terrain.build());
        if (this.terrain instanceof Plateau plateau) this.movementQuantum = plateau.getGridSize();
        else {this.movementQuantum = 1;}
    }

    public void addEntity(Entity entity) {
        entity.setSurface(terrain);
        entities.add(entity);
        buildables.add(entity.model);
        xform.getChildren().addAll(entity.model.getXform());
    }

    public void updateBuildables(boolean build) {
        this.buildables = new HashSet<>();
        for (Entity entity : this.entities) {
            if (entity instanceof Buildable buildable) {
                this.buildables.add(buildable);
                if (build) this.buildBuildables();
            }
        }
    }

    public void buildBuildables() {
        for (Buildable buildable : this.buildables) buildable.build();
    }

    public Movable getActiveMovable() {
        return activeMovable;
    }

    public void setActiveMovable(Movable activeMovable) {
        this.activeMovable = activeMovable;
    }

    public double getMovementQuantum() {
        return movementQuantum;
    }


}

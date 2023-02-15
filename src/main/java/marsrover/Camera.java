package marsrover;

import javafx.scene.PerspectiveCamera;
import marsrover.model.Buildable;
import marsrover.model.Xform;

public class Camera implements Buildable {

    private static final double INITIAL_DISTANCE = -450;
    private static final double INITIAL_X_ANGLE = 70.0;
    private static final double INITIAL_Y_ANGLE = 320.0;
    private static final double NEAR_CLIP = 0.1;
    private static final double FAR_CLIP = 10000.0;

    final PerspectiveCamera camera = new PerspectiveCamera(true);

    final Xform[] xforms = new Xform[]{new Xform(), new Xform(), new Xform()};

    public Camera(boolean build) {
        if (build) this.build();
    }

    @Override
    public Xform build() {
//        System.out.println("buildCamera()");
//        root.getChildren().add(xforms[0]);
        xforms[0].getChildren().add(xforms[1]);
        xforms[1].getChildren().add(xforms[2]);
        xforms[2].getChildren().add(camera);
        xforms[2].setRotateZ(180.0);

        camera.setNearClip(NEAR_CLIP);
        camera.setFarClip(FAR_CLIP);
        camera.setTranslateZ(INITIAL_DISTANCE);
        xforms[0].ry.setAngle(INITIAL_Y_ANGLE);
        xforms[0].rx.setAngle(INITIAL_X_ANGLE);
        return xforms[0];
    }

    public void reset() {
        xforms[1].t.setX(0.0);
        xforms[1].t.setY(0.0);
        camera.setTranslateZ(INITIAL_DISTANCE);
        xforms[0].ry.setAngle(INITIAL_Y_ANGLE);
        xforms[0].ry.setAngle(INITIAL_X_ANGLE);
    }

    public Xform[] getXforms() {
        return xforms;
    }

    public PerspectiveCamera getPerspectiveCamera() {
        return camera;
    }

}

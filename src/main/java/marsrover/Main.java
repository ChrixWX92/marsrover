package marsrover;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import marsrover.entity.entities.Rover;
import marsrover.menu.menus.WelcomePane;
import marsrover.model.Movement;
import marsrover.model.Xform;
import marsrover.model.models.RoverModel;
import marsrover.terrain.Plateau;
import marsrover.terrain.Terrain;

import javax.swing.*;

public class Main extends Application {

    final Xform axisGroup = new Xform();

    public static int[][] rootCoordinates;

//    private void buildAxes() {
//
//        final double axisLength = 250.0;
//
//        System.out.println("buildAxes()");
//        final PhongMaterial redMaterial = new PhongMaterial();
//        redMaterial.setDiffuseColor(Color.DARKRED);
//        redMaterial.setSpecularColor(Color.RED);
//
//        final PhongMaterial greenMaterial = new PhongMaterial();
//        greenMaterial.setDiffuseColor(Color.DARKGREEN);
//        greenMaterial.setSpecularColor(Color.GREEN);
//
//        final PhongMaterial blueMaterial = new PhongMaterial();
//        blueMaterial.setDiffuseColor(Color.DARKBLUE);
//        blueMaterial.setSpecularColor(Color.BLUE);
//
//        final Box xAxis = new Box(axisLength, 1, 1);
//        final Box yAxis = new Box(1, axisLength, 1);
//        final Box zAxis = new Box(1, 1, axisLength);
//
//        xAxis.setMaterial(redMaterial);
//        yAxis.setMaterial(greenMaterial);
//        zAxis.setMaterial(blueMaterial);
//
//        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
//        axisGroup.setVisible(true);
//        worldXform.getChildren().addAll(axisGroup);
//    }

    private void menu () {

        WelcomePane frame = new WelcomePane();
        frame.setTitle("Generate Plateau");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBounds(500, 100, 500, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {

        System.out.println("start()");

        Stage stage = new Stage();
        World world = new World();
        world.setTerrain(new Plateau(5, 5));

//        buildAxes();
        Rover initialRover = new Rover(new int[]{0,0});
        world.addEntity(initialRover);
        stage.addWorld(world);
        stage.addCamera(new Camera(true));

        stage.generateScene();
        stage.buildLight();


        stage.showPrimaryStage(primaryStage);

        // TODO: Menu should be repositioned and all the above should be placed in an execute method
        // TODO: This method will either be called by a terminal listener or Generate Plateau

        menu();

        world.setActiveMovable(initialRover);
        stage.engagePeripheralHandlers();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

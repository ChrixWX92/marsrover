package marsrover;

import javafx.application.Application;
import javafx.application.Platform;
import marsrover.menu.menus.WelcomePane;
import marsrover.model.Xform;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main extends Application {

    final Xform axisGroup = new Xform();

    public static Stage stage;

    public static final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

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
        frame.setBounds(600, 250, 600, 470);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(
                    frame, "Proceed with terminal input?", "Terminal Input",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    int[] coords;
                    int[] roverCoords;
                    while (true) {
                        try {
                            coords = inputCoords(reader);
                            break;
                        } catch (NumberFormatException | IOException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("\nERROR: Invalid format, please try again.\n");
                        }
                    }
                    while (true) {
                        try {
                            roverCoords = inputRoverCoords(reader);
                            break;
                        } catch (NumberFormatException | IOException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("\nERROR: Invalid format, please try again.\n");
                        }
                    }
                    frame.dispose();
                    int[] finalCoords = coords;
                    int[] finalRoverCoords = roverCoords;
                    Main.taskQueue.add(() -> Main.stage.engageControl(finalCoords[0]+1, finalCoords[1]+1, finalRoverCoords[0], finalRoverCoords[1], finalRoverCoords[2]));
                }
                else {
                    Platform.exit();
                    System.exit(0);
                }
            }

            private int[] inputCoords(BufferedReader reader) throws IOException {
                System.out.println("Please specify your plateau size in coordinates separated by a space.");
                System.out.println("(e.g. for an 8x5 grid: \"7 4\"):");
                String[] args = reader.readLine().split(" ");
                return new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1])};
            }
            private int[] inputRoverCoords(BufferedReader reader) throws IOException {
                System.out.println("\nPlease specify your initial rover's starting coordinates and facing");
                System.out.println("in the following format:  \"x y <N/E/S/W>\"");
                System.out.println("(e.g. \"3 1 S\"):");
                String[] args = reader.readLine().split(" ");
                return new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2].charAt(0)};
            }

        });

    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {

        stage = new Stage();
        World world = new World();

//        buildAxes();

        stage.addWorld(world);
        stage.addCamera(new Camera(true));

        stage.generateScene();
        stage.buildLight();

        stage.showPrimaryStage(primaryStage);

        menu();

        try {
            taskQueue.take().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runTerminalThread(world);

    }

    public static void runTerminalThread(World world) {
        TerminalThread terminalInput = new TerminalThread(world);
        Thread terminalThread = new Thread(terminalInput);
        terminalThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

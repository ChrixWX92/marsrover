package marsrover;

import marsrover.entity.Entity;
import marsrover.model.Movement;

import java.io.*;

import static marsrover.model.Movement.MovementType.*;

public class TerminalThread implements Runnable {

    public static volatile boolean movementFlag = false;
    World world;

    public TerminalThread(World world) {
        super();
        this.world = world;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String instructions = reader.readLine().toUpperCase();
            for (char instruction : instructions.toCharArray()) {
                Movement.MovementType direction = switch (instruction) {
                    case 'M' -> FORWARD;
                    case 'B' -> BACKWARD;
                    case 'L' -> TURN_LEFT;
                    case 'R' -> TURN_RIGHT;
                    default -> throw new IllegalStateException("Unexpected value: " + instruction);
                };
                while (movementFlag) {Thread.onSpinWait();}

                double distance = (direction == TURN_LEFT || direction == TURN_RIGHT) ? 90 : world.getMovementQuantum();
                world.getActiveMovable().move(direction, distance);
                Thread.sleep(1000);
            }
            int[] endCoords = ((Entity) world.getActiveMovable()).getCoordinates();
            System.out.println(endCoords[0] + " " + endCoords[1]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Main.runTerminalThread(world);
    }

}
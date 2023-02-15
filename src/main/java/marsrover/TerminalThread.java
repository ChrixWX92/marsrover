package marsrover;

import marsrover.entity.Entity;
import marsrover.model.Movement;

import java.io.*;

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
                    case 'M' -> Movement.MovementType.FORWARD;
                    case 'B' -> Movement.MovementType.BACKWARD;
                    case 'L' -> Movement.MovementType.TURN_LEFT;
                    case 'R' -> Movement.MovementType.TURN_RIGHT;
                    default -> throw new IllegalStateException("Unexpected value: " + instruction);
                };
                while (movementFlag) {Thread.onSpinWait();}
                world.getActiveMovable().move(direction, world.getMovementQuantum());
                Thread.sleep(1000);
            }
            int[] endCoords = ((Entity) world.getActiveMovable()).getCoordinates();
//            System.out.println(((Entity) world.getActiveMovable()).model.getXform().getRotate());
            System.out.println(endCoords[0] + " " + endCoords[1]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Main.runTerminalThread(world);
    }

}
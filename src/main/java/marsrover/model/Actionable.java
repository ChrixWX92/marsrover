package marsrover.model;

import java.util.ArrayDeque;
import java.util.Deque;

public interface Actionable {

    Deque<Runnable> actionDeque = new ArrayDeque<>();

}

package bob.client;/**
 * Created by Emanuel on 4/1/2016.
 */

import java.util.EnumSet;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Paul
 */
public class MultipleKeyHandler implements EventHandler<KeyEvent> {

    private final Set<KeyCode> buffer = EnumSet.noneOf(KeyCode.class);
    private final MultiKeyEvent multiKeyEvent = new MultiKeyEvent();

    private final MultiKeyEventHandler Handler;

    public MultipleKeyHandler(final MultiKeyEventHandler handler) {
        this.Handler = handler;
    }

    public void handle(final KeyEvent event) {
        final KeyCode code = event.getCode();

        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            buffer.add(code);
            Handler.handle(multiKeyEvent);
        } else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            buffer.remove(code);
        }
        event.consume();
    }

    public interface MultiKeyEventHandler {
        void handle(final MultiKeyEvent event);
    }

    public class MultiKeyEvent {
        public boolean isPressed(final KeyCode key) {
            return buffer.contains(key);
        }
    }
}

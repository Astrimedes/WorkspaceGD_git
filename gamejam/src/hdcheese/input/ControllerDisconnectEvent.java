package hdcheese.input;

import hdcheese.gameEvents.GameEvent;

import com.badlogic.gdx.controllers.Controller;

public class ControllerDisconnectEvent implements GameEvent<ControllerAddRemoveListener> {
	
	Controller controller;
	
	public ControllerDisconnectEvent(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void notify(ControllerAddRemoveListener listener) {
		listener.handleDisconnect(controller);
	}

}

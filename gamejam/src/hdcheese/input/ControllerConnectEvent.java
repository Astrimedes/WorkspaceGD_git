package hdcheese.input;

import hdcheese.gameEvents.GameEvent;

import com.badlogic.gdx.controllers.Controller;

public class ControllerConnectEvent implements GameEvent<ControllerAddRemoveListener> {
	
	Controller controller;
	
	public ControllerConnectEvent(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void notify(ControllerAddRemoveListener listener) {
		listener.handleConnect(controller);
	}

}

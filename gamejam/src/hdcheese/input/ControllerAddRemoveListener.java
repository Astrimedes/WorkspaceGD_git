package hdcheese.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

public class ControllerAddRemoveListener {
	
	public ControllerAddRemoveListener() {
	}
	
	public void handleDisconnect(Controller controller) {
		int stillConnected = 0;
		Array<Controller> arrControl = Controllers.getControllers();
		for(Controller c : arrControl) {
			if (c != controller) {
				stillConnected++;
			}
		}
		Gdx.app.log("controllers", "Disconnect! " + stillConnected + " still connected.");
	}
	
	public void handleConnect(Controller controller) {
		int totalConnected = 1;
		Array<Controller> arrControl = Controllers.getControllers();
		for(Controller c : arrControl) {
			if (c != controller) {
				totalConnected++;
			}
		}
		Gdx.app.log("controllers", "New Connect! " + totalConnected + " total connected.");
	}
	
}

package hdcheese.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Disposable;

import hdcheese.GameSession;
import hdcheese.world.Entity;

public class Player implements Disposable{
	
	public int index;
	
	//public CommandListener listener;
	
	public Entity entity;
	
	//public SimpleControllerListener controllerListener;
	
	public Controller controller;
	
	public Player(int index) {
		this.index = index;
		
		// make listener
		//listener = new CommandListener(index, null);
		
		//controllerListener = new SimpleControllerListener(index);
		
		//GameSession.eventService.listen(CommandStartEvent.class, listener);
		//GameSession.eventService.listen(CommandEndEvent.class, listener);
	}
	
	public void addController(Controller controller) {
		this.controller = controller;
		if (controller != null) {
			Gdx.app.log("controller", "controller is valid");
		}
		//controller.addListener(controllerListener);
	}
	
	public void addEntity(Entity e) {
		//listener.entity = e;
		this.entity = e;
	}
	
	@Override
	public void dispose() {
		index = -1;
//		if (listener != null) {
//			GameSession.eventService.mute(CommandStartEvent.class, listener);
//			GameSession.eventService.mute(CommandEndEvent.class, listener);
//		}
	}
	
}

package hdcheese.input;

import hdcheese.world.Entity;

import com.badlogic.gdx.Gdx;

public class CommandListener {
	
	int inputIndex;
	public Entity entity;
	
	public CommandListener(int inputIndex, Entity entity) {
		this.entity = entity;
		this.inputIndex = inputIndex;
	}
	
	public void handleCommandStartEvent(CommandStartEvent start) {
		if (start.index == inputIndex) {
			Gdx.app.log("Start Command", "index: " + inputIndex + ", Command: " + start.command);
			entity.addCommand(start.command);
		}
	}
	
	public void handleCommandEndEvent(CommandEndEvent end) {
		if (end.index == inputIndex) {
			Gdx.app.log("End Command", "index: " + inputIndex + ", Command: " + end.command);
			entity.removeCommand(end.command);
		}
	}
	
}

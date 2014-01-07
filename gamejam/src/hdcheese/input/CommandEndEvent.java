package hdcheese.input;

import hdcheese.gameEvents.GameEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CommandEndEvent implements Poolable, GameEvent<CommandListener>{
	
	public Command command = null;
	public int index = 0;
	
	public void setup(int inputIndex, Command command) {
		index = inputIndex;
		this.command = command;
	}
	
	@Override
	public void reset() {
		index = 0;
		this.command = null;
	}

	@Override
	public void notify(CommandListener listener) {
		listener.handleCommandEndEvent(this);
		Gdx.app.log("end event", command.toString());
	}

}


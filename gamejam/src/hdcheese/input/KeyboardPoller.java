package hdcheese.input;

import hdcheese.GameSession;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class KeyboardPoller {

	// input
	Input input;

	// flag
	boolean initialized = false;
	public boolean isInitialized() {
		return initialized;
	}

	// the defined keys for commands
	ArrayMap<Command, Array<Integer>> commandMap = new ArrayMap<Command, Array<Integer>>();

	// tracks held state of keys
	ArrayMap<Integer, Boolean> heldKeys = new ArrayMap<Integer, Boolean>();

	// initialize
	public void init(boolean addCommandListener) {

		if (initialized) {
			return;
		}
		
		// get input
		input = Gdx.input;

		// set the command definitions
		Array<Integer> arr = new Array<Integer>(); // tmp

		// right
		arr.add(Input.Keys.RIGHT);
		arr.add(Input.Keys.D);
		commandMap.put(Command.MOVE_RIGHT, new Array<Integer>(arr)); // add to map
		arr.clear();

		// left
		arr.add(Input.Keys.LEFT);
		arr.add(Input.Keys.A);
		commandMap.put(Command.MOVE_LEFT, new Array<Integer>(arr)); // add to map
		arr.clear();

		// up
		arr.add(Input.Keys.UP);
		arr.add(Input.Keys.W);
		commandMap.put(Command.MOVE_UP, new Array<Integer>(arr)); // add to map
		arr.clear();

		// down
		arr.add(Input.Keys.DOWN);
		arr.add(Input.Keys.S);
		commandMap.put(Command.MOVE_DOWN, new Array<Integer>(arr)); // add to map
		arr.clear();

		// select
		arr.add(Input.Keys.ENTER);
		commandMap.put(Command.SELECT, new Array<Integer>(arr)); // add to map
		arr.clear();

		// cancel
		arr.add(Input.Keys.ESCAPE);
		commandMap.put(Command.CANCEL, new Array<Integer>(arr)); // add to map
		arr.clear();

		// now with commands mapped, populate array of current held values
		for(Array<Integer> k : commandMap.values()) {
			for(Integer i : k) {
				heldKeys.put(i,  false);
			}
		}

		initialized = true;
	}


	/**
	 * Just pools keyboard for input.  convenient to test command event throwing
	 * @param dt
	 */
	public void update(float dt) {

		// test for buttons with different states than last update
		boolean wasPressed = false;
		boolean isPressed = false;
		// total up keydown and keyup changes to determine if we should start or end the event
		for(Command c : Command.values()) {
			//for(Array<Integer> keyCodes : commandMap.values()) {
			Array<Integer> keyCodes = commandMap.get(c);
			for(Integer kc : keyCodes) {
				wasPressed = heldKeys.get(kc);
				isPressed = Gdx.input.isKeyPressed(kc);

				if (wasPressed != isPressed) {
					// change status of held key
					heldKeys.put(kc, isPressed);
					
					// send event
					if (isPressed) {
						// get event from pool
						CommandStartEvent event = Pools.obtain(CommandStartEvent.class);
						// init
						event.setup(0, c);
						// throw
						GameSession.eventService.notify(event);
						// return to pool
						Pools.free(event);
					} else {
						// get event from pool
						CommandEndEvent event = Pools.obtain(CommandEndEvent.class);
						// init
						event.setup(0, c);
						// throw
						GameSession.eventService.notify(event);
						// return to pool
						Pools.free(event);
					}
				}
			}
		}

	}

	public void unload() {
		input = null;
		initialized = false;
	}

}

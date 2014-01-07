package hdcheese;

import hdcheese.audio.MusicBank;
import hdcheese.audio.SoundBank;
import hdcheese.gameEvents.EventService;
import hdcheese.graphics.MenuTool;
import hdcheese.input.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Static access to things like player scoring, audio, and hardware
 * @author Mike
 *
 */
public final class GameSession {
	
	public static Player[] players;
	
	private static StringBuilder builder = new StringBuilder("");
	
	public static void setupPlayers() {
		// use # of connected controllers for player # setting
		int playerCount = Math.max(1, Controllers.getControllers().size);
		players = new Player[playerCount];
		for (int i = 0; i < playerCount; ++i) {
			players[i] = new Player(i);
			if (Controllers.getControllers().size > 0) {
				players[i].addController(Controllers.getControllers().get(i));
			}
			Gdx.app.log("player setup", "Player " + players[i].index + " created");
		}
		
		if (playerCount == 0) {
			Gdx.app.log("player setup", "No players!");
		}
	}
	
	/**
	 * Public access to event service
	 */
	public static EventService eventService;
	
	private static GdxGame game;
	private static MenuTool menuTool;
	
	public static void setup(GdxGame game) {
		GameSession.game = game;
		
		// init other static things
		eventService = new EventService();
		
		// create the UI Skin
		menuTool = new MenuTool();
		menuTool.initializeSkin(true);
	}
	
	public static void disposeAll() {
		menuTool.disposeSkin();
	}
	
	public static GdxGame getGame() {
		return game;
	}
	
	public static Preferences getPreferences() {
		return Gdx.app.getPreferences(GdxGameConstants.PREF_NAME);
	}
	
	public static SoundBank getSound() {
		return game.getAudio().sound;
	}
	
	public static MusicBank getMusic() {
		return game.getAudio().music;
	}

	public static MenuTool getMenuTool() {
		return menuTool;
	}
	

}

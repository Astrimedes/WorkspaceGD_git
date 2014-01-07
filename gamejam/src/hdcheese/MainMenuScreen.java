package hdcheese;

import hdcheese.audio.MusicBank.MusicName;
import hdcheese.audio.SoundBank.SoundName;
import hdcheese.graphics.FancyColor;
import hdcheese.input.ControllerAddRemoveListener;
import hdcheese.input.ControllerConnectEvent;
import hdcheese.input.ControllerDisconnectEvent;
import hdcheese.input.SimpleControllerListener;
import hdcheese.world.GameplayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class MainMenuScreen extends GameScreen {
	
	//TextureRegionDrawable bg;
	
	SpriteBatch batch = new SpriteBatch();
	
	TextureRegionDrawable background;
	
	Stage stage;
	Skin skin;
	Table table;

	Label titleLabel;

	float highScore = 0;

	FancyColor flashColor = new FancyColor(0.85f, 0.1f, 0.1f, 0.9f);
	
	int minWidth = Gdx.graphics.getWidth() / 6;
	int maxWidth = Gdx.graphics.getWidth() / 2;
	int prefWidth = Gdx.graphics.getWidth() / 3;
	
	int minHeight = 30;
	int maxHeight = Gdx.graphics.getHeight() / 6;
	int prefHeight = Gdx.graphics.getHeight() / 10;

	public MainMenuScreen() {
		super(true);
	}

	// adds thin empty rows
	private void addEmptyRow(int count) {
		for(int i = 0; i < count; i++) {
			table.add().prefHeight(prefHeight/4).minHeight(minHeight/4).maxHeight(maxHeight/4);
			table.row();
		}
	}

	private void setupTable() {

		stage = new Stage();
		stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		skin = GameSession.getMenuTool().getSkin();
		table = new Table();
		
		//table.setBackground(bg);
		//table.setColor(0.2f, 0.2f, 0.4f, 1);
		// set to fill stage
		table.setFillParent(true);
		
		table.setBackground(background);
//
//		// turn on debugging
//		//table.debug();
//
		stage.addActor(table);

		// set default cell measurements
//		table.defaults().prefWidth(prefWidth).minWidth(minWidth).maxWidth(maxWidth).prefHeight(prefHeight).minHeight(minHeight).maxHeight(maxHeight);
//
//		// title text label
//		titleLabel = new Label("Grave Danger", skin.get("large", Label.LabelStyle.class));
//
//		titleLabel.setColor(flashColor); // uses the "FancyColor"
//		flashColor.setShimmer(true, 2.0f);
//
//		titleLabel.setAlignment(Align.center);
//		table.add(titleLabel).fill();
//		table.row();
//
//		// empty row
//		addEmptyRow(3);
//		
//		// Gameplay Screen
//		TextButton playButton = new TextButton("Play", skin);
//		playButton.addListener(new ClickListener() {
//			@Override
//			public void clicked (InputEvent event, float x, float y) {
//				game.setScreen(new GameplayScreen(true), true);
//			}
//		});
//		table.add(playButton);
//		table.row();
//
//		// empty row
//		addEmptyRow(1);
//		
//		// Options Screen
//		TextButton optionsButton = new TextButton("Options", skin);
//		optionsButton.addListener(new ClickListener() {
//			@Override
//			public void clicked (InputEvent event, float x, float y) {
//				game.setScreen(new OptionsScreen(), false);
//			}
//		});
//		table.add(optionsButton);
//		table.row();
//
//		// empty row
//		addEmptyRow(3);
//		
//		// Exit game
//		TextButton quitButton = new TextButton("Exit", skin);
//		quitButton.addListener(new ClickListener() {
//			@Override
//			public void clicked (InputEvent event, float x, float y) {
//				game.setScreen(null, false);
//				return;
//			}
//		});
//		table.add(quitButton);
//		table.row();
//
//		// set as input
//		Gdx.input.setInputProcessor(stage);
		
		
	}

	@Override
	public boolean loadAssets() {
		
		// init controller
		setupControllers();
		
		GameSession.getMenuTool().initializeSkin(false);
		
		background = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("backgrounds/title.png"))));

		// main menu setup
		setupTable();

		return loadAudio();
	}

	private boolean loadAudio() {
		
		//game.getSound().loadSound(SoundName.SELECT, "audio/pickup_1.wav");
		//game.getSound().loadSound(SoundName.CANCEL, "audio/powerup_1.wav");
		
		// music
		game.getMusic().loadMusic(MusicName.GAMEPLAY, "audio/game jam 3.mp3");
		
		// sounds
		game.getSound().loadSound(SoundName.GUARD_ATTACK, "audio/club01.mp3", "audio/club02.mp3",
				"audio/club03.mp3");
		game.getSound().loadSound(SoundName.PLAYER_ATTACK, "audio/shovelhit01.mp3", "audio/shovelhit02.mp3",
				"audio/shovelhit03.mp3");
		game.getSound().loadSound(SoundName.GUARD_HURT, "audio/guardow01.mp3", "audio/guardow02.mp3");
		game.getSound().loadSound(SoundName.PLAYER_HURT, "audio/igorow01.mp3", "audio/igorow02.mp3");
		game.getSound().loadSound(SoundName.GUARD_VOICE, "audio/oya.mp3");
		game.getSound().loadSound(SoundName.PLAYER_DIG, "audio/dig01.mp3", "audio/dig02.mp3");
		
		return true;
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	protected void unloadAssets() {
		game.getSound().unloadAssets();
		game.getMusic().unloadAssets();
	}

	@Override
	public boolean isPaused() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(float dt){
		super.render(dt);

		// shimmer title color
//		flashColor.update(dt	);
//		titleLabel.setColor(flashColor);
		
		// just press a button to play
		if (Controllers.getControllers().size > 0) {
			Controller controller = Controllers.getControllers().first(); 
			if (controller.getButton(0) || controller.getButton(1) || controller.getButton(2) || controller.getButton(3)) {
				game.setScreen(new GameplayScreen(true), true);
			}
		}

		// start with keyboard
		if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.Z)) {
			game.setScreen(new GameplayScreen(true), true);
		}

		// ... or exit with escape
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			game.setScreen(null, false);
		}
		
		// don't allow input while transitioning
//		if (!this.fadingOut && !this.fadingIn) {
//			// exit when Back is pressed
//			if (getTotalScreenTime() > 0.75f && 
//					(Gdx.input.isKeyPressed(Input.Keys.BACK) | Gdx.input.isKeyPressed(Input.Keys.ESCAPE))) {
//				game.setScreen(null, false);
//				return;
//			}
//			stage.act(dt);
//		}
		
		stage.act(dt);


		stage.draw();

		//Table.drawDebug(stage);
	}
	
	private void setupControllers() {

		// set up players, etc
		GameSession.setupPlayers();
		
		
	}

}

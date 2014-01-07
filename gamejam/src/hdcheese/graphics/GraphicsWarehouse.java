package hdcheese.graphics;

import hdcheese.world.TileType;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsWarehouse {
	public static TextureAtlas spriteAtlas;

	public static TextureRegion grassTile;
	public static TextureRegion graveTile;
	public static TextureRegion graveOpenTile;
	public static TextureRegion fenceHori;
	public static TextureRegion fenceVert;
	public static TextureRegion fenceLowerRight;
	public static TextureRegion fenceLowerLeft;
	public static TextureRegion fenceUpperRight;
	public static TextureRegion fenceUpperLeft;
	
	public static TextureRegion part01;
	public static TextureRegion part02;
	public static TextureRegion part03;
	public static TextureRegion bone;


	public static Animation playerWalkRightAnim;
	public static Animation playerWalkLeftAnim;
	public static Animation playerWalkUpAnim;
	public static Animation playerWalkDownAnim;

	public static Animation playerStandDownAnim;
	public static Animation playerStandUpAnim;
	public static Animation playerStandRightAnim;
	public static Animation playerStandLeftAnim;

	public static Animation guardWalkRightAnim;
	public static Animation guardWalkLeftAnim;
	public static Animation guardWalkUpAnim;
	public static Animation guardWalkDownAnim;

	public static Animation guardStandDownAnim;
	public static Animation guardStandUpAnim;
	public static Animation guardStandRightAnim;
	public static Animation guardStandLeftAnim;

	public static Animation guardAttackDownAnim;
	public static Animation guardAttackUpAnim;
	public static Animation guardAttackRightAnim;
	public static Animation guardAttackLeftAnim;

	public static Animation playerAttackDownAnim;
	public static Animation playerAttackUpAnim;
	public static Animation playerAttackRightAnim;
	public static Animation playerAttackLeftAnim;

	public static TextureRegion getTileTexture(TileType tile) {
		switch(tile) {
		case FENCE_HORIZONTAL:
			return fenceHori;
		case FENCE_LL:
			return fenceLowerLeft;
		case FENCE_LR:
			return fenceLowerRight;
		case FENCE_UL:
			return fenceUpperLeft;
		case FENCE_UR:
			return fenceUpperRight;
		case FENCE_VERTICAL:
			return fenceVert;
		case GRASS:
			return grassTile;
		case GRAVE:
			return graveTile;
		case GRAVE_OPEN:
			return graveOpenTile;
		}
		return null;
	}

	public static void loadTextures(TextureAtlas textureAtlas) {
		spriteAtlas = textureAtlas;

		// map tiles
		grassTile = textureAtlas.findRegion("grass01");
		graveTile = textureAtlas.findRegion("grave01closed");
		graveOpenTile = textureAtlas.findRegion("grave01open");
		fenceLowerLeft = textureAtlas.findRegion("lowerright");
		fenceLowerRight = textureAtlas.findRegion("fencelowerright");
		fenceUpperLeft = textureAtlas.findRegion("fenceupperleft");
		fenceUpperRight = textureAtlas.findRegion("fenceupperright");
		fenceHori = textureAtlas.findRegion("fencehori");
		fenceVert = textureAtlas.findRegion("fencevert");
		
		// body parts
		part01 = textureAtlas.findRegion("part01");
		part02 = textureAtlas.findRegion("part02");
		part03 = textureAtlas.findRegion("part03");
		bone = textureAtlas.findRegion("bone");

		// PLAYER
		// player walk animation
		playerWalkRightAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorrightwalkshovel01")),
				new Frame((TextureRegion)textureAtlas.findRegion("igorrightwalkshovel02")));
		playerWalkLeftAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorleftwalkshovel01")),
				new Frame((TextureRegion)textureAtlas.findRegion("igorleftwalkshovel02")));
		playerWalkUpAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorupwalkshovel01")),
				new Frame((TextureRegion)textureAtlas.findRegion("igorupwalkshovel02")));
		playerWalkDownAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igordownwalkshovel01")),
				new Frame((TextureRegion)textureAtlas.findRegion("igordownwalkshovel02")));

		// player idle
		playerStandDownAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igordownstandshovel01")));
		playerStandUpAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorupstandshovel01")));
		playerStandRightAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorrightstandshovel01")));
		playerStandLeftAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorleftstandshovel01")));

		// player attack
		playerAttackDownAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igordownattackshovel01")));
		playerAttackUpAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorupattackshovel01")));
		playerAttackRightAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorrightattackshovel01")));
		playerAttackLeftAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("igorleftattackshovel01")));		


		// GUARD
		// guard walk animation
		guardWalkRightAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardrightwalk01")),
				new Frame((TextureRegion)textureAtlas.findRegion("guardrightwalk02")));
		guardWalkLeftAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardleftwalk01")),
				new Frame((TextureRegion)textureAtlas.findRegion("guardleftwalk02")));
		guardWalkUpAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardupwalk01")),
				new Frame((TextureRegion)textureAtlas.findRegion("guardupwalk02")));
		guardWalkDownAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guarddownwalk01")),
				new Frame((TextureRegion)textureAtlas.findRegion("guarddownwalk02")));

		// guard idle
		guardStandDownAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guarddownstand01")));
		guardStandUpAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardupstand01")));
		guardStandRightAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardrightstand01")));
		guardStandLeftAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardleftstand01")));

		// guard attack
		guardAttackDownAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guarddownattack01")));
		guardAttackUpAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardupattack01")));
		guardAttackRightAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardrightattack01")));
		guardAttackLeftAnim = new Animation(0.1f, true, 
				new Frame((TextureRegion)textureAtlas.findRegion("guardleftattack01")));

	}
}

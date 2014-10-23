package com.androidto.cardboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import rajawali.Object3D;
import rajawali.animation.Animation.RepeatMode;
import rajawali.animation.SplineTranslateAnimation3D;
import rajawali.curves.CatmullRomCurve3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.NormalMapTexture;
import rajawali.materials.textures.Texture;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderAWD;
import rajawali.terrain.SquareTerrain;
import rajawali.terrain.TerrainGenerator;
import rajawali.vr.RajawaliVRRenderer;

public class RajawaliVRExampleRenderer extends RajawaliVRRenderer {
	private SquareTerrain mTerrain;

	public RajawaliVRExampleRenderer(Context context) {
		super(context);
	}
	
	@Override
	public void initScene() {
		DirectionalLight light = new DirectionalLight(0.2f, -1f, 0f);
		light.setPower(.7f);
		getCurrentScene().addLight(light);

		light = new DirectionalLight(0.2f, 1f, 0f);
		light.setPower(1f);
		getCurrentScene().addLight(light);

		
		getCurrentCamera().setFarPlane(1000);
		
		getCurrentScene().setBackgroundColor(0xdddddd);
		
		createTerrain();
		
		try {
			getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);
			
			LoaderAWD loader = new LoaderAWD(mContext.getResources(), mTextureManager, R.raw.space_cruiser);
			loader.parse();
			
			Material cruiserMaterial = new Material();
			cruiserMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
			cruiserMaterial.setColorInfluence(0);
			cruiserMaterial.enableLighting(true);
			cruiserMaterial.addTexture(new Texture("spaceCruiserTex", R.drawable.space_cruiser_4_color_1));
			
			Object3D spaceCruiser = loader.getParsedObject();
			spaceCruiser.setMaterial(cruiserMaterial);
			spaceCruiser.setZ(-6);
			spaceCruiser.setY(1);
			getCurrentScene().addChild(spaceCruiser);
			
			spaceCruiser = spaceCruiser.clone(true);
			spaceCruiser.setZ(-12);
			spaceCruiser.setY(-3);
			spaceCruiser.setRotY(180);
			getCurrentScene().addChild(spaceCruiser);
			
			loader = new LoaderAWD(mContext.getResources(), mTextureManager, R.raw.dark_fighter);
			loader.parse();
			
			Material darkFighterMaterial = new Material();
			darkFighterMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
			darkFighterMaterial.setColorInfluence(0);
			darkFighterMaterial.enableLighting(true);
			darkFighterMaterial.addTexture(new Texture("darkFighterTex", R.drawable.dark_fighter_6_color));
			
			Object3D darkFighter = loader.getParsedObject();
			darkFighter.setMaterial(darkFighterMaterial);
			darkFighter.setZ(-6);
			getCurrentScene().addChild(darkFighter);
			
			CatmullRomCurve3D path = new CatmullRomCurve3D();
			path.addPoint(new Vector3(0, -5, -10));
			path.addPoint(new Vector3(10, -5, 0));
			path.addPoint(new Vector3(0, -4, 8));
			path.addPoint(new Vector3(-16, -6, 0));
			path.isClosedCurve(true);
			
			SplineTranslateAnimation3D anim = new SplineTranslateAnimation3D(path);
			anim.setDurationMilliseconds(44000);
			anim.setRepeatMode(RepeatMode.INFINITE);
			// -- orient to path
			anim.setOrientToPath(true);
			anim.setTransformable3D(darkFighter);
			getCurrentScene().registerAnimation(anim);
			anim.play();
			
			loader = new LoaderAWD(mContext.getResources(), mTextureManager, R.raw.capital);
			loader.parse();
			
			Material capitalMaterial = new Material();
			capitalMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
			capitalMaterial.setColorInfluence(0);
			capitalMaterial.enableLighting(true);
			capitalMaterial.addTexture(new Texture("capitalTex", R.drawable.hullw));
			capitalMaterial.addTexture(new NormalMapTexture("capitalNormTex", R.drawable.hulln));
			
			Object3D capital = loader.getParsedObject();
			capital.setMaterial(capitalMaterial);
			capital.setScale(18);
			getCurrentScene().addChild(capital);
			
			path = new CatmullRomCurve3D();
			path.addPoint(new Vector3(0, 13, 34));
			path.addPoint(new Vector3(34, 13, 0));
			path.addPoint(new Vector3(0, 13, -34));
			path.addPoint(new Vector3(-34, 13, 0));
			path.isClosedCurve(true);
			
			anim = new SplineTranslateAnimation3D(path);
			anim.setDurationMilliseconds(60000);
			anim.setRepeatMode(RepeatMode.INFINITE);
			anim.setOrientToPath(true);
			anim.setTransformable3D(capital);
			getCurrentScene().registerAnimation(anim);
			anim.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		super.initScene();
	}
	
	public void createTerrain() {
		//
		// -- Load a bitmap that represents the terrain. Its color values will
		//    be used to generate heights.
		//

		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.terrain);

		try {
			SquareTerrain.Parameters terrainParams = SquareTerrain.createParameters(bmp);
			// -- set terrain scale
			terrainParams.setScale(4f, 54f, 4f);
			// -- the number of plane subdivisions
			terrainParams.setDivisions(128);
			// -- the number of times the textures should be repeated
			terrainParams.setTextureMult(4);
			//
			// -- Terrain colors can be set by manually specifying base, middle and
			//    top colors.   
			//
			// --  terrainParams.setBasecolor(Color.argb(255, 0, 0, 0));
			//     terrainParams.setMiddleColor(Color.argb(255, 200, 200, 200));
			//     terrainParams.setUpColor(Color.argb(255, 0, 30, 0));
			//
			// -- However, for this example we'll use a bitmap
			//
			terrainParams.setColorMapBitmap(bmp);
			//
			// -- create the terrain
			//
			mTerrain = TerrainGenerator.createSquareTerrainFromBitmap(terrainParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// -- The bitmap won't be used anymore, so get rid of it.
		//
		bmp.recycle();

		//
		// -- A normal map material will give the terrain a bit more detail.
		//
		Material material = new Material();
		material.enableLighting(true);
		material.useVertexColors(true);
		material.setDiffuseMethod(new DiffuseMethod.Lambert());
		try {
			Texture groundTexture = new Texture("ground", R.drawable.ground);
			groundTexture.setInfluence(.5f);
			material.addTexture(groundTexture);
			material.addTexture(new NormalMapTexture("groundNormalMap", R.drawable.groundnor));
			material.setColorInfluence(0);
		} catch (TextureException e) {
			e.printStackTrace();
		}

		//
		// -- Blend the texture with the vertex colors
		//
		material.setColorInfluence(.5f);
		mTerrain.setY(-100);
		mTerrain.setMaterial(material);

		getCurrentScene().addChild(mTerrain);
	}
}

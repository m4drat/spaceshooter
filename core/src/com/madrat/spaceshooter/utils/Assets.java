package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Assets class
 * All resources are here
 * Every resource must be loaded using asset manager (load -> get -> unload)
 */

public class Assets {
    public static AssetManager manager;

    public static final Color lightGreen_1 = new Color(0x6ec115ff);
    public static final Color lightGreen_2 = new Color(0x94dd99ff);
    public static final Color lightGreen_3 = new Color(0x30db88ff);

    public static final Color lightPinky = new Color(0xe57575ff);

    public static final Color lightAquamarine = new Color(0x7a9af1);
    public static final Color lightBlue_1 = new Color(0x7a9af1ff);
    public static final Color lightBlue_2 = new Color(0x1f8be2ff);
    public static final Color lightBlue_3 = new Color(0x4699E0ff);
    public static final Color lightBlue_4 = new Color(0x4da9eaff);
    public static final Color lightBlue_5 = new Color(0x4da9ffff);

    public static final Color lightYellow_1 = new Color(0xceb963ff);
    public static final Color lightYellow_2 = new Color(0xfce959ff);

    // public static String apiAddress = "";
    public static final String protocol = "http://";
    public static final String apiEndpointUpdateScore = "/updatescore", apiEndpointScoreboard = "/scoreboard";
    public static final String apiServerPort = ":8080";

    public static final String settingsXmlFile = "spacegame.xml", settingsFile = "spacegame";

    public static final String playBtn = "buttons/playBtn.png", playBtnPressed = "buttons/playBtn_pressed.png";
    public static final String shopBtn = "buttons/shopBtn.png", shopBtnPressed = "buttons/shopBtn_pressed.png";
    public static final String settBtn = "buttons/settBtn.png", settBtnPressed = "buttons/settBtn_pressed.png";
    public static final String exitBtn = "buttons/exitBtn.png", exitBtnPressed = "buttons/exitBtn_pressed.png";
    public static final String healTileUp = "buttons/shopPlanes/powerUps/healTileUp.png", healTileDown = "buttons/shopPlanes/powerUps/healTileDown.png";
    public static final String ammoTileUp = "buttons/shopPlanes/powerUps/ammoTileUp.png", ammoTileDown = "buttons/shopPlanes/powerUps/ammoTileDown.png";
    public static final String shieldTileUp = "buttons/shopPlanes/powerUps/shieldTileUp.png", shieldTileDown = "buttons/shopPlanes/powerUps/shieldTileDown.png";
    public static final String closeBtnUp = "buttons/closeUp.png", closeBtnDown = "buttons/closeDown.png";
    public static final String buyMenuPlane = "planeLayout/planeBuy.png";

    public static final String checkBoxImageUp = "buttons/checkBoxImageUp.png", checkBoxImageDown = "buttons/checkBoxImageDown.png";

    public static final String blank = "objects/anything/blank.png", blankBig = "objects/anything/blankBig.png";
    public static final String pauseBtnUp = "objects/anything/pauseBtnUp.png", pauseBtnDown = "objects/anything/pauseBtnDown.png";

    public static final String arrow = "buttons/arrow.png";

    public static final String asteroid1Animation = "objects/asteroids/asteroid1.png", asteroid2Animation = "objects/asteroids/asteroid2.png";

    public static final String explosion1 = "objects/explosions/explosion1.png", explosion2 = "objects/explosions/explosion2.png";
    public static final String explosion3 = "objects/explosions/explosion3.png", explosion4 = "objects/explosions/explosion4.png";
    public static final String explosion5 = "objects/explosions/explosion5.png", explosion6 = "objects/explosions/explosion6.png";
    public static final String explosion7 = "objects/explosions/explosion7.png", explosion8 = "objects/explosions/explosion8.png";

    public static final String nebula1 = "objects/nebulas/nebula1.png", nebula2 = "objects/nebulas/nebula2.png";

    public static final String mars1 = "objects/planets/mars1.png", aqua1 = "objects/planets/aqua1.png";
    public static final String earth1 = "objects/planets/earth1.png", jupiter1 = "objects/planets/jupiter1.png";
    public static final String neptune1 = "objects/planets/neptune1.png";

    public static final String star1 = "objects/stars/star1.png", star2 = "objects/stars/star2.png", star3 = "objects/stars/star3.png";

    public static final String backgroundSpace = "background/space.png", BackgroundLightStars = "background/lightStars.png";

    public static final String emulogicfnt = "font/emulogic.fnt", emulogicpng = "font/emulogic.png";

    public static final String uiskin = "ui/uiskin.json";

    public static final String ship0Animation = "ships/spaceShips/spaceship0_animation.png", ship1Animation = "ships/spaceShips/spaceship1_animation.png";
    public static final String ship2Animation = "ships/spaceShips/spaceship2_animation.png", ship3Animation = "ships/spaceShips/spaceship3_animation.png";
    public static final String ship4Animation = "ships/spaceShips/spaceship4_animation.png", ship5Animation = "ships/spaceShips/spaceship5_animation.png";
    public static final String ship6Animation = "ships/spaceShips/spaceship6_animation.png", ship7Animation = "ships/spaceShips/spaceship7_animation.png";
    public static final String ship8Animation = "ships/spaceShips/spaceship8_animation.png", ship9Animation = "ships/spaceShips/spaceship9_animation.png";
    public static final String ship10Animation = "ships/spaceShips/spaceship10_animation.png", ship11Animation = "ships/spaceShips/spaceship11_animation.png";
    public static final String ship12Animation = "ships/spaceShips/spaceship12_animation.png", ship13Animation = "ships/spaceShips/spaceship13_animation.png";

    public static final String bullet1 = "ships/bullets/bullet1.png", bullet2 = "ships/bullets/bullet2.png", rocket1 = "ships/bullets/rocket1.png";

    public static final String healPowerUp = "objects/powerUps/healPowerUp.png", ammoPowerUp = "objects/powerUps/ammoPowerUp.png";
    public static final String shieldPowerUp = "objects/powerUps/shieldPowerUp.png";

    public static final String shipConfigs = "shipConfigs.json", currentState = "currentState.json";


    public static void loadFont() {
        manager.load(emulogicfnt, BitmapFont.class);
    }

    public static void loadExplosions() {
        manager.load(explosion2, Texture.class);
        manager.load(explosion3, Texture.class);
    }

    public static void loadAsteroids() {
        manager.load(asteroid2Animation, Texture.class);
    }

    public static void loadUiButtons() {
        manager.load(blank, Texture.class);
        manager.load(blankBig, Texture.class);
        manager.load(pauseBtnUp, Texture.class);
        manager.load(pauseBtnDown, Texture.class);

        manager.load(arrow, Texture.class);
        manager.load(buyMenuPlane, Texture.class);

        manager.load(healTileUp, Texture.class);
        manager.load(healTileDown, Texture.class);
        manager.load(ammoTileUp, Texture.class);
        manager.load(ammoTileDown, Texture.class);
        manager.load(shieldTileUp, Texture.class);
        manager.load(shieldTileDown, Texture.class);

        manager.load(closeBtnUp, Texture.class);
        manager.load(closeBtnDown, Texture.class);

        manager.load(checkBoxImageDown, Texture.class);
        manager.load(checkBoxImageUp, Texture.class);
    }

    public static void loadBackground() {
        manager.load(backgroundSpace, Texture.class);
        manager.load(star1, Texture.class);
        manager.load(star2, Texture.class);
        manager.load(star3, Texture.class);
        manager.load(mars1, Texture.class);
        manager.load(earth1, Texture.class);
        manager.load(jupiter1, Texture.class);
    }

    public static void loadPowerUps() {
        manager.load(healPowerUp, Texture.class);
        manager.load(ammoPowerUp, Texture.class);
        manager.load(shieldPowerUp, Texture.class);
    }

    public static void loadShips() {
        manager.load(ship0Animation, Texture.class);
        manager.load(ship1Animation, Texture.class);
        manager.load(ship2Animation, Texture.class);
        manager.load(ship3Animation, Texture.class);
        manager.load(ship4Animation, Texture.class);
        manager.load(ship5Animation, Texture.class);
        manager.load(ship6Animation, Texture.class);
        manager.load(ship7Animation, Texture.class);
        manager.load(ship8Animation, Texture.class);
        manager.load(ship9Animation, Texture.class);
        manager.load(ship10Animation, Texture.class);
        manager.load(ship11Animation, Texture.class);
        manager.load(ship12Animation, Texture.class);
        manager.load(ship13Animation, Texture.class);
    }

    public static void loadBullets() {
        manager.load(bullet1, Texture.class);
        manager.load(bullet2, Texture.class);
        manager.load(rocket1, Texture.class);
    }

    public static void loadSkin() {
        manager.load(uiskin, Skin.class);
    }

    public static void unloadFont() {
        manager.unload(emulogicfnt);
    }

    public static void unloadExplosions() {
        manager.unload(explosion2);
        manager.unload(explosion3);
    }

    public static void unloadAsteroids() {
        manager.unload(asteroid2Animation);
    }

    public static void unloadUiButtons() {
        manager.unload(blank);
        manager.unload(blankBig);
        manager.unload(pauseBtnUp);
        manager.unload(pauseBtnDown);

        manager.unload(arrow);
        manager.unload(buyMenuPlane);

        manager.unload(healTileUp);
        manager.unload(healTileDown);
        manager.unload(ammoTileUp);
        manager.unload(ammoTileDown);
        manager.unload(shieldTileUp);
        manager.unload(shieldTileDown);

        manager.unload(closeBtnUp);
        manager.unload(closeBtnDown);

        manager.unload(checkBoxImageDown);
        manager.unload(checkBoxImageUp);
    }

    public static void unloadBackground() {
        manager.unload(backgroundSpace);
        manager.unload(star1);
        manager.unload(star2);
        manager.unload(star3);
        manager.unload(mars1);
        manager.unload(earth1);
        manager.unload(jupiter1);
        // manager.unload(neptune1, Texture.class);
    }

    public static void unloadPowerUps() {
        manager.unload(healPowerUp);
        manager.unload(ammoPowerUp);
        manager.unload(shieldPowerUp);
    }

    public static void unloadShips() {
        manager.unload(ship0Animation);
        manager.unload(ship1Animation);
        manager.unload(ship2Animation);
        manager.unload(ship3Animation);
        manager.unload(ship4Animation);
        manager.unload(ship5Animation);
        manager.unload(ship6Animation);
        manager.unload(ship7Animation);
        manager.unload(ship8Animation);
        manager.unload(ship9Animation);
        manager.unload(ship10Animation);
        manager.unload(ship11Animation);
        manager.unload(ship12Animation);
        manager.unload(ship13Animation);
    }

    public static void unloadBullets() {
        manager.unload(bullet1);
        manager.unload(bullet2);
        manager.unload(rocket1);
    }

    public static void unloadSkin() {
        manager.unload(uiskin);
    }

    public static void dispose() {
        manager.dispose();
    }
}

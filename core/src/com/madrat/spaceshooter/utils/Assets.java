package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    public static final AssetManager manager = new AssetManager();

    public static final String playBtn = "buttons/playBtn.png", playBtnPressed = "buttons/playBtn_pressed.png";
    public static final String shopBtn = "buttons/shopBtn.png", shopBtnPressed = "buttons/shopBtn_pressed.png";
    public static final String settBtn = "buttons/settBtn.png", settBtnPressed = "buttons/settBtn_pressed.png";
    public static final String exitBtn = "buttons/exitBtn.png", exitBtnPressed = "buttons/exitBtn_pressed.png";

    public static final String blank = "objects/anything/blank.png", pauseBtnUp = "objects/anything/pauseBtnUp.png";
    public static final String pauseBtnDown = "objects/anything/pauseBtnDown.png";

    public static final String asteroid1Animation = "objects/asteroids/asteroid1.png", asteroid2Animation = "objects/asteroids/asteroid2.png";

    public static final String explosion1 = "objects/explosions/explosion1.png", explosion2 = "objects/explosions/explosion2.png";
    public static final String explosion3 = "objects/explosions/explosion3.png", explosion4 = "objects/explosions/explosion4.png";
    public static final String explosion5 = "objects/explosions/explosion5.png", explosion6 = "objects/explosions/explosion6.png";
    public static final String explosion7 = "objects/explosions/explosion7.png", explosion8 = "objects/explosions/explosion8.png";

    public static final String nebula1 = "objects/nebulas/nebula1.png", nebula2 = "objects/nebulas/nebula2.png";

    public static final String mars1 = "objects/planets/mars1.png", aqua1 = "objects/planets/aqua1.png";
    public static final String earth1 = "objects/planets/earth1.png", jupiter1 = "objects/planets/jupiter1.png";

    public static final String star1 = "objects/stars/star1.png", star2 = "objects/stars/star2.png", star3 = "objects/stars/star3.png";

    public static final String backgroundSpace = "background/space.png", BackgroundLightStars = "background/lightStars.png";

    public static final String emulogicfnt = "font/emulogic.fnt", emulogicpng = "font/emulogic.png";

    public static final String uiskin = "ui/uiskin.json";

    public static final String ship1Animation = "ships/spaceShips/spaceship_animation.png", ship2Animation = "ships/spaceShips/spaceship2_animation.png";
    public static final String ship3Animation = "ships/spaceShips/spaceship3_animation.png", ship4Animation = "ships/spaceShips/spaceship4_animation.png";
    public static final String ship5Animation = "ships/spaceShips/spaceship5_animation.png", ship6Animation = "ships/spaceShips/spaceship6_animation.png";
    public static final String ship7Animation = "ships/spaceShips/spaceship7_animation.png", ship8Animation = "ships/spaceShips/spaceship8_animation.png";
    public static final String ship9Animation = "ships/spaceShips/spaceship9_animation.png", ship10Animation = "ships/spaceShips/spaceship10_animation.png";
    public static final String ship11Animation = "ships/spaceShips/spaceship11_animation.png";

    public static final String bullet1 = "ships/bullets/bullet1.png", bullet2 = "ships/bullets/bullet2.png", rocket1 = "ships/bullets/rocket1.png";

    public static final String healPowerUp = "objects/powerUps/healPowerUp.png", ammoPowerUp = "objects/powerUps/ammoPowerUp.png";

    public static void load() {

    }

    public static void dispose() {
        manager.dispose();
    }
}

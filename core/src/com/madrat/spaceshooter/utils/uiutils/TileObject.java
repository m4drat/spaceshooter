package com.madrat.spaceshooter.utils.uiutils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;

/**
 * Simple tile class
 */

public class TileObject extends Table {

    private Button button;
    private Label text;
    private Image icon;

    public TileObject(String btnText, Skin skin, String iconPath, float tileWidth, float tileHeight, int realIconWidth, int realIconHeight) {
        super();

        // set size of parent table
        this.setSize(tileWidth, tileHeight);

        // Create text label
        this.text = new Label(btnText, skin);

        // create icon, which will be placed onto button
        this.icon = new Image(new TextureRegion(Assets.manager.get(iconPath, Texture.class), 0, 0, realIconWidth, realIconHeight));

        // create actual button
        this.button = new Button(skin);
        this.button.setSize(tileWidth, tileHeight);
        this.button.add(icon).row();
        this.button.add(this.text);

        // add button to parent table
        this.add(button).size(tileWidth, tileHeight);

        // some debug settings
        if (BuildConfig.UIDEBUG) {
            this.button.setDebug(true);
            this.setDebug(true);
        }
    }

    public Button getButton() {
        return button;
    }

    public Cell<Label> getLabel() {
        return this.button.getCell(text);
    }

    public Cell<Image> getIcon() {
        return this.button.getCell(icon);
    }
}

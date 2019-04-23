package com.madrat.spaceshooter.utils.uiutils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.madrat.spaceshooter.utils.Assets;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class CheckBox extends Table {

    // button properties
    private ImageButton checkBox;
    private TextureRegionDrawable imageUp, imageDown;

    // text
    private Label checkBoxText;

    // state
    private boolean isChecked;

    public CheckBox(Skin skin, String checkBoxText, String checkBoxImageUp, String checkBoxImageDown, int checkBoxWidth, int checkBoxHeight) {
        super();

        imageUp = new TextureRegionDrawable(Assets.manager.get(checkBoxImageUp, Texture.class));
        imageDown = new TextureRegionDrawable(Assets.manager.get(checkBoxImageDown, Texture.class));

        checkBox = new ImageButton(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(checkBoxImageUp, Texture.class))), new TextureRegionDrawable(new TextureRegion(Assets.manager.get(checkBoxImageDown, Texture.class))));
        checkBox.getImageCell().size(checkBoxWidth * SCALE_FACTOR, checkBoxHeight * SCALE_FACTOR);

        this.checkBoxText = new Label(checkBoxText, skin);
        this.checkBoxText.setFontScale(SCALE_FACTOR / 1.45f);

        this.add(this.checkBox);
        this.add(this.checkBoxText).padLeft(12 * SCALE_FACTOR);
    }

    public ImageButton getCheckBox() {
        return checkBox;
    }

    public Label getCheckBoxText() {
        return checkBoxText;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        if (isChecked) { // button checked
            ImageButton.ImageButtonStyle _oldStyle = checkBox.getStyle();
            _oldStyle.imageUp = imageDown;
            _oldStyle.imageDown = imageUp;
            checkBox.setStyle(_oldStyle);
        } else {
            ImageButton.ImageButtonStyle _oldStyle = checkBox.getStyle();
            _oldStyle.imageUp = imageUp;
            _oldStyle.imageDown = imageDown;
            checkBox.setStyle(_oldStyle);
        }
    }
}

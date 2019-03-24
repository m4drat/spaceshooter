package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class DialogAlert extends Dialog {

    private float dialog_padding = 20f;
    private float button_pad_l = 15f;
    private float button_pad_r = 15f;
    private float button_pad_b = 15f;
    private float button_pad_t = 25f;

    private Skin skin;

    public Label label;
    public TextButton buttonYes;
    public TextButton buttonNo;

    public DialogAlert(String title, Skin skin) {
        super(title, skin);
        setup();
        this.skin = skin;
    }

    private void setup() {
        setModal(false);
        setMovable(false);
        setResizable(false);

        padTop(dialog_padding * SCALE_FACTOR).padLeft(dialog_padding * SCALE_FACTOR).padRight(dialog_padding * SCALE_FACTOR);
    }

    @Override
    public DialogAlert text(String text) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(Assets.emulogicfnt), Gdx.files.internal(Assets.emulogicpng), false);
        label = new Label(text, new Label.LabelStyle(font, Color.WHITE));

        label.setAlignment(Align.center);
        label.setWrap(false);
        label.setFontScale(0.7f * SCALE_FACTOR);

        text(label);
        return this;
    }

    public DialogAlert yesButton(String buttonText, InputListener listener) {
        buttonYes = new TextButton(buttonText, this.skin);
        buttonYes.addListener(listener);

        buttonYes.padTop(button_pad_t * SCALE_FACTOR);
        // buttonYes.padLeft(button_pad_l);
        // buttonYes.padRight(button_pad_r);
        buttonYes.padBottom(button_pad_b * SCALE_FACTOR);

        button(buttonYes);

        return this;
    }

    public DialogAlert noButton(String buttonText, InputListener listener) {
        buttonNo = new TextButton(buttonText, this.skin);
        buttonNo.addListener(listener);

        buttonNo.padTop(button_pad_t * SCALE_FACTOR);
        buttonNo.padLeft((button_pad_l + 40) * SCALE_FACTOR);
        // buttonNo.padRight(button_pad_r);
        buttonNo.padBottom(button_pad_b * SCALE_FACTOR);

        button(buttonNo);

        return this;
    }
}

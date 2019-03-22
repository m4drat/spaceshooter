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
import com.madrat.spaceshooter.MainGame;

public class DialogAlert extends Dialog {

    private float dialog_width = 280f;
    private float dialog_padding = 15f;
    private float button_pad_l = 15f;
    private float button_pad_r = 15f;
    private float button_pad_b = 15f;
    private float button_pad_t = 10f;

    private Skin skin;

    public DialogAlert(String title, Skin skin) {
        super(title, skin);
        setup();
        this.skin = skin;
    }

    private void setup() {
        // getButtonTable().defaults().height(dialog_height);
        // getContentTable().defaults().width(dialog_width);

        setModal(false);
        setMovable(false);
        setResizable(false);

        // getBackground().setMinHeight(130);
        // getButtonTable().align(Align.center);
        padTop(dialog_padding).padLeft(dialog_padding).padRight(dialog_padding);
    }

    @Override
    public DialogAlert text(String text) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(Assets.emulogicfnt), Gdx.files.internal(Assets.emulogicpng), false);
        Label label = new Label(text, new Label.LabelStyle(font, Color.WHITE));

        label.setAlignment(Align.center);
        label.setWrap(false);
        label.setFontScale(0.6f);

        text(label);
        return this;
    }

    public DialogAlert yesButton(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, this.skin);
        button.addListener(listener);

        button.padTop(button_pad_t);
        button.padLeft(button_pad_l);
        button.padRight(button_pad_r);
        button.padBottom(button_pad_b);

        button(button);

        return this;
    }

    public DialogAlert noButton(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, this.skin);
        button.addListener(listener);

        button.padTop(button_pad_t);
        button.padLeft(button_pad_l);
        button.padRight(button_pad_r);
        button.padBottom(button_pad_b);

        button(button);

        return this;
    }
}

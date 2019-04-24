package com.madrat.spaceshooter.utils.uiutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class TextInputDialog extends Dialog {

    private Stage oldStage;
    private TextButton actBtn;

    private Table parent;
    private Label objectLabel;
    private TextField textField;

    private float width, height;
    private InputMultiplexer multiplexer;

    public TextInputDialog(Skin skin, final Stage oldStage, String dialogText, float fontScale, float width, float height) {
        super("", skin);
        setup();

        this.oldStage = oldStage;

        this.width = width;
        this.height = height;

        parent = new Table();

        if (BuildConfig.UIDEBUG) {
            this.debugAll();
            parent.debug();
        }

        objectLabel = new Label(dialogText, skin);
        objectLabel.setColor(Assets.lightBlue_5);
        objectLabel.setFontScale(fontScale * SCALE_FACTOR);
        parent.add(objectLabel).padRight(20 * SCALE_FACTOR).padTop(10 * SCALE_FACTOR).padLeft(20 * SCALE_FACTOR).row();

        textField = new TextField("", skin);
        textField.setMaxLength(20);
        textField.scaleBy(SCALE_FACTOR);
        // textField.getStyle().font.getData().setScale(SCALE_FACTOR);
        // textField.setSize(width * SCALE_FACTOR, (height * SCALE_FACTOR) / 4);
        parent.add(textField).size(width * SCALE_FACTOR, (height * SCALE_FACTOR) / 4).padRight(20 * SCALE_FACTOR).padTop(10 * SCALE_FACTOR).padLeft(20 * SCALE_FACTOR).row();

        this.actBtn = new TextButton("OK", skin);
        actBtn.getLabel().setFontScale(1.1f * SCALE_FACTOR);
        parent.add(actBtn).padLeft(20 * SCALE_FACTOR).padTop(20 * SCALE_FACTOR).padBottom(20 * SCALE_FACTOR).row();

        // Add main table to stage
        add(parent);

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    return true;
                }
                return false;
            }
        };
        multiplexer = new InputMultiplexer(backProcessor, oldStage);
    }

    private void setup() {
        Sprite background = new Sprite(Assets.manager.get(Assets.buyMenuPlane, Texture.class));
        background.setSize(width, height);
        setBackground(new SpriteDrawable(background));

        setModal(true);
        setMovable(true);
        setResizable(true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(oldStage);
        super.hide();
    }

    public void hide(float fadeoutDuration) {
        Gdx.input.setInputProcessor(oldStage);
        super.hide(sequence(Actions.alpha(0), Actions.fadeIn(fadeoutDuration, Interpolation.fade)));
    }

    @Override
    public Dialog show(Stage stage) {
        Gdx.input.setInputProcessor(multiplexer);
        return super.show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
    }

    public Dialog show(Stage stage, float fadeoutDuration) {
        Gdx.input.setInputProcessor(multiplexer);
        return super.show(stage, sequence(Actions.alpha(0), Actions.fadeIn(fadeoutDuration, Interpolation.fade)));
    }

    public TextButton getActBtn() {
        return actBtn;
    }

    public Label getObjectLabel() {
        return objectLabel;
    }

    public InputMultiplexer getMultiplexer() {
        return multiplexer;
    }

    public TextField getTextField() {
        return textField;
    }
}

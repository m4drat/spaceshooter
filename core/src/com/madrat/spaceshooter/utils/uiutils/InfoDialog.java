package com.madrat.spaceshooter.utils.uiutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.madrat.spaceshooter.utils.Assets;
import com.madrat.spaceshooter.utils.BuildConfig;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.madrat.spaceshooter.MainGame.SCALE_FACTOR;

public class InfoDialog extends Dialog {

    private Stage oldStage;
    private Skin skin;
    private TextButton actBtn;

    private Table parent;
    private Label objectLabel;
    Array<DescriptionRow> descriptionRows;

    private float width, height;
    private InputMultiplexer multiplexer;

    public InfoDialog(Skin skin, final Stage oldStage, String objectName, float ObjectLabelFontScale, String ObjectIcon, int realIconWidth, int realIconHeight, float iconWidth, float iconHeight, Array<DescriptionRow> descriptionRows, DescriptionRow priceLabel, String buttonText, float width, float height) {
        super("", skin);
        setup();

        this.skin = skin;
        this.oldStage = oldStage;

        this.width = width;
        this.height = height;
        this.descriptionRows = descriptionRows;

        parent = new Table();

        if (BuildConfig.UIDEBUG) {
            this.debugAll();
            parent.debug();
        }

        objectLabel = new Label(objectName, skin);
        objectLabel.setColor(Assets.lightBlue_5);
        objectLabel.setFontScale(ObjectLabelFontScale * SCALE_FACTOR);
        parent.add(objectLabel).padRight(30 * SCALE_FACTOR).padTop(10 * SCALE_FACTOR).padLeft(80 * SCALE_FACTOR);

        ImageButton close = new ImageButton(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.closeBtnUp, Texture.class))), new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.closeBtnDown, Texture.class))));
        close.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hide();
                return true;
            }
        });
        close.getImageCell().size(42 * SCALE_FACTOR, 42 * SCALE_FACTOR);
        parent.add(close).padTop(10 * SCALE_FACTOR).padRight(10 * SCALE_FACTOR);
        parent.row();

        Image icon = new Image(new TextureRegion(Assets.manager.get(ObjectIcon, Texture.class), 0, 0, realIconWidth, realIconHeight));
        parent.add(icon).padTop(30 * SCALE_FACTOR).size(iconWidth * SCALE_FACTOR, iconHeight * SCALE_FACTOR).padLeft(40 * SCALE_FACTOR).row();

        if (descriptionRows != null) {
            for (DescriptionRow descRow : descriptionRows) {
                parent.add(descRow.description).align(Align.left).padLeft(descRow.padLeft * SCALE_FACTOR).padRight(descRow.padRight * SCALE_FACTOR).padTop(descRow.padTop * SCALE_FACTOR).padBottom(descRow.padBottom * SCALE_FACTOR).row();
            }
        }

        if (priceLabel != null)
            parent.add(priceLabel.description).align(Align.left).padLeft(25 * SCALE_FACTOR).padTop(30 * SCALE_FACTOR).row();

        this.actBtn = new TextButton(buttonText, skin);
        actBtn.getLabel().setFontScale(1.1f * SCALE_FACTOR);
        parent.add(actBtn).padLeft(40 * SCALE_FACTOR).padTop(20 * SCALE_FACTOR).padBottom(30 * SCALE_FACTOR).row();

        // Add main table to stage
        add(parent);

        setBackButton();

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    hide();
                    return true;
                }
                return false;
            }
        };
        multiplexer = new InputMultiplexer(backProcessor, oldStage);
    }

    private void setBackButton() {
        // Close dialog if user clicks anywhere outside current dialog
        InputListener inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if ((x < 0 || x > getWidth() || y < 0 || y > getHeight())) {
                    hide();
                    event.cancel();
                    return true;
                }
                return false;
            }
        };
        addListener(inputListener);
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

    public float getWidthOvverided() {
        return width;
    }

    public void setWidthOvverided(float width) {
        this.width = width;
    }

    public float getHeightOvverided() {
        return height;
    }

    public void setHeightOvverided(float height) {
        this.height = height;
    }

    public Label getObjectLabel() {
        return objectLabel;
    }

    public InputMultiplexer getMultiplexer() {
        return multiplexer;
    }

    public Array<Cell> getDescriptionRowCells() {
        Array<Cell> cells = new Array<Cell>();

        for (DescriptionRow descriptionRow : descriptionRows) {
            cells.add(parent.getCell(descriptionRow.description));
        }

        return cells;
    }
}

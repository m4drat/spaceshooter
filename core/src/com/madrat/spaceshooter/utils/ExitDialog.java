package com.madrat.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ExitDialog {

    private Dialog exitDialog;

    public ExitDialog(Skin skin) {
        this.exitDialog = new Dialog("", skin);
        this.exitDialog.getTitleLabel().setWrap(false);
        this.exitDialog.getTitleLabel().setFontScale(0.6f);
        this.exitDialog.getTitleLabel().setText("Do you really\nwant to exit?");
        this.exitDialog.getBackground().setMinHeight(130);
        this.exitDialog.getButtonTable().align(Align.center);
        this.exitDialog.padTop(80).padLeft(15).padRight(15); // set padding on top of the dialog title
        this.exitDialog.setResizable(false);
        this.exitDialog.setMovable(false);

        // YES / NO buttons
        TextButton yes = new TextButton("YES", skin);
        TextButton no = new TextButton("NO", skin);
        yes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        no.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitDialog.hide();
            }
        });

        // Set position
        yes.padBottom(20);
        no.padBottom(20);
        yes.moveBy(yes.getWidth() / 3, 0);
        no.moveBy(exitDialog.getWidth() + no.getWidth() / 2, 0);

        // Adding actors
        exitDialog.addActor(yes);
        exitDialog.addActor(no);
    }

    public void show(Stage stage) {
        this.exitDialog.show(stage);
    }
}

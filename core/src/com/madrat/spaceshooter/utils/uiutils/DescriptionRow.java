package com.madrat.spaceshooter.utils.uiutils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class DescriptionRow {

    public Label description;
    public int padTop, padBottom, padLeft, padRight;

    public DescriptionRow(int padTop, int padBottom, int padLeft, int padRight) {
        this.padTop = padTop;
        this.padBottom = padBottom;
        this.padLeft = padLeft;
        this.padRight = padRight;
    }
}

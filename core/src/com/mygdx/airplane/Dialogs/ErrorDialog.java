package com.mygdx.airplane.Dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Kwa on 2016-09-24.
 */
public class ErrorDialog extends Dialog {

    Exception exception;

    public ErrorDialog(String title, Skin skin, Exception e) {
        super(title, skin);
        this.exception = e;
        text("Encountered an error of type:\n" + exception.toString());
        button("Ok");
    }

    @Override
    protected void result(Object object) {

    }

}

package com.GF.platform.uikit.widget.chatkeyboard.base.command;

import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFKeyBoardCommand;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFCommandPerformer {

    private GFKeyBoardCommand command = null;
    private static GFCommandPerformer singleton = null;

    private GFCommandPerformer() {
    }

    public static GFCommandPerformer getDefault() {
        if (singleton == null) {
            synchronized (GFCommandPerformer.class) {
                if (singleton == null) {
                    singleton = new GFCommandPerformer();
                }
            }
        }
        return singleton;
    }

    public void setCommand(GFKeyBoardCommand command) {
        this.command = command;
    }

    public void doCommand() {
        if (null != command) {
            command.execute();
        }
    }
}

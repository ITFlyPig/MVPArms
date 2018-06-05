package com.wangyuelin.downloader.app.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.wangyuelin.downloader.widget.InputDialog;

public class InputPopUtil {

    public static InputDialog showChoosePop(Context context , InputDialog.OnConfirmListener listener) {
        InputDialog inputDialog = new InputDialog(context);
        inputDialog.setOnConfirmListener(listener);
        inputDialog.show();
        return inputDialog;

    }
}

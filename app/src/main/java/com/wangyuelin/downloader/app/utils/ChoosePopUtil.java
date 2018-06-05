package com.wangyuelin.downloader.app.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wangyuelin.downloader.widget.ChooseDialog;

public class ChoosePopUtil {

    public static ChooseDialog showChoosePop(Context context , ChooseDialog.OnChooseListener listener) {
        ChooseDialog chooseDialog = new ChooseDialog(context);
        chooseDialog.setOnChooseListener(listener);
        chooseDialog.show();
        return chooseDialog;

    }
}

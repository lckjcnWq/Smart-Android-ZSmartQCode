package com.theswitchbot.common.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.theswitchbot.common.CommonApp;
import com.theswitchbot.common.R;


public class BottomDialog extends AlertDialog{

    protected Context context;

    public BottomDialog(Context context) {
        super(context, R.style.Dialog);
        this.context = context;
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        show();
    }

    public void addView(View view) {
        DisplayMetrics dm = new DisplayMetrics();
        if(context==null){
            context= CommonApp.instance;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        setContentView(view, new LinearLayout.LayoutParams(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    protected View.OnClickListener l;
    public void setOnClickListener(View.OnClickListener l) {
        this.l = l;
    }
}

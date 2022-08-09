package com.theswitchbot.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.theswitchbot.common.R;


public class LoadingDialog extends Dialog{

    private Context context;
    private String message;
    private boolean isShowMessage=true;
    private boolean isCancelable=false;
    private boolean isCancelOutside=false;
    private TextView tvMessageStr;

    public LoadingDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
        init();
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_custom_loading,null);
        tvMessageStr= view.findViewById(R.id.tipTextView);
        if(isShowMessage){
            tvMessageStr.setText(message);
            tvMessageStr.setVisibility(View.VISIBLE);
        }else{
            tvMessageStr.setVisibility(View.GONE);
        }
        setContentView(view);
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelOutside);
    }

    /**
     * 设置提示信息
     * @param message
     * @return
     */

    public void setMessage(String message){
        this.message=message;
        if (tvMessageStr != null){
            tvMessageStr.setText(message);
            tvMessageStr.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置是否显示提示信息
     * @param isShowMessage
     * @return
     */
    public void setShowMessage(boolean isShowMessage){
        this.isShowMessage=isShowMessage;
    }

    /**
     * 设置是否可以按返回键取消
     * @param isCancelable
     * @return
     */

    public void setLoadingCancelable(boolean isCancelable){
        this.isCancelable=isCancelable;
        super.setCancelable(isCancelable);
    }

    /**
     * 设置是否可以取消
     * @param isCancelOutside
     * @return
     */
    public void setCancelOutside(boolean isCancelOutside){
        this.isCancelOutside=isCancelOutside;
    }

}

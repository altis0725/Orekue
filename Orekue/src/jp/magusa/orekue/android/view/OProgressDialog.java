package jp.magusa.orekue.android.view;

import jp.maguro.vs.samon.orekue.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

public class OProgressDialog extends Dialog {

    public static OProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static OProgressDialog show(Context context, CharSequence title, CharSequence message,
            boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static OProgressDialog show(Context context, CharSequence title, CharSequence message,
            boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static OProgressDialog show(Context context, CharSequence title, CharSequence message,
            boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        OProgressDialog dialog = new OProgressDialog(context);
        //dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        //dialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT,
        //        LayoutParams.WRAP_CONTENT));
        ProgressBar pr = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.o_progressbar, null);
        dialog.addContentView(pr, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        try {
            dialog.show();
        } catch (Exception e) {
        }

        return dialog;
    }
    
    public static OProgressDialog show(Context context){
        return show(context, null, null, false, false, null);
    }
    
    public static OProgressDialog show(Context context, boolean cancelable){
        return show(context, null, null, false, cancelable, null);
    }

    public OProgressDialog(Context context) {
        super(context, R.style.TdDialog);
    }
    
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        };
    }
}
package me.mvp.demo.ui.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import me.mvp.demo.R;
import me.mvp.frame.widget.dialog.BaseDialogFragment;

/**
 * 演示对话框
 */
public class DialogDefault extends BaseDialogFragment implements DialogInterface.OnKeyListener {

    // TAG
    public static final String TAG = DialogDefault.class.getSimpleName();

    /**
     * Create a new dialog fragment.
     */
    public static DialogDefault newInstance() {
        DialogDefault fragment = new DialogDefault();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void create(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }


        getDialog().setOnKeyListener(this);
    }

//    @OnClick({R.id.btn_default})
//    void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_default:
//                dismiss();
//                break;
//        }
//    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            dismiss();
        }
        return false;
    }

    @Override
    protected Builder builder() {
        return new Builder()
                .width(LinearLayout.LayoutParams.WRAP_CONTENT)
                .height(LinearLayout.LayoutParams.WRAP_CONTENT)
                .gravity(Gravity.LEFT | Gravity.TOP)
                .canceledOnTouchOutside(true)
                ;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_default;
    }
}
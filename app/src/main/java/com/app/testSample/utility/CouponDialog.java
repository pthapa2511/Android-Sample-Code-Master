package com.app.testSample.utility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.testSample.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CouponDialog extends DialogFragment implements View.OnClickListener{
    public static final String TAG = "CouponDialog";
    private TextView couponCode;
    private String mCouponCode;
    private String mTitle;
    private String mAffiliateUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_100);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        mCouponCode = savedInstanceState.getString("coupon_code");
        mTitle = savedInstanceState.getString("title");
        mAffiliateUrl = savedInstanceState.getString("affiliate_url");
        View viewLayout = inflater.inflate(R.layout.dialog_show_coupon, container);
        TextView dialogTitle = (TextView) viewLayout.findViewById(R.id.dialog_title);
        dialogTitle.setText("Your Coupon Code");
        couponCode = (TextView) viewLayout.findViewById(R.id.text_view_dialog_coupon_code);
        couponCode.setText(mCouponCode);
        viewLayout.findViewById(R.id.button_dialog_close).setOnClickListener(this);
        viewLayout.findViewById(R.id.button_dialog_copy_code).setOnClickListener(this);
        viewLayout.findViewById(R.id.button_dialog_open_website).setOnClickListener(this);
        return viewLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_dialog_close:{
                dismiss();
                break;
            }

            case R.id.button_dialog_copy_code:{
                copyToClipBoard();
                break;
            }

            case R.id.button_dialog_open_website:{
                Uri uri = Uri.parse(mAffiliateUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dismiss();
                break;
            }
        }
    }

    @SuppressLint("NewApi")
    private void copyToClipBoard(){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity()
                    .getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(couponCode.getText().toString());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity()
                    .getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("", couponCode.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
        ToastUtils.showToast(getActivity(), "Code copied to clipboard", Toast.LENGTH_LONG);
    }
}

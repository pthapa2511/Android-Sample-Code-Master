package com.app.testSample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DialogDescription extends DialogFragment {
    public static final String TAG = "DialogDescription";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_100);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_description_layout, container);
        savedInstanceState = getArguments();
        TextView textViewDialogDescription = (TextView) dialogView.findViewById(R.id.text_view_dialog_description);
        textViewDialogDescription.setText(Html.fromHtml(savedInstanceState.getString("description")));

        dialogView.findViewById(R.id.button_dialog_description_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialogView;
    }
}

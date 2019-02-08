package com.app.testSample.support;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.app.testSample.R;
import com.app.testSample.constants.Constants;
import com.app.testSample.controller.ApiRequestController;
import com.app.testSample.model.BaseModel;
import com.app.testSample.utility.ConnectivityUtils;
import com.app.testSample.utility.ToastUtils;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

public class HelpAndSupportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help_and_support);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        configureToolBar(Constants.REQUEST_HELP_SUPPORT);
        if(ConnectivityUtils.isNetworkEnabled(this)){
            showProgressDialog(getString(R.string.progress_loading));
            new ApiRequestController(this,this).getData(Constants.REQUEST_HELP_SUPPORT, null);
        } else {
            ToastUtils.showToast(this, getString(R.string.error_internet_availability));
        }

    }

    @Override
    protected void updateUi(Response response) {
        if(response.isSuccess()){
            switch (response.getDataType()){
                case Constants.REQUEST_HELP_SUPPORT:{
                    BaseModel baseModel = (BaseModel)response.getResponseObject();
                    TextView textView = (TextView)findViewById(R.id.text_view_help_support);
                    textView.setText(Html.fromHtml(baseModel.getMessage()));
                    break;
                }
            }
        }
        removeProgressDialog();
    }
}

package com.app.testSample.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.testSample.offer.OfferDetailActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

//    public static String makeJsonRpcCall(String jsonRpcUrl, JSONObject payload) {
//        return makeJsonRpcCall(jsonRpcUrl, payload, null);
//    }

    public static CharSequence readFile(Activity activity, int id) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(activity.getResources().openRawResource(id)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = in.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            // Chomp the last newline
            buffer.deleteCharAt(buffer.length() - 1);
            return buffer;
        } catch (IOException e) {
            return "";
        } finally {
            closeStream(in);
        }
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

//
//    public static String makeJsonRpcCall(String jsonRpcUrl, JSONObject payload, String authToken) {
//        Log.d("Util", jsonRpcUrl + " " + payload.toString());
//        try {
//            HttpClient client = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(jsonRpcUrl);
//
//            if (authToken != null) {
//                httpPost.addHeader(new BasicHeader("Authorization", "GoogleLogin auth=" + authToken));
//            }
//
//            httpPost.setEntity(new StringEntity(payload.toString(), "UTF-8"));
//
//            HttpResponse httpResponse = client.execute(httpPost);
//            if (200 == httpResponse.getStatusLine().getStatusCode()) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8 * 1024);
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line).append("\n");
//                }
//
//                return sb.toString();
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /*
     * Below function is used to Convert an INPUTSTREAM into the STRING
     */
    public static String iStream_to_String(InputStream is1) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is1), 4096);
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean checkSpecialChar(String text) {
        return SPECIAL_CHAR_PATTERN.matcher(text).matches();
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean checkPassword(String password) {
        return password.length() >= 6;
    }

    public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(
            "^[^!@#=_()?<>%$*-+!/\\.\":;,0123456789]*$"
    );

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if ((imei != null) && (imei.equals(""))) {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        }
        //Used hardcoded value for testing of coupons
        return imei;//"123456789";//
    }


    public static String getDeviceInformation(Context context) {
        String deviceInfo = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        deviceInfo += "DeviceID : " + telephonyManager.getDeviceId() + "\n\r";
        deviceInfo += "PhoneType : " + telephonyManager.getPhoneType() + "\n\r";
        deviceInfo += "NetworkOperatorName : " + telephonyManager.getNetworkOperatorName() + "\n\r";
        deviceInfo += "NetworkType : " + telephonyManager.getNetworkType() + "\n\r";
        deviceInfo += "SimOperatorName : " + telephonyManager.getSimOperatorName() + "\n\r";
        deviceInfo += "MANUFACTURER : " + Build.MANUFACTURER + "\n\r";
        deviceInfo += "MODEL : " + Build.MODEL + "\n\r";
        deviceInfo += "VERSION.SDK_INT : " + Build.VERSION.SDK_INT + "\n\r";

        return deviceInfo;
    }

    public static String saveImgeToCache(Bitmap bitmap, Context context) {
        try {
            File folder = context.getCacheDir();//eDir("pvr", MODE_PRIVATE);
            if (!folder.isDirectory())
                folder.mkdir();
            File f = new File(folder, "collage.png");
            if (!f.exists())
                f.createNewFile();
            //Convert bitmap to byte array
            //Bitmap bitmap = your bitmap;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            @SuppressWarnings("resource")
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            return f.getAbsolutePath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }

    /**
     * @param pContext
     * @return
     */
    public static boolean isNetworkEnabled(Context pContext) {
        boolean _isConnected = false;
        try {
            ConnectivityManager _connectMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (_connectMngr != null) {
                NetworkInfo _activeNetwork = _connectMngr.getActiveNetworkInfo();
                if (_activeNetwork != null) {
                    _isConnected = _activeNetwork.isConnected();
                    Log.i("Network Check", "N/W Type: " + _activeNetwork.getTypeName());
                    Log.i("Network Check", "Connected: " + _isConnected);

                    if (!_isConnected) {
                        Log.i("Network Check", "Connecting: " + _activeNetwork.isConnectedOrConnecting());

                        if (!StringUtils.isNullOrEmpty(_activeNetwork.getReason())) {
                            Log.i("Network Check", "N/W State Reason: " + _activeNetwork.getReason());
                        }
                    }
                } else {
                    Log.i("Network Check", "N/W State Reason: " + "No network is active.");
                }
            } else {
                Log.i("Network Check", "N/W State Reason: " + "Connectivity manager not available");
            }
        } catch (Exception e) {
            Log.e("Network Check", "" + e);
        }
        return _isConnected;
    }

    /**
     * @return
     */
    public boolean isNetworkConnected(Context pContext) {
        ConnectivityManager _connectMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (_connectMngr == null) return false;
        NetworkInfo[] _allNetworks = _connectMngr.getAllNetworkInfo();
        if (_allNetworks == null) return false;
        for (NetworkInfo _anyNetwork : _allNetworks) if (_anyNetwork.isConnected()) return true;
        return false;
    }

    public static AlertDialog errorAlertDialog2(String title, String message, Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        return alertDialog;
    }

    /**
     * This method is used to separate double values
     * @param param
     * @return
     */
    public static String commaSeparatedValue(String param){
        Double value = Double.parseDouble(param);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        String formattedValue = decimalFormat.format(value);
        if(formattedValue.length() == 1){
            return ("0" + formattedValue);
        }else
          return formattedValue;
    }

    /**
     * This method is used to separate double values
     * @param param
     * @return
     */
    public static String commaSeparatedValueWithDecimal(String param){
        Double value = Double.parseDouble(param);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        String formattedValue = decimalFormat.format(value);
        if(formattedValue.length() == 1){
            return ("0" + formattedValue);
        }else
            return formattedValue;
    }



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * @return formattedDate as current date time
     */
    public static Date getCurrentDate() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); /* hh:mm:ss*/
            Date date = df.parse(df.format(c.getTime()));
            return date;
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used for returning the days difference.
     * If days is greater than 7 than SSKEY value will be null
     *
     * @return days
     */
    public static long getDateDifference(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDate = df.parse(date);
        Date oldDate = df.parse(date);//new Date(Preference.getLong(SSKEY_DATE, 0, pContext));
        long different = oldDate.getTime() - getCurrentDate().getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long days = different / daysInMilli;
        return days;
    }

    /**
     * * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

//        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
//            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
//        listView.requestLayout();
    }
}

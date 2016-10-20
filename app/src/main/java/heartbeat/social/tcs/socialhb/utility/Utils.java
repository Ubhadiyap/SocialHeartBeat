package heartbeat.social.tcs.socialhb.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.bean.UserFirebaseToken;


public class Utils {


    //private static boolean sNetworkAvailability;




   /* public static void setNetworkAvailability(boolean value) {
        sNetworkAvailability = value;
    }

    public static boolean getNetworkAvailability() {
        return sNetworkAvailability;
    }*/

    public static void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);


        builder.setMessage(R.string.login_alert_message);
        builder.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        //Toast.makeText(getApplicationContext(),"No is clicked",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    public static void showErrorAlert(Context context, String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);


        builder.setMessage(errorMsg);
        builder.setNegativeButton("Try Again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        //Toast.makeText(getApplicationContext(),"No is clicked",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    public static void setToken(UserFirebaseToken userFirebaseToken, Context context) {

        String token = userFirebaseToken.getToken();

        SharedPreferences preferences = context.getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();

    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MySettings", Context.MODE_PRIVATE);

        String token_key = context.getResources().getString(R.string.token);

        return preferences.getString(token_key, null);
    }

    public static void deleteToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove(String.valueOf(R.string.token)).apply();
    }

    public static void shareAppProcess(Context context) {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved click here to visit https://androidsolved.wordpress.com/ ");
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
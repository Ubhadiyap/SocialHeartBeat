package heartbeat.social.tcs.socialhb.service;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import heartbeat.social.tcs.socialhb.bean.UserFirebaseToken;
import heartbeat.social.tcs.socialhb.bean.Webservice_API;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.utility.Utils;

/**
 * Created by admin on 28/07/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = "MyFirebaseInstanceID";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        //Checking Internet Connection
        CheckInternetConnection checkInternetConnection = new CheckInternetConnection();
        Log.e(TAG, "Outside Check Internet Connection");
        if (checkInternetConnection.checkingInternetConnection(getApplicationContext())) {
            Log.e(TAG, "Inside Check Internet Connection");
            sendRegistrationToServer(refreshedToken);
        }

    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        //Create JSONObjectRequest for Volley

        String url = Webservice_API.without_signin_firebase_push_notification_API;

        if (token == "" || token == null) {
            token = "MyCustomToken";
        }

        //Getting Wifi Address
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", 0);
            jsonObject.put("user_id", 0);
            jsonObject.put("token", token);
            jsonObject.put("mac_id", macAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!


                        Log.e(TAG, response.toString());

                        Gson gson = new Gson();

                        UserFirebaseToken userFirebaseToken = gson.fromJson(response.toString(), UserFirebaseToken.class);

                          /*  int token_id = response.getInt("id");
                            int user_id  = response.getInt("user_id");
                            String token = response.getString("token");
                            String mac_id = response.getString("mac_id");
                            int success   = response.getInt("success");

                            UserFirebaseToken userFirebaseToken = new UserFirebaseToken();
                            userFirebaseToken.setId(token_id);
                            userFirebaseToken.setToken(token);
                            userFirebaseToken.setMac_id(mac_id);
                            userFirebaseToken.setSuccess(success);
*/


                        Utils.setToken(userFirebaseToken, getApplicationContext());


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }
}

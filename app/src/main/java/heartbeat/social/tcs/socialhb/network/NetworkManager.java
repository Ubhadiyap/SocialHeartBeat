package heartbeat.social.tcs.socialhb.network;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import heartbeat.social.tcs.socialhb.activity.Dashboard;
import heartbeat.social.tcs.socialhb.bean.ModuleItem;
import heartbeat.social.tcs.socialhb.bean.UserLoginInfo;
import heartbeat.social.tcs.socialhb.bean.UserProfile;
import heartbeat.social.tcs.socialhb.bean.Webservice_API;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;
import heartbeat.social.tcs.socialhb.utility.Utils;


public class NetworkManager {
    private final Context context;
    private final String TAG = "Login";


    public NetworkManager(Context context) {
        this.context = context;
    }

    public void makeLoginWebServiceCall(String username, String password) throws JSONException {

        /**
         * used volley for network operation
         */
        String url = Webservice_API.user_login_api;
        final JSONObject sign_in_json_obj = new JSONObject();
        sign_in_json_obj.put("username", username);
        sign_in_json_obj.put("password", password);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Authenticating..");
        progressDialog.show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, sign_in_json_obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Login Response : " + response.toString());
                        //setting network availabilty
                        progressDialog.dismiss();
                        //   Utils.setNetworkAvailability(true);
                        String status;
                        try {
                            status = response.getString("success");


                            if (status.equals("1")) {
                                Gson gson = new Gson();

                                UserLoginInfo userInfo = gson.fromJson(response.toString(), UserLoginInfo.class);
                                DBHelper dbHelper = new DBHelper(context);
                                dbHelper.addUserData(userInfo);
                                String token_value = Utils.getToken(context);
                                if (!(token_value == null)) {
                                    Log.e("tokenvalue", "Token : " + token_value);
                                    //Updating UserId for firebase token
                                    updateIDForFirebaseToken("login");
                                } else {

                                    Log.e(TAG, "Token : " + "No Token");
                                    //Getting User Profile Data
                                    getUserProfileDataAndStoreIntoSQLiteDB();
                                }


                            } else {
                                String errorMsg = response.getString("errMsg");
                                Utils.showErrorAlert(context, errorMsg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        if (error instanceof NoConnectionError) {
                            progressDialog.dismiss();
                            //setting network availablity to false
                            Toast.makeText(context, "No InternetConnection", Toast.LENGTH_SHORT).show();
                            //   Utils.setNetworkAvailability(false);
                        }

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsObjRequest);


    }

    //Updating UserId for firebase notification token
    public void updateIDForFirebaseToken(String from_Activity) {
        String user_id;
        //Getting user id from SQLite
        if (from_Activity.equals("login")) {
            DBHelper dbHelper = new DBHelper(context);
            user_id = dbHelper.getUserID();

        } else {
            user_id = "0";
        }
        String token = Utils.getToken(context);

        //Getting Wifi Address
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String mac_id = wInfo.getMacAddress();


        String url = Webservice_API.firebase_update_user_id_for_registered_token_API;


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", 0);
            jsonObject.put("user_id", user_id);
            jsonObject.put("token", token);
            jsonObject.put("mac_id", mac_id);
            jsonObject.put("success", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        Log.e(TAG, "Update Response : " + response.toString());

                        //Getting User Profile Data
                        getUserProfileDataAndStoreIntoSQLiteDB();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(context).add(jsonRequest);


    }

    private void getUserProfileDataAndStoreIntoSQLiteDB() {
        DBHelper dbHelper = new DBHelper(context);
        String id = dbHelper.getUserID();

        String url = Webservice_API.user_profile_api + id;

        Log.e("NetworkManager", "Profile URI : " + url);

        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("NetworkManager", "Profile Info : " + response.toString());

                        Gson gson = new Gson();

                        UserProfile userProfile = gson.fromJson(response.toString(), UserProfile.class);

                        //Storing User Profile Data
                        ProfileDBHelper profileDBHelper = new ProfileDBHelper(context);
                        profileDBHelper.addUserProfileData(userProfile);

                        //Set session
                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.setLogin(true);

                        //Staring Dashboard
                        Intent intent = new Intent(context, Dashboard.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                        //finish();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(context).add(jsonRequest);

    }

    public void makeGetModuleWebserviceCall(final Context context, final VolleyCallback callback) {


        String url = Webservice_API.enabled_modules;
        Log.e("URL", url);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Module Response 1", response.toString());


                        List<ModuleItem> moduleItems = new ArrayList<ModuleItem>();
                        Gson gson = new Gson();
                        moduleItems = Arrays.asList(gson.fromJson(response.toString(), ModuleItem[].class));

                        System.out.println("module" + moduleItems);
                        callback.onSuccess(moduleItems);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });


        Volley.newRequestQueue(context).add(jsonArrayRequest);
        //System.out.println("modules"+modules);


    }

    public interface VolleyCallback {
        void onSuccess(List<ModuleItem> result);
    }
}

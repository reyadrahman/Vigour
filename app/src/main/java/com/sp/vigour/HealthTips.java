package com.sp.vigour;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HealthTips extends Fragment {

    ArrayList<String> userList;
    Handler mainHandler =  new Handler();
    Customadapter customadapter;
    ProgressDialog progressDialog;
    RecyclerView healthview;
    ImageButton healthclose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_healthtips,container,false);

        healthview = v.findViewById(R.id.healthview);

        userList = new ArrayList<>();

        new fetchData().start();

        Log.d("LOG", "fetching data");

        customadapter = new Customadapter(getContext(),userList);
        healthview.setAdapter(customadapter);
        healthview.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;

    }

    class fetchData extends Thread {

        String data = "";

        @Override
        public void run(){

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    /*progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();*/

                }
            });

            try {
                Log.d("LOG", "trying json");
                //https://www.npoint.io/docs/cbb709d068583b916068
                URL url = new URL("https://api.npoint.io/cbb709d068583b916068");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                if (!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("Healthtips");
                    Log.d("LOG", String.valueOf(users));
                    userList.clear();
                    for(int  i =0;i< users.length();i++){
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("healthtext");
                        userList.add(name);
                        Log.d("LOG", String.valueOf(name));

                    }
                }
            } catch (MalformedURLException e) {
                Log.d("LOG", "MalformedURLException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("LOG", "IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.d("LOG", "JSONException");
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                   /* if (progressDialog.isShowing())
                        progressDialog.dismiss();*/

                    customadapter = new Customadapter(getContext(),userList);
                    healthview.setAdapter(customadapter);
                    healthview.setLayoutManager(new LinearLayoutManager(getContext()));


                    customadapter.notifyDataSetChanged();
                }
            });
        }
    }

}

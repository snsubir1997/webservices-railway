package com.example.subir.webservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.subir.webservices.adaptor.DataAdaptor;
import com.example.subir.webservices.models.DataHandler;
import com.example.subir.webservices.network.CallAddr;
import com.example.subir.webservices.network.NetworkStatus;
import com.example.subir.webservices.network.OnWebServiceResult;
import com.example.subir.webservices.utils.CommonUtilities;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnWebServiceResult {

    EditText editText;
    TextView jsonPreview;
    String urlpt1 = "https://api.railwayapi.com/v2/pnr-status/pnr/";
    String urlpt2 = "/apikey/o3x0mmoui7/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.pnrText);
        jsonPreview = findViewById(R.id.jsonpreview);
    }

    public void getPnr(View v){
        hitrequest();
    }

    public void hitrequest(){
        FormEncodingBuilder parameters = new FormEncodingBuilder();
        parameters.add("page","1");

        if(NetworkStatus.getInstance(this).isConnectedToInternet()){
            String url = urlpt1 + editText.getText().toString() + urlpt2;
            CallAddr call = new CallAddr(this,url,parameters, CommonUtilities.SERVICE_TYPE.GET_DATA,this);
            call.execute();
        }else
        {
            Toast.makeText(this,"No Network ! You are offline.",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void getWebResponse(String result, CommonUtilities.SERVICE_TYPE type) {
        Log.i("Response IS::","type ="+type+"XML is ::"+result);

        try{
            JSONObject obj= new JSONObject(result);
            String jsonData = obj.toString();
            jsonPreview.setText(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

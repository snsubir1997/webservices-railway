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

            String response_code = obj.getString("response_code");
            if(response_code == "200") {

                String date_of_journey = obj.getString("doj");
                String pnr = obj.getString("pnr");

                JSONObject to_station = obj.getJSONObject("to_station");
                String boarding_station = to_station.getString("name");

                JSONObject from_station = obj.getJSONObject("to_station");
                String final_station = from_station.getString("name");

                JSONObject train = obj.getJSONObject("train");
                String train_name = train.getString("name");

                JSONObject journey_class = obj.getJSONObject("journey_class");
                String class_code = journey_class.getString("code");

                JSONArray jsonArray = obj.getJSONArray("passengers");
                String passenger_seat_data = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String no = jsonObject.getString("no");
                    String booking_status = jsonObject.getString("booking_status");
                    String current_status = jsonObject.getString("current_status");

                    passenger_seat_data += no + ") Booking Status : " + booking_status + "\n" +
                            "   Current Status : " + current_status + "\n\n\n";
                }

                String info = "PNR Number : " + pnr + "\n" +
                        "Train Name  : " + train_name + "\n" +
                        "From : " + boarding_station + "\n" +
                        "To : " + final_station + "\n" +
                        "Date Of Journey : " + date_of_journey + "\n" +
                        "Class : " + class_code + "\n\n" +
                        "Passenger Datails : " + "\n" +
                        passenger_seat_data;
                jsonPreview.setText(info);
            }else {
                Toast.makeText(getApplicationContext(),
                        "Cannot Fetch Data. Try Again",Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

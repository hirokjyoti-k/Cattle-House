package in.adbu.cattlehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCattleData extends AppCompatActivity implements OnMapReadyCallback {

    private TextView uid, type, age, gender, weight, heartrate, temperature, movement, breeding, lastvaccine;
    private List<CattleData> cattleData;
    private String breeding_status, string_age, string_weight;
    private int Position;

    private GoogleMap mMap;
    private CattleData cattledata;

    private Spinner breedingStatus;
    private DatePicker vaccineDate;

    private TextInputEditText ageUpdate, weightUpdate;

    StringBuilder strVaccineDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cattle_data);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cattleData = new ArrayList<>();

        Intent intent = getIntent();
        Position = Integer.valueOf(intent.getStringExtra("position"));

        uid = (TextView) findViewById(R.id.ID);
        type = (TextView) findViewById(R.id.Type);
        age = (TextView) findViewById(R.id.Age);
        gender = (TextView) findViewById(R.id.Gender);
        weight = (TextView) findViewById(R.id.Weight);
        heartrate = (TextView) findViewById(R.id.HeartRate);
        temperature = (TextView) findViewById(R.id.Temparture);
        movement = (TextView) findViewById(R.id.Movement);
        breeding = (TextView) findViewById(R.id.Breeding);
        lastvaccine = (TextView) findViewById(R.id.Vaccine);

        ageUpdate = (TextInputEditText) findViewById(R.id.cattleAgeUpdate);
        weightUpdate = (TextInputEditText) findViewById(R.id.cattleWeightUpdate);

        vaccineDate = (DatePicker) findViewById(R.id.lastvaccinedate);
        breedingStatus =(Spinner) findViewById(R.id.breedingStatus);
        breedingStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: breeding_status = "None"; break;
                    case 1: breeding_status = "Incalf"; break;
                    case 2: breeding_status = "Cycling"; break;
                    case 3: breeding_status = "Inheat"; break;
                    case 4: breeding_status = "Inseminated"; break;
                    default: breeding_status = "None";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.singleMap);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cattles");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cattleData.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CattleData snapshotData = snapshot.getValue(CattleData.class);
                    cattleData.add(snapshotData);
                }
                loadData();
                //Load map after data recvd from firebase
                mapFragment.getMapAsync(ViewCattleData.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewCattleData.this, "" + databaseError, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadData() {

        cattledata = cattleData.get(Position);
        uid.setText("UID : " + cattledata.Uid);
        type.setText("Category : " + cattledata.Type);
        age.setText("Age : " + cattledata.Age);
        gender.setText("Gender : " + cattledata.Gender);
        weight.setText("Weight : " + cattledata.Weight);
        heartrate.setText("HeartRate : " + cattledata.HeartRate);
        temperature.setText("Temperature : " + cattledata.Temperature);
        movement.setText("Status : " + cattledata.Movement);
        breeding.setText("Breeding Status : \n" + cattledata.Beeding);
        lastvaccine.setText("Last Vaccine : \n" + cattledata.LastVaccine);

        if(cattledata.Gender.equals("Male"))
            breedingStatus.setEnabled(false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        LatLng cattle = new LatLng(cattledata.Latitude, cattledata.Longitude);
        mMap.addMarker(new MarkerOptions().position(cattle).title("" + cattledata.Uid));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(cattle));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cattle, 17.0f));
    }


    public void updateData(View view) {

        strVaccineDate = new StringBuilder();;
        strVaccineDate.append(vaccineDate.getDayOfMonth()+"-");
        strVaccineDate.append((vaccineDate.getMonth() + 1)+"-");//month is 0 based
        strVaccineDate.append(vaccineDate.getYear());

        string_age = ageUpdate.getText().toString().trim();
        string_weight = weightUpdate.getText().toString().trim();

        FirebaseDatabase cattledb = FirebaseDatabase.getInstance();
        DatabaseReference lastVaccine = cattledb.getReference("cattles/"+cattledata.Uid+"/LastVaccine");
        DatabaseReference Breeding = cattledb.getReference("cattles/"+cattledata.Uid+"/Beeding");
        DatabaseReference age = cattledb.getReference("cattles/"+cattledata.Uid+"/Age");
        DatabaseReference weight = cattledb.getReference("cattles/"+cattledata.Uid+"/Weight");


        lastVaccine.setValue(strVaccineDate.toString());
        Breeding.setValue(""+breeding_status);


        if(!string_age.isEmpty()){
            age.setValue(Integer.valueOf(string_age));
        }

        if(!string_weight.isEmpty()){
            weight.setValue(Integer.valueOf(string_weight));
        }

        Toast.makeText(ViewCattleData.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
    }


    public void saveData(View view) {

        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("Saving data");
        loading.setCancelable(false);
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzA0Xla2Wm0aTuy1zht9RLC1FgxpEJRV_qrh98mNxySK_15YTA_e3ng7MtEclG58HjxcQ/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ViewCattleData.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewCattleData.this, ""+error, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("UID", cattledata.Uid);
                parmas.put("Type", cattledata.Type);
                parmas.put("Gender", cattledata.Gender);
                parmas.put("Age", String.valueOf(cattledata.Age));
                parmas.put("Weight", String.valueOf(cattledata.Weight));
                parmas.put("Temperature", String.valueOf(cattledata.Temperature));
                parmas.put("Latitude", String.valueOf(cattledata.Latitude));
                parmas.put("Longitude", String.valueOf(cattledata.Longitude));
                return parmas;
            }
        };

        int socketTimeOut = 10000;// u can change this .. here it is 10 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    public void deleteData(View view) {
        //fix app crash when data removed
        if(Position>1)
            Position = Position-1;

        DatabaseReference cattleNode = FirebaseDatabase.getInstance().getReference("cattles/"+cattledata.Uid);
        cattleNode.removeValue();
        startActivity(new Intent(ViewCattleData.this, MainActivity.class));
        finish();
    }
}
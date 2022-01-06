package in.adbu.cattlehouse.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.adbu.cattlehouse.R;


public class EditData extends Fragment {

    private Spinner category;
    private String string_category, string_gender, string_uid, string_age, string_weight;
    private RadioButton gender;
    private RadioGroup genderGrp;
    private TextInputEditText uid, age, weight;
    private MaterialButton addcattleBtn;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uid = (TextInputEditText) view.findViewById(R.id.cattleUID);
        age = (TextInputEditText) view.findViewById(R.id.cattleAge);
        weight = (TextInputEditText) view.findViewById(R.id.cattleWeight);

        addcattleBtn = (MaterialButton) view.findViewById(R.id.addBtn);

        category =(Spinner) view.findViewById(R.id.cattleCategory);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: string_category = "Cattle"; break;
                    case 2: string_category = "Goat"; break;
                    case 3: string_category = "Pig"; break;
                    default: string_category = "None";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addcattleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData(view);
            }
        });
    }

    private void uploadData(View view) {

        if(string_category.equals("None")){
            Toast.makeText(getContext(), "Please select Category", Toast.LENGTH_SHORT).show();
            return;
        }

        string_uid = uid.getText().toString().trim();
        string_age = age.getText().toString().trim();
        string_weight = weight.getText().toString().trim();

        //retive age from radio button
        genderGrp = (RadioGroup) view.findViewById(R.id.cattleGender);
        int selectedGender = genderGrp.getCheckedRadioButtonId();
        gender = (RadioButton) view.findViewById(selectedGender);
        string_gender = gender.getText().toString().trim();

        if(string_uid.isEmpty()){
            uid.setError("UID can't be empty");
            return;
        }

        if(string_age.isEmpty()){
            age.setError("Age can't be empty");
            return;
        }

        if(string_weight.isEmpty()){
            weight.setError("Weight can't be empty");
            return;
        }

        loadProgressDailog();

        FirebaseDatabase cattledb = FirebaseDatabase.getInstance();
        DatabaseReference age = cattledb.getReference("cattles/"+string_uid+"/Age");
        DatabaseReference gender = cattledb.getReference("cattles/"+string_uid+"/Gender");
        DatabaseReference type = cattledb.getReference("cattles/"+string_uid+"/Type");
        DatabaseReference weight = cattledb.getReference("cattles/"+string_uid+"/Weight");
        DatabaseReference uid = cattledb.getReference("cattles/"+string_uid+"/Uid");

        age.setValue(Integer.valueOf(string_age));
        weight.setValue(Integer.valueOf(string_weight));
        gender.setValue(string_gender);
        type.setValue(string_category);
        uid.setValue(string_uid);
    }

    void loadProgressDailog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Data Successfully Added", Toast.LENGTH_SHORT).show();
            }
        },3000);
    }
}
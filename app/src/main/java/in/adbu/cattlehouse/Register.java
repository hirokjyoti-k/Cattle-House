package in.adbu.cattlehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private TextInputEditText fullname, email, password;
    private String string_fullname, string_email, string_password;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = (TextInputEditText) findViewById(R.id.fullname);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        }
    }

    public void register(View view) {
        string_fullname = fullname.getText().toString().trim();
        string_email = email.getText().toString().trim();
        string_password = password.getText().toString().trim();

        if(string_fullname.isEmpty()){
            fullname.setError("Name can't be empty");
            return;
        }

        if(string_email.isEmpty()){
            email.setError("Email can't be empty");
            return;
        }

        if(string_password.isEmpty()){
            password.setError("Password can't be empty");
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(string_email, string_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    createAccount();
                }else {
                    Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void createAccount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference name = database.getReference("users/"+auth.getCurrentUser().getUid()+"/Name");
        DatabaseReference email = database.getReference("users/"+auth.getCurrentUser().getUid()+"/Email");

        name.setValue(string_fullname);
        email.setValue(string_email);

        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }

    public void login(View view) {
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }
}
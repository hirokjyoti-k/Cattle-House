package in.adbu.cattlehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputEditText email, password;
    private String string_email, string_password;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    public void login(View view) {

        string_email = email.getText().toString().trim();
        string_password = password.getText().toString().trim();

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

        auth.signInWithEmailAndPassword(string_email, string_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(Login.this, Register.class));
        finish();
    }

    public void forgotpassword(View view) {

        string_email = email.getText().toString().trim();

        if(string_email.isEmpty()){
            email.setError("Email can't be empty");
            return;
        }

        ProgressDialog reset = new ProgressDialog(this);
        reset.setCancelable(false);
        reset.setMessage("Sending reset link");
        reset.show();

        auth.sendPasswordResetEmail(string_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reset.dismiss();
                Toast.makeText(Login.this, "Reset link sent to : "+string_email, Toast.LENGTH_LONG).show();
            }
        });
    }
}
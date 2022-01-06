package in.adbu.cattlehouse.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.adbu.cattlehouse.Login;
import in.adbu.cattlehouse.R;

public class Profile extends Fragment {

    private MaterialButton logout;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference name,email;
    private TextInputEditText Name, Email;
    private String emailID;
    private ProgressDialog reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Name = (TextInputEditText) view.findViewById(R.id.fullname);
        Email = (TextInputEditText) view.findViewById(R.id.email);

        auth = FirebaseAuth.getInstance();
        logout = (MaterialButton) view.findViewById(R.id.logoutBtn);
        logout();

        database = FirebaseDatabase.getInstance();
        name = database.getReference("users/"+auth.getCurrentUser().getUid()+"/Name");
        email = database.getReference("users/"+auth.getCurrentUser().getUid()+"/Email");
        loadUserProfile();

        TextView updatePassword = (TextView) view.findViewById(R.id.resetpassword);
        updatePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reset = new ProgressDialog(getContext());
                reset.setMessage("Sending reset link");
                reset.setCancelable(false);
                reset.show();

                auth.sendPasswordResetEmail(emailID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reset.dismiss();
                        Toast.makeText(getContext(), "Reset link sent to "+emailID, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void loadUserProfile() {

        name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                Name.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        email.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                emailID = snapshot.getValue(String.class);
                Email.setText(emailID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity() ,Login.class );
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
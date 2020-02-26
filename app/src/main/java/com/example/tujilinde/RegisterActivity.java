package com.example.tujilinde;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(RegisterActivity.this, AgentProfileActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordText);
        mRegister = findViewById(R.id.registerButton);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailText = mEmail.getText().toString();
                final String passwordText = mPassword.getText().toString();

                if (!emailText.isEmpty() && !passwordText.isEmpty()) {


                    mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
//                                String user_id = mAuth.getCurrentUser().getUid();
//                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Security Agents").child(user_id);
//                                current_user_db.setValue(true);

                            } else {
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Security Agents").child(user_id);
                                current_user_db.setValue(true);
                                Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(RegisterActivity.this, "Email or password empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}

package com.example.anasamin.chatme.Authentication;

import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.Activities.MainActivity;
import com.example.anasamin.chatme.GeneralUtilities.resetPasswordDialogFragment;
import com.example.anasamin.chatme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {
EditText loginId,password;
Button login,createNew,forgot;
ProgressBar progressBar;
FirebaseAuth mAuth;
FirebaseAuth.AuthStateListener mListener;
LinearLayout layout;
resetPasswordDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authentication);
        layout=findViewById(R.id.LayoutSignin);
        loginId=(EditText)findViewById(R.id.logid);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.logbutton);
        createNew=(Button)findViewById(R.id.create);
        forgot=(Button)findViewById(R.id.forgot);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth=FirebaseAuth.getInstance();



        mListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()!=null){
                    String path=mAuth.getCurrentUser().getUid();
                    Intent intent=new Intent(AuthenticationActivity.this, MainActivity.class);
                    intent.putExtra("uid",path);
                    startActivity(intent);
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                onlogin();
            }
        });
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AuthenticationActivity.this,CreateuserActivity.class);
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new resetPasswordDialogFragment();
                dialog.show(getSupportFragmentManager().beginTransaction(),"Dialog");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);

        if(mAuth.getCurrentUser()!=null){
            String path=mAuth.getCurrentUser().getUid();
            Intent intent=new Intent(AuthenticationActivity.this,MainActivity.class);
          //  intent.putExtra("myProfile",id);
            intent.putExtra("uid",path);
            startActivity(intent);
        }
    }

    private void onlogin(){
        String email=loginId.getText().toString();
        String pass=password.getText().toString();
        if(!loginId.getText().toString().equals("")||!password.getText().toString().equals("")){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(AuthenticationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(AuthenticationActivity.this,"User didn't entered email or password",Toast.LENGTH_LONG).show();
        }

    }
}

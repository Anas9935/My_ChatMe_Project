package com.example.anasamin.chatme.Authentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anasamin.chatme.Objects.userWithGender;
import com.example.anasamin.chatme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateuserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
EditText name,pass,confPass,email,phone;
Spinner genderSpinner;
int gender;

FirebaseAuth mAuth;
FirebaseAuth.AuthStateListener mlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        name=(EditText)findViewById(R.id.newName);
        pass=(EditText)findViewById(R.id.newPass);
        confPass=(EditText)findViewById(R.id.newConfPass);
        email=(EditText)findViewById(R.id.newEmail);
        phone=(EditText)findViewById(R.id.newphone);
        genderSpinner=(Spinner)findViewById(R.id.newSpinnerGender);

        genderSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.gender_Option,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:{ gender=1;      //Male
            break;}
            case 1: {gender=2;      //Female
                break;}
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        gender=1;
        genderSpinner.setSelection(0);
    }
    private void save(){
        int flag=0;
        final String e=email.getText().toString();
        String password=pass.getText().toString();
        if(pass.getText().toString().equals(confPass.getText().toString())){
            flag=1;
        }
        if(email.length()>0&&pass.length()>0&&flag==1){
        mAuth.createUserWithEmailAndPassword(e,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //adding the user details
                if(task.isSuccessful()){
                    userId newuser=new userId(name.getText().toString(),null,null,null
                    ,email.getText().toString(),Long.valueOf(phone.getText().toString()),gender,null);
                    FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(gender))
                            .child(mAuth.getCurrentUser().getUid())
                            .setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CreateuserActivity.this,"New id generated successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(CreateuserActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    userWithGender u=new userWithGender(mAuth.getUid(),gender);

                    FirebaseDatabase.getInstance().getReference("userGender").child(mAuth.getCurrentUser().getUid()).push().setValue(u);
                    FirebaseDatabase.getInstance().getReference("userCount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Double users=dataSnapshot.getValue(double.class);
                            if(users==null){
                                users=0d;
                            }
                            FirebaseDatabase.getInstance().getReference("userCount").setValue(users+1);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    Toast.makeText(CreateuserActivity.this,"task isnt successful",Toast.LENGTH_LONG).show();
                }
            }
        });
        finish();
    }else{
            Toast.makeText(CreateuserActivity.this,"Re check the entries",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save :{
                save();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}

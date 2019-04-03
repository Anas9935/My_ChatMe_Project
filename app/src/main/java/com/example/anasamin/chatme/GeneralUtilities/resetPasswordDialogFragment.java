package com.example.anasamin.chatme.GeneralUtilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anasamin.chatme.Authentication.AuthenticationActivity;
import com.example.anasamin.chatme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPasswordDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("ResetPassword");
        View view= LayoutInflater.from(getContext()).inflate(R.layout.reset_password_dialog,null);
        builder.setView(view);
        final EditText email=view.findViewById(R.id.dialog_email);

        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String mail=email.getText().toString();

               FirebaseAuth.getInstance().sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                          Log.e("fragmentSuccess","email is send successfully");
                            //  Toast.makeText(getContext(),"Details are sent to your email",Toast.LENGTH_LONG).show();
                        }else{
                            Log.e("Fragment","Email not sent");
                        }
                   }
                });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        return builder.create();
    }
}

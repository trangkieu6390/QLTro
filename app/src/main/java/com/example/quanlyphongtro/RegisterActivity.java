package com.example.quanlyphongtro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText edtFullname,edtEmail,edtPassword,edtUsername,edtCnfPassword;
    public RadioButton mGenderOption;

    private RadioGroup mGender;
    public String strGender;
    TextView btnLogin;
    //  private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        btnRegister=findViewById(R.id.btnRegister);
        edtFullname=findViewById(R.id.edtFullName);
        edtUsername=findViewById(R.id.edtUserName);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassWord);
        edtCnfPassword=findViewById(R.id.edtCnfPassword);

        mGender=findViewById(R.id.rdobtnGroup);
//        progressBar=view.findViewById(R.id.progessBar);



        firebaseAuth=FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = edtFullname.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String cnfPassword = edtCnfPassword.getText().toString().trim();


                mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        mGenderOption=mGender.findViewById(checkedId);
                        switch (checkedId){
                            case R.id.rdoMale:
                                strGender=mGenderOption.getText().toString();
                                break;
                            case R.id.rdoFemale:
                                strGender=mGenderOption.getText().toString();
                                break;
                            default:
                        }
                    }
                });

                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(RegisterActivity.this,"please enter fullname",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this,"please enter username",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this,"please enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this,"please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cnfPassword)) {
                    Toast.makeText(RegisterActivity.this,"please enter confirm password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(RegisterActivity.this,"password too short",Toast.LENGTH_SHORT).show();
                }

//                progressBar.setVisibility(View.VISIBLE);

                if(password.equals(cnfPassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // progressBar.setVisibility(View.GONE);

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,"Authentication fail",Toast.LENGTH_SHORT).show();
                                    } else {

                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                                        Toast.makeText(RegisterActivity.this,"Register success",Toast.LENGTH_SHORT).show();

                                    }


                                }


                            });
                }
            }
        });
    }
}

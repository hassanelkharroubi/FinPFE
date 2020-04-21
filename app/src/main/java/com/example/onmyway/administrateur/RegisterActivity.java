package com.example.onmyway.administrateur;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.example.onmyway.CustomToast;
import com.example.onmyway.DB.CustomFirebase;
import com.example.onmyway.DB.UserDB;
import com.example.onmyway.R;
import com.example.onmyway.UserInfo.User;
import com.example.onmyway.connection.Internet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;


    private static final String TAG="register";
    private String cin;
    private EditText editTextCin;

    private EditText editTextFullName;
    private String fullName;

    private String email;
    private EditText editTextEmail;

    private String password;
    private EditText editTextPassword;

    private String confirmPassword;
    private EditText editTextConfirmPassword;

    private User user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //for sqlite data base
    private UserDB userDB;
    //check internet
    Internet internet;
    //boolean for check if there network or not
    //intialize it in onResume() method
    private Boolean connected;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(getResources().getString(R.string.UserData));
        userDB=new UserDB(this);
        //start new Thread to check network state and internet acess

        connected=new Internet(this).connected();






        //get toolbar_layout
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.driver));

        // Initialize Firebase Auth

        mAuth= CustomFirebase.getUserAuth();

        editTextEmail=findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        editTextConfirmPassword=findViewById(R.id.confirmpassword);
        editTextFullName=findViewById(R.id.fullname);
        editTextCin=findViewById(R.id.cin);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        menu.removeItem(R.id.ajouter);

        return super.onCreateOptionsMenu(menu);
}


    public void register(View view) {
        connected=new Internet(this).connected();


        if(allInputValid())
        {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                myRef.child( mAuth.getUid()).setValue(user);

                                //i the same time we have to add this user to local data base
                                userDB.addUser(user);

                                CustomToast.toast("Authentication success.",RegisterActivity.this);

                                mAuth.signOut();
                                mAuth.signInWithEmailAndPassword(Administrateur.email,Administrateur.password);

                                startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));
                            }
                            else
                            {

                                CustomToast.toast("on ne peut pas ajouter neveau utilidateur !Verfier votre connection.",RegisterActivity.this);


                            }
                        }
                    });

        }
        else
        {

            CustomToast.toast("Veuilez verifier les donnees que vouz avez saisi ....!",this);
        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
            startActivity(new Intent(this, Home.class));

        return super.onOptionsItemSelected(item);
    }





    //fonction de verification email
    public static boolean isEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    //this method will valid input in the RegisterActivity.java
    private boolean allInputValid()
    {

        cin=editTextCin.getText().toString().trim();
        fullName=editTextFullName.getText().toString().trim();
        email=editTextEmail.getText().toString().trim();
        password=editTextPassword.getText().toString();
        confirmPassword=editTextConfirmPassword.getText().toString();
       if(!cin.isEmpty() && !fullName.isEmpty() && isEmail(email)
               && !password.isEmpty() && password.equals(confirmPassword))
       {
           user=new User(fullName,email,password,cin);
           return true;
       }
       return  false;


    }

    @Override
    protected void onResume() {
        super.onResume();
        connected=new Internet(this).connected();

    }


}

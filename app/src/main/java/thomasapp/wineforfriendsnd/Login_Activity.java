package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends Activity implements View.OnClickListener{

    TextView TV_registeraccess;
    EditText ET_mail, ET_password;
    Button B_login;
    String mail, password;
    AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseAuth = FirebaseAuth.getInstance();

        ET_mail = findViewById(R.id.editTextmaillogin);
        ET_password = findViewById(R.id.editTextpasswordlogin);
        B_login = findViewById(R.id.buttonlogin);
        progressBar = (ProgressBar) findViewById(R.id.progressbarlogin);
        B_login.setOnClickListener(this);

        builder = new AlertDialog.Builder(Login_Activity.this);

        TV_registeraccess = (TextView) findViewById(R.id.TextViewregisteraccess);
        TV_registeraccess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this, Register.class);
                startActivity(i);
            }
        });
    }

    private void login(){

        mail = ET_mail.getText().toString().trim();
        password = ET_password.getText().toString().trim();

        if (mail.equals("") || password.equals("")) {
            builder.setTitle("Oups");
            builder.setMessage("Il manque des infos..");
            builder.setCancelable(false);
            displayAlert();
        } else if (firebaseAuth.getCurrentUser()!=null){
            builder.setTitle("Oups");
            builder.setMessage("Vous êtes déjà connécté");
            builder.setCancelable(false);
            displayAlert();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                        Intent ii = new Intent(Login_Activity.this, MainActivity.class);
                        startActivity(ii);
                        finish();
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        builder.setTitle("Oups");
                        builder.setMessage("Cet utilisateur n'existe pas, veuillez reéssayer!");
                        displayAlert();
                        //Toast.makeText(getApplicationContext(),"n", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void displayAlert() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ET_mail.setText("");
                ET_password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == B_login)
        login();
    }

    @Override
    public void onBackPressed() {
    }
}

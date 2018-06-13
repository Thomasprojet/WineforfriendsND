package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by thoma on 05/01/2018.
 */

public class Register extends Activity implements View.OnClickListener{

    EditText ET_mail, ET_mailbis, ET_password, ET_name;
    TextView TV_checkboxcaviste;
    ImageView IV_flecheblanche;
    Button B_register;
    String mail, mailbis, name, password;
    AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myref;
    ProgressBar progressBar;
    CheckBox CB_caviste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();

        ET_mail = findViewById(R.id.editTextmailregister);
        ET_mailbis = findViewById(R.id.editTextmailbisregister);
        ET_password = findViewById(R.id.editTextpasswordregister);
        ET_name = findViewById(R.id.editTextnameregister);
        builder = new AlertDialog.Builder(Register.this);
        progressBar = (ProgressBar) findViewById(R.id.progressbarregister);
        IV_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);
        CB_caviste = (CheckBox) findViewById(R.id.checkboxvisibilitecaviste);
        TV_checkboxcaviste = (TextView) findViewById(R.id.tvforthcheckboxcaviste);

        B_register = findViewById(R.id.buttonregister);
        B_register.setOnClickListener(this);
        IV_flecheblanche.setOnClickListener(this);

    }

    private void registeruser(){

        if (CB_caviste.isChecked()) {
            TV_checkboxcaviste.setText("ok");
        } else {
            TV_checkboxcaviste.setText("nok");
        }
        mail = ET_mail.getText().toString();
        String input = ET_mail.getText().toString();
        mail = input.toLowerCase(); //converts the string to lowercase
        mailbis = ET_mailbis.getText().toString().trim();
        String input2 = ET_mail.getText().toString();
        mailbis = input2.toLowerCase(); //converts the string to lowercase
        name = ET_name.getText().toString().trim();
        password = ET_password.getText().toString().trim();
        final String ischecked = TV_checkboxcaviste.getText().toString().trim();



        if (name.equals("") || mail.equals("") || password.equals("") || mailbis.equals("")) {
            builder.setTitle("Oups");
            builder.setMessage("Un des champs doit étre vide!");
            displayAlertvide();
        } else if (!mail.equals(mailbis)) {
            builder.setTitle("Oups");
            builder.setMessage("Les deux adresses mail ne sont pas identiques!");
            displayAlert();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        String mailwithoutdot = mail.replace(".",",");
                        //decode replace(",", ".");
                        myref = database.getReference("users").child(mailwithoutdot);
                        myref.child("mail").setValue(mail);
                        myref.child("name").setValue(name);
                        myref.child("caviste").setValue(ischecked);
                        //myref.child("mail").setValue(mail);
                        Toast.makeText(Register.this,"Votre compte a bien été créé", Toast.LENGTH_SHORT).show();
                        Intent ii = new Intent(Register.this, MainActivity.class);
                        startActivity(ii);
                        finish();
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        builder.setTitle("Oups");
                        builder.setMessage("Une erreur est survenue, veuillez reéssayer");
                        displayAlert();
                        //Toast.makeText(Register.this,"not success", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void displayAlert() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    ET_name.setText("");
                    ET_mail.setText("");
                    ET_mailbis.setText("");
                    ET_password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void displayAlertvide() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == B_register){
            registeruser();
        }
        if (view == IV_flecheblanche){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Add_Message extends Activity {

    ImageView IV_flecheblanche;
    EditText ET_message;
    Button BT_ajoutermessage;
    String usermail, message;
    ProgressBar progressBar;
    FirebaseDatabase FBDmessage;
    DatabaseReference DBRmessage;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase FBDmessage2;
    DatabaseReference DBRmessage2;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__message);

        IV_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);
        ET_message = (EditText) findViewById(R.id.ajoutermessage);
        BT_ajoutermessage = (Button) findViewById(R.id.buttonaddmessage);
        progressBar = (ProgressBar) findViewById(R.id.progressbaraddmessage);

        builder = new AlertDialog.Builder(Add_Message.this);

        firebaseAuth = FirebaseAuth.getInstance();
        FBDmessage = FirebaseDatabase.getInstance();
        FBDmessage2 = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        final String premiercommentaireresult = getIntent().getExtras().getString("Premiercommentaire");
        final String premierchateauxresult = getIntent().getExtras().getString("Premierchateaux");
        final String premierpseudoresult = getIntent().getExtras().getString("Premierpseudo");
        //final String premeiercommentairesansdot = premiercommentaireresult.replace(".",",");

        final String rassemblement = premierchateauxresult+premiercommentaireresult+premierpseudoresult;


        BT_ajoutermessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }

                message = ET_message.getText().toString().trim();

                if (message.equals("")) {
                    builder.setTitle("Oups");
                    builder.setMessage("Il manque des infos..");
                    builder.setCancelable(false);
                    displayAlert();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    assert user != null;
                    usermail = user.getEmail();
                    message = ET_message.getText().toString().trim();
                    final String mailwithoutdot = usermail.replace(".",",");

                    DBRmessage = FBDmessage.getReference("users");
                    DBRmessage.child(mailwithoutdot).orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final String pseudo = dataSnapshot.child("name").getValue().toString();

                                DBRmessage2 = FBDmessage2.getReference("message").child(rassemblement).push();
                                DBRmessage2.child("commentairesuivant").setValue(message);
                                DBRmessage2.child("pseudo").setValue(pseudo);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    //progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(Add_Message.this, "Votre commentaire vient d'être ajouté.", Toast.LENGTH_SHORT).show();
                    thread.start();
                }
            }
        });

        IV_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Add_Message.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void displayAlert() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(Add_Message.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Add_Message.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}

package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Add_Wine extends Activity {

    EditText ET_chateaux, ET_commentaire;
    TextView TV_recap;
    ImageButton IB_rouge, IB_blanc, IB_rose, IB_champagne, IB_spiritueux;
    String chateaux, commentaire, type, usermail;
    Button BT_addwine;
    ImageView IM_flecheblanche;
    ProgressBar progressBar;
    FirebaseDatabase FBDpseudo;
    DatabaseReference DBRpseudo;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myref;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__wine);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ET_chateaux = (EditText)findViewById(R.id.chateauxtoadd);
        ET_commentaire = (EditText) findViewById(R.id.commentairetoadd);
        TV_recap = (TextView) findViewById(R.id.typevin);
        IB_rouge = (ImageButton) findViewById(R.id.imageButtonrouge);
        IB_blanc = (ImageButton) findViewById(R.id.imageButtonblanc);
        IB_rose = (ImageButton) findViewById(R.id.imageButtonrose);
        IB_champagne = (ImageButton) findViewById(R.id.imageButtonchamp);
        IB_spiritueux = (ImageButton) findViewById(R.id.imageButtonspirit);
        BT_addwine= (Button) findViewById(R.id.buttonaddwinetobdd);
        IM_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);
        progressBar = (ProgressBar) findViewById(R.id.progressbaraddwine);

        builder = new AlertDialog.Builder(Add_Wine.this);


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        FBDpseudo = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        IM_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add_Wine.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        BT_addwine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert user != null;
                usermail = user.getEmail();
                chateaux = ET_chateaux.getText().toString().trim();
                commentaire = ET_commentaire.getText().toString().trim();
                type = TV_recap.getText().toString().trim();
                String mailwithoutdot = usermail.replace(".",",");

                if (chateaux.equals("") || commentaire.equals("")|| type.equals("")) {
                    builder.setTitle("Oups");
                    builder.setMessage("Il manque des infos..");
                    builder.setCancelable(false);
                    displayAlert();
                } else{

                    DBRpseudo = FBDpseudo.getReference("users");
                    DBRpseudo.child(mailwithoutdot).orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final String pseudo = dataSnapshot.child("name").getValue().toString();
                                usermail = user.getEmail();
                                assert usermail != null;
                                //final Date currentTime = Calendar.getInstance().getTime();
                                final String mailwithoutdot = usermail.replace(".",",");

                                Date todayDate = Calendar.getInstance().getTime();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                final String todayString = formatter.format(todayDate);
                                //Toast.makeText(Add_Wine.this,pseudo, Toast.LENGTH_LONG).show();

                                myref.child("contacts").child(mailwithoutdot).orderByChild("visibilite").equalTo("ok").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                String mailcontact = child.child("contact_mail").getValue().toString();
                                                String mailcontactwd = mailcontact.replace(".",",");
                                                myref = database.getReference("wines").child(mailcontactwd).push();
                                                //Toast.makeText(Add_Wine.this,mailcontactwd, Toast.LENGTH_LONG).show();
                                                myref.child("chateaux").setValue(chateaux);
                                                myref.child("commentaire").setValue(commentaire);
                                                myref.child("type").setValue(type);
                                                myref.child("usermail").setValue(usermail);
                                                myref.child("pseudo").setValue(pseudo);
                                                myref.child("coupdecoeur").setValue("nok");
                                                //myref.child("date").setValue(todayString);
                                            }

                                            myref = database.getReference("wines").child(mailwithoutdot).push();
                                            myref.child("chateaux").setValue(chateaux);
                                            myref.child("commentaire").setValue(commentaire);
                                            myref.child("type").setValue(type);
                                            myref.child("usermail").setValue(usermail);
                                            myref.child("pseudo").setValue(pseudo);
                                            myref.child("coupdecoeur").setValue("nok");
                                            myref.child("date").setValue(todayString);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //Toast.makeText(Add_Wine.this,"Cette référence est entrain d'être enregistrée dans la base de données ;)", Toast.LENGTH_SHORT).show();
                    thread.start();
                }
            }
        });

        IB_rouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("rouge");
                IB_rouge.setBackgroundResource(R.drawable.roundimage);
                IB_blanc.setBackgroundColor(0);
                IB_rose.setBackgroundColor(0);
                IB_champagne.setBackgroundColor(0);
                IB_spiritueux.setBackgroundColor(0);
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });
        IB_blanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("blanc");
                IB_rouge.setBackgroundColor(0);
                IB_blanc.setBackgroundResource(R.drawable.roundimage);
                IB_rose.setBackgroundColor(0);
                IB_champagne.setBackgroundColor(0);
                IB_spiritueux.setBackgroundColor(0);
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });
        IB_rose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("rose");
                IB_rouge.setBackgroundColor(0);
                IB_blanc.setBackgroundColor(0);
                IB_rose.setBackgroundResource(R.drawable.roundimage);
                //IB_rose.setBackgroundColor(0x80E57373);
                IB_champagne.setBackgroundColor(0);
                IB_spiritueux.setBackgroundColor(0);
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });
        IB_champagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("champagne");
                IB_rouge.setBackgroundColor(0);
                IB_blanc.setBackgroundColor(0);
                IB_rose.setBackgroundColor(0);
                IB_champagne.setBackgroundResource(R.drawable.roundimage);
                IB_spiritueux.setBackgroundColor(0);
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });
        IB_spiritueux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("spiritueux");
                IB_rouge.setBackgroundColor(0);
                IB_blanc.setBackgroundColor(0);
                IB_rose.setBackgroundColor(0);
                IB_champagne.setBackgroundColor(0);
                IB_spiritueux.setBackgroundResource(R.drawable.roundimage);
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
            }
        });

    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(Add_Wine.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Add_Wine.this, MainActivity.class);
        startActivity(i);
        finish();
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
}

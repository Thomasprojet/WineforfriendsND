package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class Add_contacts extends Activity {

    EditText ET_mail;
    Button BT_addcontact;
    ImageView IV_flecheblanche;
    String usermail,mail,pseudo;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myref;
    AlertDialog.Builder builder;
    FirebaseDatabase FBDcheck;
    DatabaseReference DBRcheck;
    FirebaseDatabase FBDcheck2;
    DatabaseReference DBRcheck2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ET_mail = (EditText) findViewById(R.id.edittextmailcontact);
        progressBar = (ProgressBar) findViewById(R.id.progressbarcontacts);
        BT_addcontact = (Button) findViewById(R.id.buttonaddcontact);

        IV_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);
        IV_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add_contacts.this, Contacts.class);
                startActivity(i);
                finish();
            }
        });

        builder = new AlertDialog.Builder(Add_contacts.this);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FBDcheck = FirebaseDatabase.getInstance();
        FBDcheck2 = FirebaseDatabase.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        BT_addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
                assert user != null;
                usermail = user.getEmail();
                mail = ET_mail.getText().toString().trim();
                final String mailwithoutdot = usermail.replace(".", ",");
                final String mailcheck = mail.replace(".", ",");

                if (mail.equals(usermail)){
                    builder.setTitle("Oups");
                    builder.setMessage("Impossible de s'ajouter... Hein!!");
                    builder.setCancelable(false);
                    displayAlertcontactintrouvable();
                }else {
                    DBRcheck = FBDcheck.getReference("users");
                    DBRcheck.child(mailcheck).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final String pseudo = dataSnapshot.child("name").getValue().toString();
                                //Toast.makeText(Contacts.this,pseudo, Toast.LENGTH_LONG).show();
                                DBRcheck = FBDcheck.getReference("contacts");
                                DBRcheck.child(mailwithoutdot).orderByChild("contact_mail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            builder.setTitle("Oups");
                                            builder.setMessage("Vous etes déjà en contact...:)");
                                            builder.setCancelable(false);
                                            displayAlertcontactintrouvable();
                                        }else {
                                            progressBar.setVisibility(View.VISIBLE);
                                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            DBRcheck = FBDcheck.getReference("users");
                                            DBRcheck.child(mailwithoutdot).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        final String pseudootheruser = dataSnapshot.child("name").getValue().toString();
                                                        myref = database.getReference("verification4").child(mailcheck).push();
                                                        myref.child("verification4").setValue("ok");
                                                        myref.child("pseudo").setValue(pseudootheruser);
                                                        myref.child("mail").setValue(usermail);
                                                        Toast.makeText(Add_contacts.this,"Demande d'ami envoyée, dés lors que votre contact aura accépté votre demande vous pourez partager des vins;) ", Toast.LENGTH_LONG).show();

                                                    }else {
                                                        Toast.makeText(Add_contacts.this,"Oupssss", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            /*progressBar.setVisibility(View.VISIBLE);
                                            myref = database.getReference("contacts").child(mailwithoutdot).push();
                                            myref.child("contact_mail").setValue(mail);
                                            myref.child("name").setValue(pseudo);
                                            myref.child("visibilite").setValue("ok");
                                            myref.child("visibilitecb").setValue("ok");
                                            DBRcheck = FBDcheck.getReference("users");
                                            DBRcheck.child(mailwithoutdot).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        final String pseudootheruser = dataSnapshot.child("name").getValue().toString();
                                                        myref = database.getReference("contacts").child(mailcheck).push();
                                                        myref.child("contact_mail").setValue(usermail);
                                                        myref.child("name").setValue(pseudootheruser);
                                                        myref.child("visibilite").setValue("ok");
                                                        myref.child("visibilitecb").setValue("ok");
                                                        ET_mail.setText("");
                                                        //ET_pseudo.setText("");
                                                        //Toast.makeText(Add_contacts.this,"Contact ajouté correctement", Toast.LENGTH_LONG).show();
                                                    }else {
                                                        Toast.makeText(Add_contacts.this,"Oupssss", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            DBRcheck2 = FBDcheck2.getReference("wines");
                                            DBRcheck2.child(mailwithoutdot).orderByChild("usermail").equalTo(usermail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            String chateaux = child.child("chateaux").getValue().toString();
                                                            String commentaire = child.child("commentaire").getValue().toString();
                                                            String type = child.child("type").getValue().toString();
                                                            String usermail = child.child("usermail").getValue().toString();
                                                            String pseudo = child.child("pseudo").getValue().toString();

                                                            DBRcheck2 = FBDcheck2.getReference("wines").child(mailcheck).push();
                                                            //Toast.makeText(Add_contacts.this,mailcheck, Toast.LENGTH_LONG).show();
                                                            DBRcheck2.child("chateaux").setValue(chateaux);
                                                            DBRcheck2.child("commentaire").setValue(commentaire);
                                                            DBRcheck2.child("type").setValue(type);
                                                            DBRcheck2.child("usermail").setValue(usermail);
                                                            DBRcheck2.child("pseudo").setValue(pseudo);
                                                            DBRcheck2.child("coupdecoeur").setValue("nok");
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            DBRcheck2 = FBDcheck2.getReference("wines");
                                            DBRcheck2.child(mailcheck).orderByChild("usermail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            String chateaux = child.child("chateaux").getValue().toString();
                                                            String commentaire = child.child("commentaire").getValue().toString();
                                                            String type = child.child("type").getValue().toString();
                                                            String usermail = child.child("usermail").getValue().toString();
                                                            String pseudo = child.child("pseudo").getValue().toString();

                                                            DBRcheck2 = FBDcheck2.getReference("wines").child(mailwithoutdot).push();
                                                            //Toast.makeText(Add_Wine.this,mailcontactwd, Toast.LENGTH_LONG).show();
                                                            DBRcheck2.child("chateaux").setValue(chateaux);
                                                            DBRcheck2.child("commentaire").setValue(commentaire);
                                                            DBRcheck2.child("type").setValue(type);
                                                            DBRcheck2.child("usermail").setValue(usermail);
                                                            DBRcheck2.child("pseudo").setValue(pseudo);
                                                            DBRcheck2.child("coupdecoeur").setValue("nok");

                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });

                                        */}
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        thread.start();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }else {
                                builder.setTitle("Oups");
                                builder.setMessage("Ce contact n'existe pas... veuillez reéssayer:)");
                                builder.setCancelable(false);
                                displayAlertcontactintrouvable();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                thread.start();*/

            }
        });
    }

    public void displayAlertcontactintrouvable() {
        builder.setPositiveButton("Compris", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ET_mail.setText("");
                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Add_contacts.this, Contacts.class);
        startActivity(i);
        finish();
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(Add_contacts.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

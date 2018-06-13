package thomasapp.wineforfriendsnd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AfficherContact extends AppCompatActivity {

    FirebaseDatabase FBDvisibilite;
    DatabaseReference DBRvisibilite;
    FirebaseDatabase FBDvisibilite2;
    DatabaseReference DBRvisibilite2;
    FirebaseDatabase FBDafficher;
    DatabaseReference DBRafficher;
    FirebaseDatabase FBDcdc;
    DatabaseReference DBRcdc;
    FirebaseAuth firebaseAuth;
    Button BT_affichercontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_contact);

        FBDvisibilite = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FBDvisibilite2 = FirebaseDatabase.getInstance();
        FBDafficher = FirebaseDatabase.getInstance();
        FBDcdc = FirebaseDatabase.getInstance();
        BT_affichercontact= (Button) findViewById(R.id.ibaffichercontact);

        final String tvcontactmail = getIntent().getExtras().getString("visibilite");

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //final String ajouteparresult = getIntent().getExtras().getString("Ajoutepar");

        BT_affichercontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                final String email = user.getEmail();
                assert email != null;
                final String mailwithoutdot = email.replace(".",",");
                final String contactmailsanspoint = tvcontactmail.replace(".",",");

                DBRvisibilite = FBDvisibilite.getReference("contacts");
                DBRvisibilite.child(contactmailsanspoint).orderByChild("contact_mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        DBRvisibilite.child(contactmailsanspoint).child(key).child("visibilite").setValue("ok");
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                DBRvisibilite = FBDvisibilite.getReference("contacts");
                DBRvisibilite.child(mailwithoutdot).orderByChild("contact_mail").equalTo(tvcontactmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        DBRvisibilite.child(mailwithoutdot).child(key).child("visibilitecb").setValue("ok");
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                DBRafficher = FBDafficher.getReference("wines");
                DBRafficher.child(contactmailsanspoint).orderByChild("usermail").equalTo(tvcontactmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                final String chateaux = child.child("chateaux").getValue().toString();
                                final String commentaire = child.child("commentaire").getValue().toString();
                                final String type = child.child("type").getValue().toString();
                                final String usermail = child.child("usermail").getValue().toString();
                                final String pseudo = child.child("pseudo").getValue().toString();
                                final String date = child.child("date").getValue().toString();

                                DBRcdc = FBDcdc.getReference("coupsdecoeur");
                                DBRcdc.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateaux).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            DBRafficher = FBDafficher.getReference("wines").child(mailwithoutdot).push();
                                            //Toast.makeText(Add_contacts.this,mailcheck, Toast.LENGTH_LONG).show();
                                            DBRafficher.child("chateaux").setValue(chateaux);
                                            DBRafficher.child("commentaire").setValue(commentaire);
                                            DBRafficher.child("type").setValue(type);
                                            DBRafficher.child("usermail").setValue(usermail);
                                            DBRafficher.child("pseudo").setValue(pseudo);
                                            DBRafficher.child("coupdecoeur").setValue("nok");
                                            DBRafficher.child("date").setValue(date);
                                        }
                                        else {
                                            DBRafficher = FBDafficher.getReference("wines").child(mailwithoutdot).push();
                                            //Toast.makeText(Add_contacts.this,mailcheck, Toast.LENGTH_LONG).show();
                                            DBRafficher.child("chateaux").setValue(chateaux);
                                            DBRafficher.child("commentaire").setValue(commentaire);
                                            DBRafficher.child("type").setValue(type);
                                            DBRafficher.child("usermail").setValue(usermail);
                                            DBRafficher.child("pseudo").setValue(pseudo);
                                            DBRafficher.child("coupdecoeur").setValue("ok");
                                            DBRafficher.child("date").setValue(date);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                thread.start();
            }
        });
        //Toast.makeText(MasquerContact.this, (CharSequence) tvcontactmail, Toast.LENGTH_LONG).show();
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(500); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(AfficherContact.this, Contacts.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

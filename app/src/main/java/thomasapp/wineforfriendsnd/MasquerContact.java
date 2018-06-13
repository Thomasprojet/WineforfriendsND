package thomasapp.wineforfriendsnd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MasquerContact extends AppCompatActivity {

    FirebaseDatabase FBDvisibilite2;
    DatabaseReference DBRvisibilite2;
    FirebaseDatabase FBDvisibilite3;
    DatabaseReference DBRvisibilite3;
    FirebaseDatabase FBDsupprimer;
    DatabaseReference DBRsupprimer;
    private FirebaseAuth firebaseAuth;
    Button BT_masquercontact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masquer_contact);

        firebaseAuth = FirebaseAuth.getInstance();
        FBDvisibilite2 = FirebaseDatabase.getInstance();
        FBDvisibilite3 = FirebaseDatabase.getInstance();
        FBDsupprimer = FirebaseDatabase.getInstance();
        BT_masquercontact = (Button) findViewById(R.id.ibmasquercontact);

        final String tvcontactmail = getIntent().getExtras().getString("visibilite");

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        BT_masquercontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                final String email = user.getEmail();
                assert email != null;
                final String mailwithoutdot = email.replace(".",",");
                assert tvcontactmail != null;
                final String contactmailsanspoint = tvcontactmail.replace(".",",");
                //Toast.makeText(MasquerContact.this, (CharSequence) contactmailsanspoint, Toast.LENGTH_LONG).show();

                DBRvisibilite2 = FBDvisibilite2.getReference("contacts");
                DBRvisibilite2.child(mailwithoutdot).orderByChild("contact_mail").equalTo(tvcontactmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                        DBRvisibilite2.child(mailwithoutdot).child(key).child("visibilitecb").setValue("nok");

                        DBRvisibilite3 = FBDvisibilite3.getReference("contacts");
                        DBRvisibilite3.child(contactmailsanspoint).orderByChild("contact_mail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                DBRvisibilite3.child(contactmailsanspoint).child(key).child("visibilite").setValue("nok");

                                DBRsupprimer = FBDsupprimer.getReference("wines");
                                DBRsupprimer.child(mailwithoutdot).orderByChild("usermail").equalTo(tvcontactmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       if (dataSnapshot.exists()) {
                                           for (DataSnapshot child : dataSnapshot.getChildren()) {
                                               child.getRef().setValue(null);
                                           }
                                       }
                                   }
                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {
                                   }
                                });
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
                thread.start();
            }
        });
    }
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(500); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(MasquerContact.this, Contacts.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.List;

public class Contacts extends Activity {

    ImageButton BT_addcontact;
    ImageView IV_flecheblanche;
    String usermail,mail,pseudo;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myref;
    AlertDialog.Builder builder;
    RecyclerView myrecyclerView;
    Contacts.RecyclerAdapterContacts adapterContacts;
    List<ContactsGETSET> listdata;
    FirebaseDatabase FBD;
    DatabaseReference DBR;
    FirebaseDatabase FBDcheck;
    DatabaseReference DBRcheck;
    FirebaseDatabase FBDverification;
    DatabaseReference DBRverification;
    FirebaseDatabase FBDverification2;
    DatabaseReference DBRverification2;
    FirebaseDatabase FBDverification4;
    DatabaseReference DBRverification4;
    FirebaseDatabase FBDverification5;
    DatabaseReference DBRverification5;
    FirebaseDatabase FBDcheck2;
    DatabaseReference DBRcheck2;
    //FirebaseDatabase FBDvisibilite2;
    //DatabaseReference DBRvisibilite2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressBar = (ProgressBar) findViewById(R.id.progressbarcontacts);
        BT_addcontact = (ImageButton) findViewById(R.id.ibaddcontacts);

        BT_addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contacts.this, Add_contacts.class);
                startActivity(i);
                finish();
            }
        });

        IV_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);

        IV_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contacts.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FBDcheck = FirebaseDatabase.getInstance();
        FBDverification = FirebaseDatabase.getInstance();
        FBDverification2 = FirebaseDatabase.getInstance();
        FBDverification4 = FirebaseDatabase.getInstance();
        FBDverification5 = FirebaseDatabase.getInstance();
        FBDcheck2 = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewcontacts);
        myrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        myrecyclerView.setLayoutManager(LM);

        listdata = new ArrayList<>();
        adapterContacts = new Contacts.RecyclerAdapterContacts((ArrayList<ContactsGETSET>) listdata);
        FBD = FirebaseDatabase.getInstance();

        builder = new AlertDialog.Builder(Contacts.this);
        firebaseAuth = FirebaseAuth.getInstance();

        assert user != null;
        final String email = user.getEmail();
        assert email != null;
        final String emaildecode = email.replace(".",",");
        progressBar.setVisibility(View.VISIBLE);
        DBRcheck = FBDcheck.getReference("contacts");
        DBRcheck.child(emaildecode).orderByChild("contact_mail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    DBRverification = FBDverification.getReference("verification2");
                    DBRverification.child(emaildecode).orderByChild("verification2").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                builder.setTitle("Woww votre première utilisation");
                                builder.setMessage("Pour l'instant votre liste de contacts est vide, vous pouvez ajouter un contact grace à la croix en bas à droite.");
                                builder.setCancelable(false);
                                displayAlertfirstuse();
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


                }else {
                    DBR = FBD.getReference("contacts");
                    DBR.child(emaildecode).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ContactsGETSET contactsGETSET = new ContactsGETSET();
                            contactsGETSET = dataSnapshot.getValue(ContactsGETSET.class);
                            listdata.add(contactsGETSET);
                            myrecyclerView.setAdapter(adapterContacts);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                        }
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
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
    }

    public class RecyclerAdapterContacts extends RecyclerView.Adapter<Contacts.RecyclerAdapterContacts.MyViewHolder>{

        ArrayList<ContactsGETSET> arrayList;

        public RecyclerAdapterContacts(ArrayList<ContactsGETSET>arrayList) {
            this.arrayList = arrayList;
        }
        @Override
        public Contacts.RecyclerAdapterContacts.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_contacts_list,parent,false);
            return  new Contacts.RecyclerAdapterContacts.MyViewHolder(view);


        }
        @Override
        public void onBindViewHolder(final Contacts.RecyclerAdapterContacts.MyViewHolder holder, int position) {
            ContactsGETSET contactsGETSET = arrayList.get(position);
            holder.tvcontactmail.setText(contactsGETSET.getContact_mail());
            holder.tvcontactname.setText(contactsGETSET.getName());
            holder.tvvisibilite.setText(contactsGETSET.getVisibilite());
            holder.tvvisibilitecb.setText(contactsGETSET.getVisibilitecb());

            if (holder.tvvisibilitecb.getText().toString().equals("ok")){
                holder.CBvisibilite.setChecked(true);
            }else {
                holder.CBvisibilite.setChecked(false);
            }



            holder.CBvisibilite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    final String email = user.getEmail();
                    assert email != null;
                    final String mailwithoutdot = email.replace(".",",");
                    String contactmail = holder.tvcontactmail.getText().toString();
                    final String contactmailsanspoint = contactmail.replace(".",",");
                    //Toast.makeText(Contacts.this, (CharSequence) holder.tvvisibilite, Toast.LENGTH_LONG).show();
                    //DBRcheck = FBDcheck.getReference("contacts");
                    //DBRcheck.child(contactmailsanspoint).orderByChild("visibilite").equalTo("ok").addListenerForSingleValueEvent(new ValueEventListener() {
                    //    @Override
                     //   public void onDataChange(DataSnapshot dataSnapshot) {
                    if (holder.tvvisibilitecb.getText().toString().equals("ok")) {
                        Intent i = new Intent(Contacts.this, MasquerContact.class);
                        i.putExtra("visibilite",holder.tvcontactmail.getText().toString());
                        startActivity(i);

                    }else {

                        Intent i = new Intent(Contacts.this, AfficherContact.class);
                        i.putExtra("visibilite",holder.tvcontactmail.getText().toString());
                        //i.putExtra("contactmailsanspoint",contactmailsanspoint);
                        startActivity(i);

                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvcontactmail,tvcontactname,tvvisibilite,tvvisibilitecb;
            CheckBox CBvisibilite;

            public MyViewHolder (View view)
            {
                super(view);
                tvcontactmail = (TextView) view.findViewById(R.id.tvcontactmail);
                tvcontactname = (TextView) view.findViewById(R.id.tvcontactpseudo);
                tvvisibilite = (TextView) view.findViewById(R.id.separation2);
                CBvisibilite = (CheckBox) view.findViewById(R.id.checkboxvisibilite);
                tvvisibilitecb = (TextView) view.findViewById(R.id.separation3);

            }
        }
    }

    public void displayAlertfirstuse() {

        builder.setPositiveButton("Compris", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.INVISIBLE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String email = user.getEmail();
                assert email != null;
                final String emaildecode = email.replace(".",",");
                DBRverification2 = FBDverification2.getReference("verification2").child(emaildecode).push();
                DBRverification2.child("verification2").setValue("ok");
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void displayAlertfriendrequest() {

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Contacts.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(Contacts.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

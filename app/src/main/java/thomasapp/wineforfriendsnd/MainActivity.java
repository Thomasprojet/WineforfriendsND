package thomasapp.wineforfriendsnd;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton IB1,IB2,IB3,IB4;
    AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;
    RecyclerView myrecyclerView;
    RecyclerAdapterWine adapterWine;
    List<WinesGETSET> listdata;
    FirebaseDatabase FBD;
    DatabaseReference DBR;
    FirebaseDatabase FBDpseudo;
    DatabaseReference DBRpseudo;
    FirebaseDatabase FBDverification;
    DatabaseReference DBRverification;
    FirebaseDatabase FBDverification2;
    DatabaseReference DBRverification2;
    FirebaseDatabase FBDverification4;
    DatabaseReference DBRverification4;
    FirebaseDatabase FBDverification5;
    DatabaseReference DBRverification5;
    FirebaseDatabase database;
    DatabaseReference myref;
    FirebaseDatabase FBDcheck;
    DatabaseReference DBRcheck;
    FirebaseDatabase FBDcheck2;
    DatabaseReference DBRcheck2;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressbarmain);
        //test

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        progressBar.setVisibility(View.VISIBLE);

        myrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewwine);
        myrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        listdata = new ArrayList<>();
        //adapterWine.notifyDataSetChanged();

        adapterWine = new RecyclerAdapterWine((ArrayList<WinesGETSET>) listdata);
        myrecyclerView.setLayoutManager(LM);
        //adapterWine.notifyDataSetChanged();
        myrecyclerView.setAdapter(adapterWine);


        FBD = FirebaseDatabase.getInstance();
        FBDpseudo = FirebaseDatabase.getInstance();
        FBDverification = FirebaseDatabase.getInstance();
        FBDverification2 = FirebaseDatabase.getInstance();
        FBDverification4 = FirebaseDatabase.getInstance();
        FBDverification5 = FirebaseDatabase.getInstance();
        FBDcheck = FirebaseDatabase.getInstance();
        FBDcheck2 = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();



        builder = new AlertDialog.Builder(MainActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseAuth.getCurrentUser()==null) {
            builder.setTitle("Bonjour et bienvenue sur WineForFriends");
            builder.setMessage("Pour commencer à utiliser l'application veuillez vous connecter ou créer un compte :)");
            builder.setCancelable(false);
            displayAlert();
        }

        else {

            assert user != null;
            final String email = user.getEmail();
            assert email != null;
            final String emaildecode = email.replace(".",",");

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View hView =  navigationView.getHeaderView(0);
            TextView nav_usermail = (TextView)hView.findViewById(R.id.textViewmail);
            TextView nav_username = (TextView)hView.findViewById(R.id.textviewname);

            nav_usermail.setText(email);
            nav_username.setText("Bonjour;)");
            progressBar.setVisibility(View.VISIBLE);
            DBRpseudo = FBDpseudo.getReference("wines");
            DBRpseudo.child(emaildecode).orderByChild("chateaux").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        DBRverification = FBDverification.getReference("verification1");
                        DBRverification.child(emaildecode).orderByChild("verification1").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    builder.setTitle("Woww votre première utilisation");
                                    builder.setMessage("Pour l'instant votre liste de vins et votre liste de contacts sont vides, vous pouvez ajouter un " +
                                            "vin en cliquant sur l'icône bouteille. Vous pouvez également ajouter un contact en cliquant sur l'autre icône." +
                                            " \n\nOn se lance? ");
                                    builder.setCancelable(false);
                                    displayAlertfirstuse();
                                }
                                else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    DBRverification4 = FBDverification4.getReference("verification4");
                                    DBRverification4.child(emaildecode).orderByChild("verification4").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                DBRverification5 = FBDverification5.getReference("verification4");
                                                DBRverification5.child(emaildecode).orderByChild("verification4").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                                        final String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                                        final String pseudo = dataSnapshot.child(key).child("pseudo").getValue().toString();
                                                        final String mail = dataSnapshot.child(key).child("mail").getValue().toString();
                                                        final String mailsansdot = mail.replace(".",",");
                                                        //Toast.makeText(Contacts.this,"pppp", Toast.LENGTH_LONG).show();
                                                        builder.setTitle("Vous avez une demande d'ami");
                                                        builder.setMessage("Accepter l'invitation de " +pseudo +"("+mail+")"+ " ?");
                                                        builder.setCancelable(false);
                                                        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                progressBar.setVisibility(View.VISIBLE);
                                                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                                myref = database.getReference("contacts").child(emaildecode).push();
                                                                myref.child("contact_mail").setValue(mail);
                                                                myref.child("name").setValue(pseudo);
                                                                myref.child("visibilite").setValue("ok");
                                                                myref.child("visibilitecb").setValue("ok");

                                                                DBRcheck = FBDcheck.getReference("users");
                                                                DBRcheck.child(emaildecode).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            final String pseudootheruser = dataSnapshot.child("name").getValue().toString();
                                                                            myref = database.getReference("contacts").child(mailsansdot).push();
                                                                            myref.child("contact_mail").setValue(email);
                                                                            myref.child("name").setValue(pseudootheruser);
                                                                            myref.child("visibilite").setValue("ok");
                                                                            myref.child("visibilitecb").setValue("ok");

                                                                            //Toast.makeText(Add_contacts.this,"Contact ajouté correctement", Toast.LENGTH_LONG).show();
                                                                        }else {
                                                                            //Toast.makeText(Contacts.this,"Oupssss", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                    }
                                                                });
                                                                DBRcheck2 = FBDcheck2.getReference("wines");
                                                                DBRcheck2.child(emaildecode).orderByChild("usermail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot != null) {
                                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                                String chateaux = child.child("chateaux").getValue().toString();
                                                                                String commentaire = child.child("commentaire").getValue().toString();
                                                                                String type = child.child("type").getValue().toString();
                                                                                String usermail = child.child("usermail").getValue().toString();
                                                                                String pseudo = child.child("pseudo").getValue().toString();

                                                                                DBRcheck2 = FBDcheck2.getReference("wines").child(mailsansdot).push();
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
                                                                DBRcheck2.child(mailsansdot).orderByChild("usermail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot != null) {
                                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                                String chateaux = child.child("chateaux").getValue().toString();
                                                                                String commentaire = child.child("commentaire").getValue().toString();
                                                                                String type = child.child("type").getValue().toString();
                                                                                String usermail = child.child("usermail").getValue().toString();
                                                                                String pseudo = child.child("pseudo").getValue().toString();

                                                                                DBRcheck2 = FBDcheck2.getReference("wines").child(emaildecode).push();
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
                                                                DBRverification4 = FBDverification4.getReference("verification4");
                                                                DBRverification4.child(emaildecode).orderByChild("pseudo").equalTo(pseudo).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                                                        for (DataSnapshot child: dataSnapshot2.getChildren()) {
                                                                            child.getRef().setValue(null);
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                    }
                                                                });
                                                                dialog.dismiss();
                                                                thread.start();
                                                            }
                                                        });
                                                        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                DBRverification4 = FBDverification4.getReference("verification4");
                                                                DBRverification4.child(emaildecode).orderByChild("pseudo").equalTo(pseudo).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                                                        for (DataSnapshot child: dataSnapshot2.getChildren()) {
                                                                            child.getRef().setValue(null);
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                    }
                                                                });
                                                                dialog.dismiss();
                                                                thread.start();
                                                            }
                                                        });
                                                        AlertDialog alertDialog = builder.create();
                                                        alertDialog.show();
                                                        //displayAlertfriendrequest();
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                            else {

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
                        /*builder.setTitle("Woww votre première utilisation");
                        builder.setMessage("Pour l'instant votre liste de vins et votre liste de contacts sont vides, vous pouvez ajouter un " +
                                "vin en cliquant sur l'icône bouteille. Vous pouvez également ajouter un contact en cliquant sur l'autre icône." +
                                " On se lance? ");
                        builder.setCancelable(false);
                        displayAlertfirstuse();*/

                    }
                    else {
                        DBRverification4 = FBDverification4.getReference("verification4");
                        DBRverification4.child(emaildecode).orderByChild("verification4").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    DBRverification5 = FBDverification5.getReference("verification4");
                                    DBRverification5.child(emaildecode).orderByChild("verification4").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                            final String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                            final String pseudo = dataSnapshot.child(key).child("pseudo").getValue().toString();
                                            final String mail = dataSnapshot.child(key).child("mail").getValue().toString();
                                            final String mailsansdot = mail.replace(".",",");
                                            //Toast.makeText(Contacts.this,"pppp", Toast.LENGTH_LONG).show();
                                            builder.setTitle("Vous avez une demande d'ami");
                                            builder.setMessage("Accepter l'invitation de " +pseudo +"("+mail+")"+ " ?");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    progressBar.setVisibility(View.VISIBLE);
                                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    myref = database.getReference("contacts").child(emaildecode).push();
                                                    myref.child("contact_mail").setValue(mail);
                                                    myref.child("name").setValue(pseudo);
                                                    myref.child("visibilite").setValue("ok");
                                                    myref.child("visibilitecb").setValue("ok");

                                                    DBRcheck = FBDcheck.getReference("users");
                                                    DBRcheck.child(emaildecode).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                final String pseudootheruser = dataSnapshot.child("name").getValue().toString();
                                                                myref = database.getReference("contacts").child(mailsansdot).push();
                                                                myref.child("contact_mail").setValue(email);
                                                                myref.child("name").setValue(pseudootheruser);
                                                                myref.child("visibilite").setValue("ok");
                                                                myref.child("visibilitecb").setValue("ok");

                                                                //Toast.makeText(Add_contacts.this,"Contact ajouté correctement", Toast.LENGTH_LONG).show();
                                                            }else {
                                                                //Toast.makeText(Contacts.this,"Oupssss", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                    DBRcheck2 = FBDcheck2.getReference("wines");
                                                    DBRcheck2.child(emaildecode).orderByChild("usermail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot != null) {
                                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                    String chateaux = child.child("chateaux").getValue().toString();
                                                                    String commentaire = child.child("commentaire").getValue().toString();
                                                                    String type = child.child("type").getValue().toString();
                                                                    String usermail = child.child("usermail").getValue().toString();
                                                                    String pseudo = child.child("pseudo").getValue().toString();

                                                                    DBRcheck2 = FBDcheck2.getReference("wines").child(mailsansdot).push();
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
                                                    DBRcheck2.child(mailsansdot).orderByChild("usermail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot != null) {
                                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                    String chateaux = child.child("chateaux").getValue().toString();
                                                                    String commentaire = child.child("commentaire").getValue().toString();
                                                                    String type = child.child("type").getValue().toString();
                                                                    String usermail = child.child("usermail").getValue().toString();
                                                                    String pseudo = child.child("pseudo").getValue().toString();

                                                                    DBRcheck2 = FBDcheck2.getReference("wines").child(emaildecode).push();
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
                                                    DBRverification4 = FBDverification4.getReference("verification4");
                                                    DBRverification4.child(emaildecode).orderByChild("pseudo").equalTo(pseudo).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot2) {
                                                            for (DataSnapshot child: dataSnapshot2.getChildren()) {
                                                                child.getRef().setValue(null);
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                    thread.start();
                                                }
                                            });
                                            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DBRverification4 = FBDverification4.getReference("verification4");
                                                    DBRverification4.child(emaildecode).orderByChild("pseudo").equalTo(pseudo).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot2) {
                                                            for (DataSnapshot child: dataSnapshot2.getChildren()) {
                                                                child.getRef().setValue(null);
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                    thread2.start();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            //displayAlertfriendrequest();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                                else {
                                    DBR = FBD.getReference("wines");
                                    DBR.child(emaildecode).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                new WinesGETSET();
                                                WinesGETSET winesGETSET;
                                                winesGETSET = postSnapshot.getValue(WinesGETSET.class);
                                                listdata.add(winesGETSET);
                                                adapterWine.notifyDataSetChanged();
                                                //myrecyclerView.setAdapter(adapterWine);

                                                progressBar.setVisibility(View.INVISIBLE);
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
                        //progressBar.setVisibility(View.VISIBLE);
                        /*addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                new WinesGETSET();
                                WinesGETSET winesGETSET;
                                winesGETSET = dataSnapshot.getValue(WinesGETSET.class);
                                listdata.add(winesGETSET);
                                adapterWine.notifyDataSetChanged();
                                //myrecyclerView.setAdapter(adapterWine);

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
                        });*/
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        IB1 = (ImageButton) findViewById(R.id.ibcontacts);
        IB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this, Contacts.class);
                startActivity(i);
                finish();
            }
        });

        IB2 = (ImageButton) findViewById(R.id.ibcroixrouge);
        IB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this, Add_Wine.class);
                startActivity(i);
                finish();
            }
        });

        IB3 = (ImageButton) findViewById(R.id.ibfiltrevin);
        IB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this, Filtre_par_couleur.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                //finish();
            }
        });

        IB4 = (ImageButton) findViewById(R.id.ibmapview);
        IB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
                //finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.textViewmail);
        assert user != null;
        String email = user.getEmail();

        if (firebaseAuth.getCurrentUser()==null) {
        }
        else {
            nav_user.setText(email);
        }*/
    }

    public class RecyclerAdapterWine extends RecyclerView.Adapter<RecyclerAdapterWine.MyViewHolder>{

        ArrayList<WinesGETSET> arrayList;

        public RecyclerAdapterWine(ArrayList<WinesGETSET>arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_wines_list,parent,false);
            return  new MyViewHolder (view);
        }

        @Override
        public void onBindViewHolder(final RecyclerAdapterWine.MyViewHolder holder, int position) {
            WinesGETSET winesGETSET = arrayList.get(position);
            holder.Chateaux.setText(winesGETSET.getChateaux());
            holder.Commentaire.setText(winesGETSET.getCommentaire());
            //holder.ajoutepar.setText(winesGETSET.getUsermail());
            holder.Type.setText(winesGETSET.getType());
            holder.pseudo.setText(winesGETSET.getPseudo());
            holder.Coupdecoeurtext.setText(winesGETSET.getCoupdecoeur());

            if (holder.Coupdecoeurtext.getText().toString().equals("ok")){
                holder.Coupdecoeur.setImageResource(R.mipmap.coeurrouge);}
            else if(holder.Coupdecoeurtext.getText().toString().equals("nok")){
                holder.Coupdecoeur.setImageResource(0);}



            if (holder.Type.getText().toString().equals("rouge")){
                holder.Typeimage.setImageResource(R.mipmap.rougerv);}
            else if(holder.Type.getText().toString().equals("blanc")){
                holder.Typeimage.setImageResource(R.mipmap.blancrv);}
            else if(holder.Type.getText().toString().equals("rose")){
                holder.Typeimage.setImageResource(R.mipmap.roserv);}
            else if(holder.Type.getText().toString().equals("champagne")){
                holder.Typeimage.setImageResource(R.mipmap.champagnerv);}
            else if(holder.Type.getText().toString().equals("spiritueux"))
                {holder.Typeimage.setImageResource(R.mipmap.spiritueuxrv);}
            else {
                holder.Typeimage.setImageResource(0);
                holder.Chateaux.setText("...");
                holder.Commentaire.setText("Référence en attente");
                holder.Type.setText("");
                holder.pseudo.setText("...");
                holder.Coupdecoeurtext.setText("");
                holder.Coupdecoeur.setImageResource(0);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.Type.getText().toString().equals("")){

                    }else {
                        Intent i = new Intent(MainActivity.this, OnClick_Wine.class);
                        i.putExtra("Type",holder.Type.getText().toString());
                        i.putExtra("Ajoutepar", holder.pseudo.getText().toString());
                        i.putExtra("Chateaux", holder.Chateaux.getText().toString());
                        //String hhh = holder.Commentaire.getText().toString();
                        //String premiercommentairesansdot = hhh.replace(".",",");
                        i.putExtra("Premiercommentaire", holder.Commentaire.getText().toString());
                        startActivity(i);
                        //Toast.makeText(getApplicationContext(), (CharSequence) holder.pseudo, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView Chateaux,Commentaire,Type, Coupdecoeurtext,pseudo;
            ImageView Typeimage,Coupdecoeur;

            public MyViewHolder (View view)
            {
                super(view);
                Chateaux = (TextView) view.findViewById(R.id.tvchateaux);
                pseudo = (TextView) view.findViewById(R.id.tvaddedby);
                Commentaire = (TextView) view.findViewById(R.id.tvcommentaire);
                Type = (TextView) view.findViewById(R.id.separation);
                Typeimage  = (ImageView) view.findViewById(R.id.ivtype);
                Coupdecoeurtext = (TextView) view.findViewById(R.id.coupdecoeurtext);
                Coupdecoeur  = (ImageView) view.findViewById(R.id.ivcoupdecoeur);

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Deconnetion) {
            builder.setTitle("Etes-vous sûr de vouloir quitter l'application?");
            builder.setMessage("");
            builder.setCancelable(false);
            displayAlertlogout();

        } /*else if (id == R.id.nav_Mes_vins) {
            Intent i = new Intent(MainActivity.this, Mes_Vins.class);
            startActivity(i);

        } else if (id == R.id.nav_Filtre) {
            Intent i = new Intent(MainActivity.this, Filtre_par_couleur.class);
            startActivity(i);

        } else if (id == R.id.nav_Bons_plans) {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(i);
            //finish();

        } else if (id == R.id.nav_coups_coeur) {
            Intent i = new Intent(MainActivity.this, Mes_Coupsdecoeur.class);
            startActivity(i);

        } /*else if (id == R.id.nav_send) {

        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayAlert() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(i);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void displayAlertlogout() {

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();
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

    public void displayAlertfirstuse() {

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.INVISIBLE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String email = user.getEmail();
                assert email != null;
                final String emaildecode = email.replace(".",",");
                DBRverification2 = FBDverification2.getReference("verification1").child(emaildecode).push();
                DBRverification2.child("verification1").setValue("ok");
                dialog.dismiss();
                //Intent i = new Intent(MainActivity.this, Add_Wine.class);
                //startActivity(i);
                //finish();
            }
        });
        /*builder.setNegativeButton("CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent i = new Intent(MainActivity.this, MapsActivity.class);
                //startActivity(i);
                //finish();
                progressBar.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });*/
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void displayAlertsupprimer() {

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent i = new Intent(MainActivity.this, Add_Wine.class);
                //startActivity(i);
                //finish();
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

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Thread thread2 = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(1500); // As I am using LENGTH_LONG in Toast
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

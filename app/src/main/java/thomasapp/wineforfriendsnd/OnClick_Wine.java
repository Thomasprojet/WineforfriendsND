package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class OnClick_Wine extends Activity {

    ImageView IV_reprisetype, IV_flecheblanche;
    TextView TV_repriseajoutepar, TV_reprisepremiercommentaire,TV_chateauxreprise;
    ImageButton BT_ajoutercommentaire,BT_supprimer, BT_coupsdecoeur;
    AlertDialog.Builder builder;
    FirebaseDatabase FBDsupprimer;
    DatabaseReference DBRsupprimer;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase FBDsupprimer2;
    DatabaseReference DBRsupprimer2;
    FirebaseDatabase FBDsupprimer3;
    DatabaseReference DBRsupprimer3;
    FirebaseDatabase FBDcoupsdecoeur1;
    DatabaseReference DBRcoupsdecoeur1;
    FirebaseDatabase FBDverification1;
    DatabaseReference DBRverification1;
    FirebaseDatabase FBDverification2;
    DatabaseReference DBRverification2;
    FirebaseDatabase FBDcoupsdecoeur2;
    DatabaseReference DBRcoupsdecoeur2;
    FirebaseDatabase FBDcoupsdecoeur3;
    DatabaseReference DBRcoupsdecoeur3;
    FirebaseDatabase FBDcheck;
    DatabaseReference DBRcheck;
    FirebaseDatabase FBD3;
    DatabaseReference DBR3;
    RecyclerView myrecyclerView;
    RecyclerAdapterMessages adapterMessages;
    List<MessagesGETSET> listdata;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click__wine);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        IV_reprisetype = (ImageView) findViewById(R.id.reprisetype);
        TV_repriseajoutepar =(TextView) findViewById(R.id.repriseajoutepar);
        TV_reprisepremiercommentaire =(TextView) findViewById(R.id.reprisepremiercommentaire);
        TV_chateauxreprise =(TextView) findViewById(R.id.reprischateaux);
        BT_ajoutercommentaire =(ImageButton) findViewById(R.id.ajouteruncommentaire);
        BT_supprimer =(ImageButton) findViewById(R.id.supprimerunvin);
        BT_coupsdecoeur =(ImageButton) findViewById(R.id.coupsdecoeur);
        progressBar = (ProgressBar) findViewById(R.id.progressbarmessages);

        builder = new AlertDialog.Builder(OnClick_Wine.this);

        firebaseAuth = FirebaseAuth.getInstance();
        FBDsupprimer = FirebaseDatabase.getInstance();
        FBDsupprimer2 = FirebaseDatabase.getInstance();
        FBDsupprimer3 = FirebaseDatabase.getInstance();
        FBDcoupsdecoeur1 = FirebaseDatabase.getInstance();
        FBDverification1 = FirebaseDatabase.getInstance();
        FBDverification2 = FirebaseDatabase.getInstance();
        FBDcoupsdecoeur2 = FirebaseDatabase.getInstance();
        FBDcoupsdecoeur3 = FirebaseDatabase.getInstance();
        FBDcheck = FirebaseDatabase.getInstance();
        FBD3 = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        myrecyclerView = (RecyclerView) findViewById(R.id.repriserecyclerview);
        myrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        myrecyclerView.setLayoutManager(LM);

        listdata = new ArrayList<>();
        adapterMessages = new RecyclerAdapterMessages((ArrayList<MessagesGETSET>) listdata);

        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String typeresult = getIntent().getExtras().getString("Type");
        final String ajouteparresult = getIntent().getExtras().getString("Ajoutepar");
        assert ajouteparresult != null;
        final String ajouteparsansdot = ajouteparresult.replace(".",",");
        final String premiercommentaireresult = getIntent().getExtras().getString("Premiercommentaire");
        assert premiercommentaireresult != null;
        final String premiercommentairesansdot = premiercommentaireresult.replace(".",",");
        final String chateauxresult = getIntent().getExtras().getString("Chateaux");
        assert chateauxresult != null;
        final String chateauxsansdot = chateauxresult.replace(".",",");

        final String rassemblement = chateauxsansdot+premiercommentairesansdot+ajouteparsansdot;

        TV_repriseajoutepar.setText(ajouteparresult);
        TV_reprisepremiercommentaire.setText(premiercommentaireresult);
        TV_chateauxreprise.setText(chateauxresult);

        if (typeresult.equals("rouge")){
            IV_reprisetype.setImageResource(R.mipmap.rougerv);}
        else if(typeresult.equals("blanc")){
            IV_reprisetype.setImageResource(R.mipmap.blancrv);}
        else if(typeresult.equals("rose")){
            IV_reprisetype.setImageResource(R.mipmap.roserv);}
        else if(typeresult.equals("champagne")){
            IV_reprisetype.setImageResource(R.mipmap.champagnerv);}
        else {IV_reprisetype.setImageResource(R.mipmap.spiritueuxrv);}

        IV_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);
        IV_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        assert user != null;
        String usermail = user.getEmail();
        assert usermail != null;
        final String mailwithoutdot = usermail.replace(".",",");

        DBRcoupsdecoeur1 = FBDcoupsdecoeur1.getReference("coupsdecoeur");
        DBRcoupsdecoeur1.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateauxresult).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    BT_coupsdecoeur.setImageResource(R.drawable.coeurblanc);

                }else {
                    BT_coupsdecoeur.setImageResource(R.drawable.coeurblancbarre);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        BT_coupsdecoeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert user != null;
                String usermail = user.getEmail();
                assert usermail != null;
                final String mailwithoutdot = usermail.replace(".",",");
                DBRcoupsdecoeur1 = FBDcoupsdecoeur1.getReference("coupsdecoeur");
                DBRcoupsdecoeur1.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateauxresult).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            DBRcoupsdecoeur2 = FBDcoupsdecoeur2.getReference("coupsdecoeur").child(mailwithoutdot).push();
                            DBRcoupsdecoeur2.child("chateaux").setValue(chateauxresult);
                            DBRcoupsdecoeur2.child("commentaire").setValue(premiercommentaireresult);
                            DBRcoupsdecoeur2.child("type").setValue(typeresult);
                            //DBRcoupsdecoeur2.child("usermail").setValue(usermail);
                            DBRcoupsdecoeur2.child("pseudo").setValue(ajouteparresult);

                            DBRcoupsdecoeur3 = FBDcoupsdecoeur3.getReference("wines");
                            DBRcoupsdecoeur3.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateauxresult).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                    String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                    DBRcoupsdecoeur3.child(mailwithoutdot).child(key).child("coupdecoeur").setValue("ok");

                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                }
                            });

                            progressBar.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(OnClick_Wine.this,"Ajout dans vos coups de coeur en cours ;)", Toast.LENGTH_SHORT).show();
                            thread.start();

                        }else {
                            builder.setTitle("Voulez vous retirer ce vin de vos coups de coeur?");
                            //builder.setMessage("Etes vous sur de vouloir supprimer cette référence? ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    assert user != null;
                                    String usermail = user.getEmail();
                                    assert usermail != null;
                                    final String mailwithoutdot = usermail.replace(".",",");
                                    DBRcoupsdecoeur1 = FBDcoupsdecoeur1.getReference("coupsdecoeur");
                                    DBRcoupsdecoeur1.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateauxresult).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot1) {
                                            for (DataSnapshot child: dataSnapshot1.getChildren()) {
                                                child.getRef().setValue(null);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                                    DBRcoupsdecoeur3 = FBDcoupsdecoeur3.getReference("wines");
                                    DBRcoupsdecoeur3.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateauxresult).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                                            DBRcoupsdecoeur3.child(mailwithoutdot).child(key).child("coupdecoeur").setValue("nok");

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Failed to read value
                                        }
                                    });
                                    thread.start();
                                }
                            });

                            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        BT_ajoutercommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OnClick_Wine.this, Add_Message.class);
                intent.putExtra("Premiercommentaire", premiercommentairesansdot);
                intent.putExtra("Premierchateaux", chateauxsansdot);
                intent.putExtra("Premierpseudo", ajouteparsansdot);
                startActivity(intent);
                finish();
            }
        });

        BT_supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String usermail = user.getEmail();
                final String ajoutepar = TV_repriseajoutepar.getText().toString();
                final String chateaux = TV_chateauxreprise.getText().toString().trim();
                assert usermail != null;
                final String mailwithoutdot = usermail.replace(".",",");

                DBRsupprimer = FBDsupprimer.getReference("users");
                DBRsupprimer.child(mailwithoutdot).orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String pseudo = dataSnapshot.child("name").getValue().toString();
                            //Toast.makeText(OnClick_Wine.this,pseudo, Toast.LENGTH_LONG).show();

                            if (pseudo.equals(ajoutepar)){

                                builder.setTitle("Attention cette action est definitive!");
                                builder.setMessage("Etes vous sur de vouloir supprimer cette référence? ");
                                builder.setCancelable(false);
                                displayAlertsupprimer();

                            }else {
                                builder.setTitle("Oups");
                                builder.setMessage("Vous n'etes pas à l'origine de cette référence, impossible de supprimer!");
                                builder.setCancelable(false);
                                displayAlertinterdictiondesupprimer();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        DBRcheck = FBDcheck.getReference("message");
        //assert premiercommentaireresult != null;
        DBRcheck.child(rassemblement).orderByChild("pseudo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    assert user != null;
                    String usermail = user.getEmail();
                    final String mailwithoutdot = usermail.replace(".",",");
                    DBRverification2 = FBDverification2.getReference("verification3");
                    DBRverification2.child(mailwithoutdot).orderByChild("verification3").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                builder.setTitle("3 options s'offrent à vous");
                                builder.setMessage("Ajouter un commentaire avec l'icône plus,"+ "\n\nAjouter à vos coups de coeur avec l'icône coeur," +"\n\nSupprimer cette référence avec le troisième icône,");
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

                    DBR3 = FBD3.getReference("message");
                    DBR3.child(rassemblement).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            MessagesGETSET messagesGETSET = new MessagesGETSET();
                            messagesGETSET = dataSnapshot.getValue(MessagesGETSET.class);
                            listdata.add(messagesGETSET);
                            myrecyclerView.setAdapter(adapterMessages);
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

    public class RecyclerAdapterMessages extends RecyclerView.Adapter<RecyclerAdapterMessages.MyViewHolder>{

        ArrayList<MessagesGETSET> arrayList;

        public RecyclerAdapterMessages(ArrayList<MessagesGETSET>arrayList) {
            this.arrayList = arrayList;
        }
        @Override
        public RecyclerAdapterMessages.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_messages_list,parent,false);
            return  new RecyclerAdapterMessages.MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerAdapterMessages.MyViewHolder holder, int position) {
            MessagesGETSET messagesGETSET = arrayList.get(position);
            holder.tvmessage.setText(messagesGETSET.getCommentairesuivant());
            holder.tvpseudo.setText(messagesGETSET.getPseudo());

        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvmessage,tvpseudo;

            public MyViewHolder (View view)
            {
                super(view);
                tvmessage = (TextView) view.findViewById(R.id.tvmessage);
                tvpseudo = (TextView) view.findViewById(R.id.tvpseudo);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //Intent i = new Intent(OnClick_Wine.this, MainActivity.class);
        //startActivity(i);
        finish();
    }

    public void displayAlertsupprimer() {

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String usermail = user.getEmail();
                final String chateaux = TV_chateauxreprise.getText().toString().trim();
                //type = TV_recap.getText().toString().trim();
                assert usermail != null;
                final String mailwithoutdot = usermail.replace(".",",");

                DBRsupprimer2 = FBDsupprimer2.getReference("contacts");
                DBRsupprimer2.child(mailwithoutdot).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String mailcontact = child.child("contact_mail").getValue().toString();
                                final String mailcontactwd = mailcontact.replace(".",",");

                                DBRsupprimer3 = FBDsupprimer3.getReference("wines");
                                DBRsupprimer3.child(mailcontactwd).orderByChild("chateaux").equalTo(chateaux).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                                for (DataSnapshot child: dataSnapshot1.getChildren()) {
                                                    child.getRef().setValue(null);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                });
                            }
                            DBRsupprimer3 = FBDsupprimer3.getReference("wines");
                            DBRsupprimer3.child(mailwithoutdot).orderByChild("chateaux").equalTo(chateaux).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                //progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(OnClick_Wine.this,"Supprimé correctement", Toast.LENGTH_SHORT).show();
                thread.start();
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

    public void displayAlertinterdictiondesupprimer() {

        builder.setPositiveButton("Compris", new DialogInterface.OnClickListener() {
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
                Intent i = new Intent(OnClick_Wine.this, MainActivity.class);
                startActivity(i);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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
                DBRverification1 = FBDverification1.getReference("verification3").child(emaildecode).push();
                DBRverification1.child("verification3").setValue("ok");
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /*public void displayAlertdejacoupdecoeur() {

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.VISIBLE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String email = user.getEmail();
                assert email != null;
                final String emaildecode = email.replace(".",",");
                DBRsupprimer3 = FBDsupprimer3.getReference("wines");

                DBRsupprimer3.child(emaildecode).orderByChild("chateaux").equalTo(chateaux).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        for (DataSnapshot child: dataSnapshot1.getChildren()) {
                            child.getRef().setValue(null);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/

}

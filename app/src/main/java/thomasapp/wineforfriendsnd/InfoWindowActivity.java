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

public class InfoWindowActivity extends Activity {

    AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;
    RecyclerView myrecyclerView;
    ImageView IM_flecheblanche;
    InfoWindowActivity.RecyclerAdapterinfocaviste adapterinfocaviste;
    List<CavisteinfoGETSET> listdata;
    FirebaseDatabase FBDinfocaviste1;
    DatabaseReference DBRinfocaviste1;
    FirebaseDatabase FBDinfocaviste2;
    DatabaseReference DBRinfocaviste2;
    FirebaseDatabase FBDinfocaviste3;
    DatabaseReference DBRinfocaviste3;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window);

        progressBar = (ProgressBar) findViewById(R.id.progressbarinfocaviste);
        IM_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);

        progressBar.setVisibility(View.VISIBLE);

        myrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewinfocaviste);
        myrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        listdata = new ArrayList<>();
        adapterinfocaviste = new InfoWindowActivity.RecyclerAdapterinfocaviste((ArrayList<CavisteinfoGETSET>) listdata);
        myrecyclerView.setLayoutManager(LM);

        IM_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoWindowActivity.this, MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
            }
        });

        FBDinfocaviste1 = FirebaseDatabase.getInstance();
        FBDinfocaviste2 = FirebaseDatabase.getInstance();
        FBDinfocaviste3 = FirebaseDatabase.getInstance();

        builder = new AlertDialog.Builder(InfoWindowActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        assert user != null;
        final String email = user.getEmail();
        assert email != null;
        final String emaildecode = email.replace(".", ",");

        final String infocavistefrominfowindow = getIntent().getExtras().getString("cavistetitle");
        assert infocavistefrominfowindow != null;


        DBRinfocaviste1 = FBDinfocaviste1.getReference("caviste");
        DBRinfocaviste1.orderByChild("caviste").equalTo(infocavistefrominfowindow).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        final String ref = child.child("ref").getValue().toString();
                        final String reff = child.child("reff").getValue().toString();
                        //final String prix = child.child("prix").getValue().toString();
                        //Toast.makeText(InfoWindowActivity.this,ref, Toast.LENGTH_SHORT).show();

                        new CavisteinfoGETSET();

                        DBRinfocaviste2 = FBDinfocaviste2.getReference("wines");
                        DBRinfocaviste2.child(emaildecode).orderByChild("chateaux").equalTo(ref).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        final String ref = child.child("chateaux").getValue().toString();

                                        final  String tt = ref+infocavistefrominfowindow;

                                        //Toast.makeText(InfoWindowActivity.this,tt, Toast.LENGTH_SHORT).show();

                                        DBRinfocaviste3 = FBDinfocaviste3.getReference("caviste");
                                        DBRinfocaviste3.orderByChild("refcaviste").equalTo(tt).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                //new CavisteinfoGETSET();
                                                CavisteinfoGETSET cavisteinfoGETSET;
                                                cavisteinfoGETSET = dataSnapshot.getValue(CavisteinfoGETSET.class);
                                                listdata.add(cavisteinfoGETSET);
                                                //myrecyclerView.setAdapter(adapterinfocaviste);
                                                adapterinfocaviste.notifyDataSetChanged();
                                                //progressBar.setVisibility(View.INVISIBLE);
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
                                    //Toast.makeText(getApplicationContext(),listdata3.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getApplicationContext(),"aa", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        DBRinfocaviste2 = FBDinfocaviste2.getReference("wines");
                        DBRinfocaviste2.child(emaildecode).orderByChild("chateaux").equalTo(reff).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        final String ref = child.child("chateaux").getValue().toString();

                                        final  String tt = ref+infocavistefrominfowindow;

                                        //Toast.makeText(InfoWindowActivity.this,tt, Toast.LENGTH_SHORT).show();

                                        DBRinfocaviste3 = FBDinfocaviste3.getReference("caviste");
                                        DBRinfocaviste3.orderByChild("reffcaviste").equalTo(tt).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                //new CavisteinfoGETSET();
                                                CavisteinfoGETSET cavisteinfoGETSET;
                                                cavisteinfoGETSET = dataSnapshot.getValue(CavisteinfoGETSET.class);
                                                listdata.add(cavisteinfoGETSET);
                                                //myrecyclerView.setAdapter(adapterinfocaviste);
                                                adapterinfocaviste.notifyDataSetChanged();
                                                //progressBar.setVisibility(View.INVISIBLE);
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
                                    //Toast.makeText(getApplicationContext(),listdata3.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getApplicationContext(),"aa", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        myrecyclerView.setAdapter(adapterinfocaviste);
                        adapterinfocaviste.notifyDataSetChanged();
                    }
                    //Toast.makeText(getApplicationContext(),listdata3.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(),"rr", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public class RecyclerAdapterinfocaviste extends RecyclerView.Adapter<InfoWindowActivity.RecyclerAdapterinfocaviste.MyViewHolder>{

        ArrayList<CavisteinfoGETSET> arrayList;

        public RecyclerAdapterinfocaviste(ArrayList<CavisteinfoGETSET>arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public InfoWindowActivity.RecyclerAdapterinfocaviste.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_cavisteinfo,parent,false);
            return  new InfoWindowActivity.RecyclerAdapterinfocaviste.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final InfoWindowActivity.RecyclerAdapterinfocaviste.MyViewHolder holder, int position) {
            CavisteinfoGETSET cavisteinfoGETSET = arrayList.get(position);
            holder.ref.setText(cavisteinfoGETSET.getRef());
            holder.ref2.setText(cavisteinfoGETSET.getRef2());
            holder.prix.setText(cavisteinfoGETSET.getPrix());
            //holder.ajoutepar.setText(winesGETSET.getUsermail());

            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, OnClick_Wine.class);
                    i.putExtra("Type",holder.Type.getText().toString());
                    i.putExtra("Ajoutepar", holder.pseudo.getText().toString());
                    i.putExtra("Chateaux", holder.Chateaux.getText().toString());
                    i.putExtra("Premiercommentaire", holder.Commentaire.getText().toString());
                    startActivity(i);
                    //Toast.makeText(getApplicationContext(), (CharSequence) holder.pseudo, Toast.LENGTH_LONG).show();
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ref,ref2,prix;
            ImageView Typeimage;

            public MyViewHolder (View view)
            {
                super(view);
                ref = (TextView) view.findViewById(R.id.tvref);
                ref2 = (TextView) view.findViewById(R.id.tvref2);
                prix = (TextView) view.findViewById(R.id.tvprix);
            }
        }
    }

    public void displayAlertfirstuse() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(InfoWindowActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(InfoWindowActivity.this, MapsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }
}

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

public class Mes_Coupsdecoeur extends Activity {

    AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;
    RecyclerView myrecyclerView;
    ImageView IM_flecheblanche;
    Mes_Coupsdecoeur.RecyclerAdapterCoupsdecoeur adapterWine;
    List<MesCoupsdecoeurGETSET> listdata;
    FirebaseDatabase FBD;
    DatabaseReference DBR;
    FirebaseDatabase FBDpseudo;
    DatabaseReference DBRpseudo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes__coupsdecoeur);

        progressBar = (ProgressBar) findViewById(R.id.progressbarmesvins);
        IM_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);

        progressBar.setVisibility(View.VISIBLE);

        myrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewcoupsdecoeur);
        myrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        listdata = new ArrayList<>();
        adapterWine = new Mes_Coupsdecoeur.RecyclerAdapterCoupsdecoeur((ArrayList<MesCoupsdecoeurGETSET>) listdata);
        myrecyclerView.setLayoutManager(LM);

        IM_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mes_Coupsdecoeur.this, Filtre_par_couleur.class);
                startActivity(i);
                finish();
            }
        });

        FBD = FirebaseDatabase.getInstance();
        FBDpseudo = FirebaseDatabase.getInstance();

        builder = new AlertDialog.Builder(Mes_Coupsdecoeur.this);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        assert user != null;
        final String email = user.getEmail();
        assert email != null;
        final String emaildecode = email.replace(".",",");

        progressBar.setVisibility(View.VISIBLE);
        DBRpseudo = FBDpseudo.getReference("coupsdecoeur");
        DBRpseudo.child(emaildecode).orderByChild("chateaux").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    builder.setTitle("Aucun coup de coeur pour le moment");
                    builder.setMessage("");
                    builder.setCancelable(false);
                    displayAlertfirstuse();

                }
                else {
                    DBR = FBD.getReference("coupsdecoeur");
                    DBR.child(emaildecode).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            new MesCoupsdecoeurGETSET();
                            MesCoupsdecoeurGETSET mesCoupsdecoeurGETSET;
                            mesCoupsdecoeurGETSET = dataSnapshot.getValue(MesCoupsdecoeurGETSET.class);
                            listdata.add(mesCoupsdecoeurGETSET);
                            myrecyclerView.setAdapter(adapterWine);
                            adapterWine.notifyDataSetChanged();
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


    public class RecyclerAdapterCoupsdecoeur extends RecyclerView.Adapter<Mes_Coupsdecoeur.RecyclerAdapterCoupsdecoeur.MyViewHolder>{

        ArrayList<MesCoupsdecoeurGETSET> arrayList;

        public RecyclerAdapterCoupsdecoeur(ArrayList<MesCoupsdecoeurGETSET>arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public Mes_Coupsdecoeur.RecyclerAdapterCoupsdecoeur.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_coupsdecoeur,parent,false);
            return  new Mes_Coupsdecoeur.RecyclerAdapterCoupsdecoeur.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Mes_Coupsdecoeur.RecyclerAdapterCoupsdecoeur.MyViewHolder holder, int position) {
            MesCoupsdecoeurGETSET mesCoupsdecoeurGETSET = arrayList.get(position);
            holder.Chateaux.setText(mesCoupsdecoeurGETSET.getChateaux());
            holder.Commentaire.setText(mesCoupsdecoeurGETSET.getCommentaire());
            //holder.ajoutepar.setText(winesGETSET.getUsermail());
            holder.Type.setText(mesCoupsdecoeurGETSET.getType());
            holder.pseudo.setText(mesCoupsdecoeurGETSET.getPseudo());


            if (holder.Type.getText().toString().equals("rouge")){
                holder.Typeimage.setImageResource(R.mipmap.rougerv);}
            else if(holder.Type.getText().toString().equals("blanc")){
                holder.Typeimage.setImageResource(R.mipmap.blancrv);}
            else if(holder.Type.getText().toString().equals("rose")){
                holder.Typeimage.setImageResource(R.mipmap.roserv);}
            else if(holder.Type.getText().toString().equals("champagne")){
                holder.Typeimage.setImageResource(R.mipmap.champagnerv);}
            else {holder.Typeimage.setImageResource(R.mipmap.spiritueuxrv);}

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
            TextView Chateaux,Commentaire,Type, ajoutepar,pseudo;
            ImageView Typeimage;

            public MyViewHolder (View view)
            {
                super(view);
                Chateaux = (TextView) view.findViewById(R.id.tvchateaux);
                pseudo = (TextView) view.findViewById(R.id.tvaddedby);
                Commentaire = (TextView) view.findViewById(R.id.tvcommentaire);
                Type = (TextView) view.findViewById(R.id.separation);
                Typeimage  = (ImageView) view.findViewById(R.id.ivtype);
            }
        }
    }

    public void displayAlertfirstuse() {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Mes_Coupsdecoeur.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Mes_Coupsdecoeur.this, Filtre_par_couleur.class);
        startActivity(i);
        finish();
    }
}

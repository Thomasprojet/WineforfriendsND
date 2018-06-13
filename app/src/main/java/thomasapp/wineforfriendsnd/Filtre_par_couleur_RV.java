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

public class Filtre_par_couleur_RV extends Activity {

    AlertDialog.Builder builder;
    private FirebaseAuth firebaseAuth;
    RecyclerView myrecyclerView;
    ImageView IM_flecheblanche;
    Filtre_par_couleur_RV.RecyclerAdapterWinetype adapterType;
    List<FiltrecouleurGETSET> listdata;
    FirebaseDatabase FBD;
    DatabaseReference DBR;
    FirebaseDatabase FBDpseudo;
    DatabaseReference DBRpseudo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre_par_couleur__rv);

        progressBar = (ProgressBar) findViewById(R.id.progressbarmesvins);
        IM_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);

        progressBar.setVisibility(View.VISIBLE);

        myrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewwinetype);
        myrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        listdata = new ArrayList<>();
        adapterType = new Filtre_par_couleur_RV.RecyclerAdapterWinetype((ArrayList<FiltrecouleurGETSET>) listdata);
        myrecyclerView.setLayoutManager(LM);

        IM_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Filtre_par_couleur_RV.this, Filtre_par_couleur.class);
                startActivity(i);
                finish();
            }
        });

        FBD = FirebaseDatabase.getInstance();
        FBDpseudo = FirebaseDatabase.getInstance();

        builder = new AlertDialog.Builder(Filtre_par_couleur_RV.this);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        assert user != null;
        final String email = user.getEmail();
        assert email != null;
        final String emaildecode = email.replace(".",",");

        final String type = getIntent().getExtras().getString("type");

        progressBar.setVisibility(View.VISIBLE);
        DBRpseudo = FBDpseudo.getReference("wines");
        DBRpseudo.child(emaildecode).orderByChild("type").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    builder.setTitle("Aucun vin de ce type pour le moment");
                    builder.setMessage("");
                    builder.setCancelable(false);
                    displayAlertfirstuse();

                }
                else {
                    DBR = FBD.getReference("wines");
                    DBR.child(emaildecode).orderByChild("type").equalTo(type).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            new FiltrecouleurGETSET();
                            FiltrecouleurGETSET filtrecouleurGETSET;
                            filtrecouleurGETSET = dataSnapshot.getValue(FiltrecouleurGETSET.class);
                            listdata.add(filtrecouleurGETSET);
                            myrecyclerView.setAdapter(adapterType);
                            adapterType.notifyDataSetChanged();
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


    public class RecyclerAdapterWinetype extends RecyclerView.Adapter<Filtre_par_couleur_RV.RecyclerAdapterWinetype.MyViewHolder>{

        ArrayList<FiltrecouleurGETSET> arrayList;

        public RecyclerAdapterWinetype(ArrayList<FiltrecouleurGETSET>arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public Filtre_par_couleur_RV.RecyclerAdapterWinetype.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_filtre_par_type,parent,false);
            return  new Filtre_par_couleur_RV.RecyclerAdapterWinetype.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final Filtre_par_couleur_RV.RecyclerAdapterWinetype.MyViewHolder holder, int position) {
            FiltrecouleurGETSET filtrecouleurGETSET = arrayList.get(position);
            holder.Chateaux.setText(filtrecouleurGETSET.getChateaux());
            holder.Commentaire.setText(filtrecouleurGETSET.getCommentaire());
            //holder.ajoutepar.setText(winesGETSET.getUsermail());
            holder.Type.setText(filtrecouleurGETSET.getType());
            holder.pseudo.setText(filtrecouleurGETSET.getPseudo());


            if (holder.Type.getText().toString().equals("rouge")){
                holder.Typeimage.setImageResource(R.mipmap.rougerv);}
            else if(holder.Type.getText().toString().equals("blanc")){
                holder.Typeimage.setImageResource(R.mipmap.blancrv);}
            else if(holder.Type.getText().toString().equals("rose")){
                holder.Typeimage.setImageResource(R.mipmap.roserv);}
            else if(holder.Type.getText().toString().equals("champagne")){
                holder.Typeimage.setImageResource(R.mipmap.champagnerv);}
            else {holder.Typeimage.setImageResource(R.mipmap.spiritueuxrv);}
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
                Intent i = new Intent(Filtre_par_couleur_RV.this, Filtre_par_couleur.class);
                startActivity(i);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Filtre_par_couleur_RV.this, Filtre_par_couleur.class);
        startActivity(i);
        finish();
    }


}

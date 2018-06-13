package thomasapp.wineforfriendsnd;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Filtre_par_couleur extends Activity {

    TextView TV_recap;
    ImageButton IB_rouge, IB_blanc, IB_rose, IB_champagne, IB_spiritueux,IB_coupdecoeur, IB_mesvins;
    String chateaux, commentaire, type, usermail;
    Button BT_addwine;
    ImageView IM_flecheblanche;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtre_par_couleur);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TV_recap = (TextView) findViewById(R.id.typevin);
        IB_rouge = (ImageButton) findViewById(R.id.imageButtonrouge);
        IB_blanc = (ImageButton) findViewById(R.id.imageButtonblanc);
        IB_rose = (ImageButton) findViewById(R.id.imageButtonrose);
        IB_champagne = (ImageButton) findViewById(R.id.imageButtonchamp);
        IB_spiritueux = (ImageButton) findViewById(R.id.imageButtonspirit);
        IB_coupdecoeur = (ImageButton) findViewById(R.id.imageButtoncoupdecoeur);
        IB_mesvins = (ImageButton) findViewById(R.id.imageButtonmesvins);
        BT_addwine= (Button) findViewById(R.id.buttonaddwinetobdd);
        IM_flecheblanche = (ImageView) findViewById(R.id.imageflecheblanche);
        progressBar = (ProgressBar) findViewById(R.id.progressbaraddwine);

        IM_flecheblanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Filtre_par_couleur.this, MainActivity.class);
                startActivity(i);
                finish();
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
                IB_coupdecoeur.setBackgroundColor(0);
                IB_mesvins.setBackgroundColor(0);

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
                IB_coupdecoeur.setBackgroundColor(0);
                IB_mesvins.setBackgroundColor(0);

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
                IB_coupdecoeur.setBackgroundColor(0);
                IB_mesvins.setBackgroundColor(0);

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
                IB_coupdecoeur.setBackgroundColor(0);
                IB_mesvins.setBackgroundColor(0);

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
                IB_coupdecoeur.setBackgroundColor(0);
                IB_mesvins.setBackgroundColor(0);

            }
        });
        IB_coupdecoeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("coupdecoeur");
                IB_rouge.setBackgroundColor(0);
                IB_blanc.setBackgroundColor(0);
                IB_rose.setBackgroundColor(0);
                IB_champagne.setBackgroundColor(0);
                IB_spiritueux.setBackgroundColor(0);
                IB_coupdecoeur.setBackgroundResource(R.drawable.roundimage);
                IB_mesvins.setBackgroundColor(0);

            }
        });
        IB_mesvins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_recap.setText("mesvins");
                IB_rouge.setBackgroundColor(0);
                IB_blanc.setBackgroundColor(0);
                IB_rose.setBackgroundColor(0);
                IB_champagne.setBackgroundColor(0);
                IB_spiritueux.setBackgroundColor(0);
                IB_coupdecoeur.setBackgroundColor(0);
                IB_mesvins.setBackgroundResource(R.drawable.roundimage);

            }
        });

        type = TV_recap.getText().toString().trim();

        BT_addwine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = TV_recap.getText().toString().trim();
                assert type != null;
                if (type.equals("rouge")|| type.equals("rose")||type.equals("blanc")|| type.equals("champagne")|| type.equals("spiritueux")){

                    Intent i = new Intent(Filtre_par_couleur.this, Filtre_par_couleur_RV.class);
                    i.putExtra("type",TV_recap.getText().toString().trim());
                    //i.putExtra("contactmailsanspoint",contactmailsanspoint);
                    startActivity(i);
                }else if (type.equals("coupdecoeur")){
                    Intent i = new Intent(Filtre_par_couleur.this, Mes_Coupsdecoeur.class);
                    startActivity(i);
                }else if (type.equals("mesvins")){
                    Intent i = new Intent(Filtre_par_couleur.this, Mes_Vins.class);
                    startActivity(i);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Filtre_par_couleur.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }
}

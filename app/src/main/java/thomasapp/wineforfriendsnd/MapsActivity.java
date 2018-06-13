package thomasapp.wineforfriendsnd;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    AlertDialog.Builder builder;
    List<String> listdata1;
    List<String> listdata3;
    FirebaseDatabase FBDcheck1;
    DatabaseReference DBRcheck1;
    FirebaseDatabase FBDcheck2;
    DatabaseReference DBRcheck2;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private static final int LOCATION_REQUEST_CODE = 101;
    private String TAG = "MapDemo";
    private GoogleMap mMap;
    List<CavisteinfoGETSET> listdata;
    FirebaseDatabase FBDinfocaviste1;
    DatabaseReference DBRinfocaviste1;
    FirebaseDatabase FBDinfocaviste2;
    DatabaseReference DBRinfocaviste2;
    FirebaseDatabase FBDinfocaviste3;
    DatabaseReference DBRinfocaviste3;
    FirebaseDatabase FBDverification;
    DatabaseReference DBRverification;
    FirebaseDatabase FBDverification2;
    DatabaseReference DBRverification2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FBDcheck1 = FirebaseDatabase.getInstance();
        FBDcheck2 = FirebaseDatabase.getInstance();
        FBDinfocaviste1 = FirebaseDatabase.getInstance();
        FBDinfocaviste2 = FirebaseDatabase.getInstance();
        FBDinfocaviste3 = FirebaseDatabase.getInstance();
        FBDverification = FirebaseDatabase.getInstance();
        FBDverification2 = FirebaseDatabase.getInstance();

        builder = new AlertDialog.Builder(MapsActivity.this);

        listdata1 = new ArrayList<>();
        listdata3 = new ArrayList<>();
        listdata = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        builder = new AlertDialog.Builder(MapsActivity.this);

        /*builder.setTitle("En construction!");
        builder.setMessage("Soyez patient, on vous reserve le meilleur");
        builder.setCancelable(false);
        displayAlertmapnotready();
        AlertDialog alert = builder.create();
        alert.show();*/

        assert user != null;
        final String email = user.getEmail();
        assert email != null;
        final String emaildecode = email.replace(".", ",");

        DBRcheck1 = FBDcheck1.getReference("wines");
        DBRcheck1.child(emaildecode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    builder.setTitle("Votre liste est vide!");
                    builder.setMessage("...");
                    builder.setCancelable(false);
                    displayAlertmapnotready();
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {

                    DBRverification = FBDverification.getReference("verification5");
                    DBRverification.child(emaildecode).orderByChild("verification5").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                builder.setTitle("premier pas sur cette map;)");
                                builder.setMessage("Si un caviste vend une des bouteille de votre liste principale alors un icône apparaîtra");
                                builder.setCancelable(false);
                                displayAlertfirstuse();
                            }
                            else {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                assert user != null;
                                final String email = user.getEmail();
                                assert email != null;
                                final String emaildecode = email.replace(".", ",");

                                DBRinfocaviste1 = FBDinfocaviste1.getReference("wines");
                                DBRinfocaviste1.child(emaildecode).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                                final String refchateaux = child.child("chateaux").getValue().toString();
                                                //final String prix = child.child("prix").getValue().toString();
                                                //Toast.makeText(InfoWindowActivity.this,ref, Toast.LENGTH_SHORT).show();

                                                DBRinfocaviste2 = FBDinfocaviste2.getReference("caviste");
                                                DBRinfocaviste2.orderByChild("ref").equalTo(refchateaux).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                final Double Ling = Double.valueOf(child.child("ling").getValue().toString());
                                                                final Double Lat = Double.valueOf(child.child("lat").getValue().toString());
                                                                final String caviste = child.child("caviste").getValue().toString();
                                                                //final String ref = child.child("refcaviste").getValue().toString();

                                                                DBRinfocaviste3 = FBDinfocaviste3.getReference("caviste");
                                                                DBRinfocaviste3.orderByChild("caviste").equalTo(caviste).addChildEventListener(new ChildEventListener() {
                                                                    @Override
                                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                                        /*new CavisteinfoGETSET();
                                                                        CavisteinfoGETSET cavisteinfoGETSET;
                                                                        cavisteinfoGETSET = dataSnapshot.getValue(CavisteinfoGETSET.class);
                                                                        listdata.add(cavisteinfoGETSET);
                                                                        String yy = cavisteinfoGETSET.getRef();
                                                                        String uu = cavisteinfoGETSET.getPrix();
                                                                        String yyuu = yy+uu;*/

                                                                        //Toast.makeText(MapsActivity.this,yyuu, Toast.LENGTH_SHORT).show();
                                                                        LatLng sydney = new LatLng(Lat, Ling);
                                                                        mMap.addMarker(new MarkerOptions().position(sydney).title(caviste));
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

                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                                DBRinfocaviste2 = FBDinfocaviste2.getReference("caviste");
                                                DBRinfocaviste2.orderByChild("reff").equalTo(refchateaux).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                final Double Ling = Double.valueOf(child.child("ling").getValue().toString());
                                                                final Double Lat = Double.valueOf(child.child("lat").getValue().toString());
                                                                final String caviste = child.child("caviste").getValue().toString();
                                                                //final String ref = child.child("refcaviste").getValue().toString();

                                                                DBRinfocaviste3 = FBDinfocaviste3.getReference("caviste");
                                                                DBRinfocaviste3.orderByChild("caviste").equalTo(caviste).addChildEventListener(new ChildEventListener() {
                                                                    @Override
                                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                                        //Toast.makeText(MapsActivity.this,yyuu, Toast.LENGTH_SHORT).show();
                                                                        LatLng sydney = new LatLng(Lat, Ling);
                                                                        mMap.addMarker(new MarkerOptions().position(sydney).title(caviste));
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

                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                            //Toast.makeText(getApplicationContext(),listdata3.toString(), Toast.LENGTH_SHORT).show();
                                        } else {

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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this,
                permissionType);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permissionType}, requestCode
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i = new Intent(MapsActivity.this, MapsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String cavistetitle = marker.getTitle();
                Intent i = new Intent(MapsActivity.this, InfoWindowActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("cavistetitle",cavistetitle);
                startActivity(i);
                //finish();
            }
        });
        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        assert locationManager != null;
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        //assert location != null;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));

        //double latitude = location.getLatitude();
        //double longitude = location.getLongitude();
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
    }

    /*public void displayAlertfirstuse() {

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }*/

    public void displayAlertmapnotready() {

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void displayAlertfirstuse() {

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String email = user.getEmail();
                assert email != null;
                final String emaildecode = email.replace(".",",");
                DBRverification2 = FBDverification2.getReference("verification5").child(emaildecode).push();
                DBRverification2.child("verification5").setValue("ok");
                dialog.dismiss();
                Intent i = new Intent(MapsActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

package com.tech12.venueexpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SPHome1Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String link;
    private ImageView image;
    private EditText name;
    private EditText capacity,price;
    private Button address,date,btn_register_venue;
    private Uri imguri;
    private ProgressDialog progressDialog;
    private StorageReference storage;
    private static final int GALLERY_REQUEST = 1;
    public   double lat=0,lon=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome1);
        image = (ImageView)findViewById(R.id.image);
        name = (EditText)findViewById(R.id.name);
        capacity = (EditText)findViewById(R.id.capacity);
        price = (EditText)findViewById(R.id.price);
        btn_register_venue =(Button)findViewById(R.id.btn_register_venue);
        address =(Button)findViewById(R.id.address);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        String id =mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers").child(id).child("Venue");
        storage = FirebaseStorage.getInstance().getReference().child("Venue");
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery= new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_REQUEST);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SPHome1Activity.this, MapsActivity.class));
            }

        });
        Intent i = getIntent();

//        Bundle bundle = getIntent().getExtras();
        lat=i.getDoubleExtra("Key1",0);
        lon=i.getDoubleExtra("Key2",0);
        btn_register_venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                String c = capacity.getText().toString();
                String p = price.getText().toString();
                if(n.equals("")||c.equals("")||p.equals(""))
                {
                    Toast.makeText(SPHome1Activity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else  if(lat==0 || lon==0)
                {
                    Toast.makeText(SPHome1Activity.this, "Please Select Your Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Adding Venue..");
                progressDialog.show();
                final DatabaseReference key = mDatabase;
                key.child("VName").setValue(n);
                key.child("Capacity").setValue(c);
                key.child("Price").setValue(p);
                key.child("Latitude").setValue(lat);
                key.child("Longitude").setValue(lon);
                final StorageReference path = storage.child(System.currentTimeMillis() + ".jpg");
                path.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // link = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        link = taskSnapshot.getDownloadUrl().toString();
                        key.child("image").setValue(link);
                        progressDialog.dismiss();
                        startActivity(new Intent(SPHome1Activity.this, SPHomeActivity.class));
                        finish();
                    }
                });
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SPHome1Activity.this, SPHomeActivity.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            imguri = data.getData();
            image.setImageURI(imguri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

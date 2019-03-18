package com.tech12.venueexpress;

import android.content.Context;
import android.content.Intent;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SPHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ImageView img;
    TextView sna,spr,scp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphome);

        mAuth = FirebaseAuth.getInstance();
        String id =mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Service Providers").child(id).child("Venue");

        img = (ImageView)findViewById(R.id.simage);
        sna=(TextView)findViewById(R.id.sname);
        spr=(TextView)findViewById(R.id.sprice);
        scp=(TextView)findViewById(R.id.scapacity);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    String i = dataSnapshot.child("image").getValue().toString();
                    String n = dataSnapshot.child("VName").getValue().toString();
                    String c = dataSnapshot.child("Capacity").getValue().toString();
                    String p = dataSnapshot.child("Price").getValue().toString();

                    Picasso.with(getApplicationContext()).load(i).into(img);
                    sna.setText(n);
                    spr.setText(p);
                    scp.setText(c);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.add)
        {
            startActivity(new Intent(getApplicationContext(), SPHome1Activity.class));
            return true;
        }
        if(item.getItemId()==R.id.logout)

        {
            mAuth.signOut();
            finish();
            startActivity(new Intent(SPHomeActivity.this, SPLoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.hfad.teach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.teach.Models.Notes;

import java.util.ArrayList;
import java.util.List;

public class MyNotesOverViewActivity extends AppCompatActivity {
    private List<Notes> note1;
    private RecyclerView mRecyclerview;
    private MyNotesAdapter adapter;
    int id;
    Notes note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes_over_view);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setTitle(R.string.my_notes);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        id = intent.getIntExtra("position", 0);
        loadNoteOverview();
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
    }
    private void loadNoteOverview() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("notes");
        Query query =reference1.orderByChild("user_id")
                //OR could use ->.orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                note1 = new ArrayList<>();

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    //                        if (firebaseUser.getUid().equals(postSnapShot.getKey())) {

                     note = postSnapShot.getValue(Notes.class);
                    note1.add(new Notes(note.getSubject(),note.getTopic(),note.getBody(),note.getName(),note.getUser_id(),note.getNote_id(),note.getAttach_url0(),note.getAttach_url1(),note.getAttach_url2(),note.getAttach_url3()));
                }

                adapter = new MyNotesAdapter(MyNotesOverViewActivity.this, note1 /*new MyNotesAdapter.ImageViewInterface() {
                    @Override
                    public void onImageViewLoaded(ImageView imageView) {
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(MyNotesOverViewActivity.this);
                                alert.setMessage("Are you Sure you want to Delete?");
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notes");
                                        reference.child(note1.get(id).getNote_id())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(MyNotesOverViewActivity.this,"Note Deleted",Toast.LENGTH_LONG).show();

                                                }
                                                else{Toast.makeText(MyNotesOverViewActivity.this,"Unable to Delete Note Try again Later",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                });
                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = alert.create();
                                alertDialog.show();
                            }
                        });
                    }

                }**/);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(MyNotesOverViewActivity.this);
                mRecyclerview.setLayoutManager(mLayoutManager);
                mRecyclerview.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
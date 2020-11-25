package com.hfad.teach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.Users;

import java.util.ArrayList;
import java.util.List;

public class MyNoteActivity extends AppCompatActivity {
    private int id;
    private TextView subject,topic,body,name;
    //List<Notes> text;
    ArrayList<Notes> text, attachUrl;
    Users users;
    int i = 0;
    ImageView[] img1,img2,img3,img4;
    ImageView attach,attach1,attach2,attach3,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_note);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setTitle("My Note");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        id = intent.getIntExtra("position", 0);
        subject = (TextView)findViewById(R.id.textView8);
        topic = (TextView)findViewById(R.id.textView9);
        name =(TextView)findViewById(R.id.textView10);
        body = (TextView)findViewById(R.id.textView11);
        delete = (ImageView)findViewById(R.id.delete);
        body.setMovementMethod(new ScrollingMovementMethod());
        getNotes();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyNoteActivity.this);
                alert.setMessage("Are you Sure you want to Delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseStorage storage1 = FirebaseStorage.getInstance();
                        StorageReference reference2 = storage1.getReference().child("/" + text.get(id).getNote_id() + "/" + "attach_url" + 0);
                        //Toast.makeText(MyNoteActivity.this,text.get(id).getNote_id(),Toast.LENGTH_LONG).show();
                        reference2.delete();
                        FirebaseStorage storage2 = FirebaseStorage.getInstance();
                        StorageReference reference3 = storage2.getReference().child("/" + text.get(id).getNote_id() + "/" + "attach_url" + 1);
                        //Toast.makeText(MyNoteActivity.this,text.get(id).getNote_id(),Toast.LENGTH_LONG).show();
                        reference3.delete();
                        FirebaseStorage storage3 = FirebaseStorage.getInstance();
                        StorageReference reference4 = storage3.getReference().child("/" + text.get(id).getNote_id() + "/" + "attach_url" + 2);
                        //Toast.makeText(MyNoteActivity.this,text.get(id).getNote_id(),Toast.LENGTH_LONG).show();
                        reference4.delete();
                        FirebaseStorage storage4 = FirebaseStorage.getInstance();
                        StorageReference reference5 = storage4.getReference().child("/" + text.get(id).getNote_id() + "/" + "attach_url" + 3);
                        //Toast.makeText(MyNoteActivity.this,text.get(id).getNote_id(),Toast.LENGTH_LONG).show();
                        reference5.delete();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notes");
                        reference.child(text.get(id).getNote_id())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(MyNoteActivity.this,"Note Deleted",Toast.LENGTH_LONG).show();
                                     finish();
                                }
                                else{Toast.makeText(MyNoteActivity.this,"Unable to Delete Note Try again Later",Toast.LENGTH_LONG).show();
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
    private void getNotes(){
        text = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        Query query =databaseReference.orderByChild("user_id")
                //OR could use ->.orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text = new ArrayList<>();
                attachUrl = new ArrayList<>();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    //                        if (firebaseUser.getUid().equals(postSnapShot.getKey())) {

                    Notes note = postSnapShot.getValue(Notes.class);
                    text.add(new Notes(note.getSubject(),note.getTopic(),note.getBody(),note.getName(),note.getUser_id(),note.getNote_id(),note.getAttach_url0(),note.getAttach_url1(),note.getAttach_url2(),note.getAttach_url3(),note.getType0(),note.getType1(),note.getType2(),note.getType3()));

                    //atachUrl.add(new Notes())
                }
                subject.setText(text.get(id).getSubject());
                topic.setText("Topic: " + text.get(id).getTopic());
                body.setText(text.get(id).getBody());
                name.setText("By: " + text.get(id).getName());
                while (i < 4){//NewNoteActivity.selectData.size()) {
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT);
                    ImageView[] img1 = new ImageView[10];
                    ImageView[] img2 = new ImageView[10];
                    img1[i] = new ImageView(getApplicationContext());
                    img2[i] = new ImageView(getApplicationContext());
                    img1[i].setLayoutParams(params2);
                    img2[i].setLayoutParams(params2);
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape2_layout);
                    img1[i].setBackground(drawable);
                    setMargins(img1[i], 50, 0, 50, 0);
                    img1[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    img2[i].setImageResource(R.drawable.action_download);
                    switch (i) {
                        case 0:
                            if(text.get(id).getAttach_url0()!=null){
                                LinearLayout r = (LinearLayout) findViewById(R.id.r1);
                                switch(text.get(id).getType0()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r.addView(img1[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r.addView(img1[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url0()).into(img1[i]);
                                        r.addView(img1[i]);
                                        break;
                                }
                                img1[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.parse(text.get(id).getAttach_url0()), "*/*");
                                        startActivity(intent);

                                    }
                                });
                            }
                            else{

                            }
                            break;
                        case 1:
                            if(text.get(id).getAttach_url1()!=null){
                                LinearLayout r1 = (LinearLayout) findViewById(R.id.r1);
                                switch(text.get(id).getType1()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r1.addView(img1[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r1.addView(img1[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url1()).into(img1[i]);
                                        r1.addView(img1[i]);
                                        break;
                                }
                                img1[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.parse(text.get(id).getAttach_url1()), "*/*");
                                        startActivity(intent);
                                    }
                                });
                            }
                            else{

                            }
                            break;
                        case 2:
                            if(text.get(id).getAttach_url2()!=null){
                                LinearLayout r2 = (LinearLayout) findViewById(R.id.r1);
                                switch(text.get(id).getType2()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r2.addView(img1[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r2.addView(img1[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url2()).into(img1[i]);
                                        r2.addView(img1[i]);
                                        break;
                                }
                                img1[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.parse(text.get(id).getAttach_url2()), "*/*");
                                        startActivity(intent);
                                    }
                                });
                            }
                            else{

                            }
                            break;
                        case 3:
                            if(text.get(id).getAttach_url3()!=null){
                                LinearLayout r3 = (LinearLayout) findViewById(R.id.r1);
                                switch(text.get(id).getType3()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r3.addView(img1[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r3.addView(img1[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url3()).into(img1[i]);
                                        r3.addView(img1[i]);
                                        break;
                                }
                                img1[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setDataAndType(Uri.parse(text.get(id).getAttach_url3()), "*/*");
                                        startActivity(intent);
                                    }
                                });
                            }
                            else{

                            }
                            break;
                        default:
                            break;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setMargins(View view, int left,int top,int right, int bottom){
        if(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
            p.setMargins(left,top,right,bottom);
            view.requestLayout();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
    }
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
    }
    }


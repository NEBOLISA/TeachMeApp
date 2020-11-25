package com.hfad.teach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.service.autofill.RegexValidator;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.Preview;
import com.hfad.teach.Models.Users;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    private int id;
    private TextView subject,topic,body,name;
    ImageView attach,attach1,attach2,attach3;
    ArrayList<Notes> text;
    LinearLayout r;
    ImageView[] img1,img2,img3,img4;
    TextView[] tx1;
    Users users;
    int i = 0;
    String fileExtension0;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setTitle("Note");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        id = intent.getIntExtra("position", 0);
        subject = (TextView)findViewById(R.id.textView8);
        topic = (TextView)findViewById(R.id.textView9);
        name =(TextView)findViewById(R.id.textView10);
        body = (TextView)findViewById(R.id.textView11);
        text = new ArrayList<>();
        getNotes();
        // Toast.makeText(NoteActivity.this,NewNoteActivity.pathExtension.get(0),Toast.LENGTH_LONG).show();
        body.setMovementMethod(new ScrollingMovementMethod());


    }
    private void getNotes(){
        //text = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    //                        if (firebaseUser.getUid().equals(postSnapShot.getKey()))
                    Notes note = postSnapShot.getValue(Notes.class);
                    text.add(new Notes(note.getSubject(), note.getTopic(), note.getBody(), note.getName(), note.getUser_id(), note.getNote_id(), note.getAttach_url0(), note.getAttach_url1(), note.getAttach_url2(), note.getAttach_url3(),note.getType0(),note.getType1(),note.getType2(),note.getType3()));
                }
                TextView tx = new TextView(getApplicationContext());
                tx.setText("Topic : ");
                subject.setText(text.get(id).getSubject());
                topic.setText(tx.getText() + text.get(id).getTopic());
                body.setText(text.get(id).getBody());
                name.setText("By: " + text.get(id).getName());

                while (i < 4){//NewNoteActivity.selectData.size()) {

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ImageView[] img1 = new ImageView[10];
                    ImageView[] img2 = new ImageView[10];
                    img1[i] = new ImageView(getApplicationContext());
                    img2[i] = new ImageView(getApplicationContext());
                    img1[i].setLayoutParams(params2);
                    img2[i].setLayoutParams(params2);
                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape2_layout);
                    img1[i].setBackground(drawable);
                    setMargins(img1[i], 40, 0, 40, 0);
                    img1[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    img2[i].setImageResource(R.drawable.action_download);
                    switch (i) {
                        case 0:
                            if(text.get(id).getAttach_url0()!=null){
                                LinearLayout r = (LinearLayout) findViewById(R.id.r1);
                                LinearLayout l = (LinearLayout) findViewById(R.id.r2);
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReference("/" + text.get(id).getNote_id());
                               final StorageReference ref = storageReference.child("/" + "attach_url" + 0);
                                switch(text.get(id).getType0()){
                                    case "pdf":
                                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                    r.addView(img1[i]);
                                    l.addView(img2[i]);
                                    break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                    r.addView(img1[i]);
                                        l.addView(img2[i]);
                                    break;
                                    default:
                                    Glide.with(getApplicationContext()).load(text.get(id).getAttach_url0()).into(img1[i]);
                                    r.addView(img1[i]);
                                        l.addView(img2[i]);
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
                                img2[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        downloadFiles(getApplicationContext(),"attach_url0.",text.get(id).getType0(),"download",text.get(id).getAttach_url0());
                                    }
                                });
                            }
                            else{

                            }
                            break;
                        case 1:
                            if(text.get(id).getAttach_url1()!=null){
                                LinearLayout r1 = (LinearLayout) findViewById(R.id.r1);
                                LinearLayout l1 = (LinearLayout) findViewById(R.id.r2);
                                switch(text.get(id).getType1()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r1.addView(img1[i]);
                                        l1.addView(img2[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r1.addView(img1[i]);
                                        l1.addView(img2[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url1()).into(img1[i]);
                                        r1.addView(img1[i]);
                                        l1.addView(img2[i]);
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
                                img2[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        downloadFiles(getApplicationContext(),"attach_url1.",text.get(id).getType1(),"download",text.get(id).getAttach_url1());
                                    }
                                });
                            }
                            else{

                            }
                            break;
                        case 2:

                            if(text.get(id).getAttach_url2()!=null){
                                LinearLayout r2 = (LinearLayout) findViewById(R.id.r1);
                                LinearLayout l2 = (LinearLayout) findViewById(R.id.r2);
                                switch(text.get(id).getType2()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r2.addView(img1[i]);
                                        l2.addView(img2[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r2.addView(img1[i]);
                                        l2.addView(img2[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url2()).into(img1[i]);
                                        r2.addView(img1[i]);
                                        l2.addView(img2[i]);
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
                                img2[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        downloadFiles(getApplicationContext(),"attach_url2.",text.get(id).getType2(),"download",text.get(id).getAttach_url2());
                                    }
                                });
                            }
                            else{

                            }
                            break;
                        case 3:
                            if(text.get(id).getAttach_url3()!=null){
                                LinearLayout r3 = (LinearLayout) findViewById(R.id.r1);
                                LinearLayout l3 = (LinearLayout) findViewById(R.id.r2);
                                switch(text.get(id).getType3()){
                                    case "pdf":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/ic_action_pdf.png?alt=media&token=b2adb922-9918-4a48-8ec6-a382af9a4641").into(img1[i]);
                                        r3.addView(img1[i]);
                                        l3.addView(img2[i]);
                                        break;
                                    case "doc":
                                    case "docx":
                                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/teach-c2c68.appspot.com/o/word.png?alt=media&token=2c71d47e-3db5-4d61-840c-e62ce2d8d2ca").into(img1[i]);
                                        r3.addView(img1[i]);
                                        l3.addView(img2[i]);
                                        break;
                                    default:
                                        Glide.with(getApplicationContext()).load(text.get(id).getAttach_url3()).into(img1[i]);
                                        r3.addView(img1[i]);
                                        l3.addView(img2[i]);
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
                                img2[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        downloadFiles(getApplicationContext(),"attach_url3.",text.get(id).getType3(),"download",text.get(id).getAttach_url3());
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
    private void getImage(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        Query query =databaseReference.orderByChild("user_id")
                //OR could use ->.orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notes note = dataSnapshot.getValue(Notes.class);
                Glide.with(getApplicationContext()).load(note.getAttach_url1()).into(attach);
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
    public void onStart(){
        super.onStart();
        getNotes();
    }
    @Override
    public void onResume(){
        super.onResume();
        getNotes();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        getNotes();
    }
    private String getMimeType(String url){
        String parts[] = url.split("\\.");
        String extension = parts[parts.length-1];
        String type=null;
        if (extension!=null){
            MimeTypeMap mime= MimeTypeMap.getSingleton();
            type=mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
    private void showDialog()
    {
        progressDialog = new ProgressDialog(NoteActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Downloading..");
        progressDialog.show();
    }
    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public void downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url){
        DownloadManager dm = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName + fileExtension);
        dm.enqueue(request);
    }
    public String GetFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
    }
}

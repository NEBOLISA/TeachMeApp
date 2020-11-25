package com.hfad.teach;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.Users;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewNoteActivity extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY=101;
   static Uri filepath;
    Button remove;
    int i=0;
    androidx.constraintlayout.widget.ConstraintLayout[] cl;
    TextView[] tv;
    ImageView[] mv,cancel;
    LinearLayout[] fl;
    static ArrayList<Uri> selectData;
    static ArrayList<String>pathExtension,fileExtension;
    private String creatorName;
    private ProgressDialog progressDialog;
    EditText textview,textview2,edit;
    UploadTask uploadTask;
    FirebaseStorage storage;
    StorageReference ref;
    StorageReference storageReference;
    ArrayList<UploadTask> up;
    String name;
    static String noteId;
    static Uri uri;
    //private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Create Note");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
         setName();
        selectData = new ArrayList<>();
        pathExtension = new ArrayList<>();
        fileExtension = new ArrayList<>();
        remove = (Button) findViewById(R.id.remove);
edit = (EditText)findViewById(R.id.editTextTextMultiLine);

edit.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(edit.hasFocus()){
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_SCROLL:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
            }
        }
        return false;
    }
});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_note,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            selectData.add(filepath);
//pathExtension.add(getNameFromURI(filepath));
//fileExtension.add(GetFileExtension(filepath));
        }
        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectData.get(i));
            if(i<=4) {

                final ArrayList<View> my = new ArrayList<>();
                RelativeLayout l =(RelativeLayout)findViewById(R.id.email_login_form);
                final LinearLayout r = (LinearLayout)findViewById(R.id.r1);
                fl = new LinearLayout[10];
                final LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                tv = new TextView[10];
                mv = new ImageView[10];
                fl[i] = new LinearLayout(this);
                fl[i].setLayoutParams(params2);
                fl[i].setOrientation(LinearLayout.VERTICAL);
                Drawable drawable = ContextCompat.getDrawable(this,R.drawable.shape2_layout);
                fl[i].setBackground(drawable);
                tv[i] = new TextView(this);
                mv[i] = new ImageView(this);
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                final LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,100);

                tv[i].setText(getNameFromURI(selectData.get(i)));
                mv[i].setImageBitmap(bitmap);
                mv[i].setLayoutParams(params);
                tv[i].setLayoutParams(params1);
                r.addView(fl[i]);
                fl[i].addView(tv[i]);
                fl[i].addView(mv[i]);
                if(selectData!=null){
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Toast.makeText(NewNoteActivity.this, "Attachments Removed",Toast.LENGTH_LONG).show();
                        //recreate();
                        r.removeAllViews();
                        selectData.removeAll(selectData);
                        recreate();
                    }
                });}
else {
    
                }
                i++;
            }

            else{
                Toast.makeText(this,"You can't attach more files",Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item){
        switch(item.getItemId()){
            case R.id.attach:
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.putExtra("return-data",true);
                startActivityForResult(Intent.createChooser(intent,"Select how u want"),PICK_FROM_GALLERY);
                return true;
            case R.id.post:
                if(filepath!=null) {
                textview = (EditText) findViewById(R.id.editText);
                textview2 = (EditText) findViewById(R.id.editText2);
                edit = (EditText) findViewById(R.id.editTextTextMultiLine);
                final String subject = textview.getText().toString();
                final String topic = textview2.getText().toString();
                final String body = edit.getText().toString();
                if (!subject.equals("") && !topic.equals("") && !body.equals("")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(NewNoteActivity.this);
                    alert.setMessage("Are you Sure you want to Save?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Notes note = new Notes();
                            noteId = reference
                                    .push().getKey();
                            note.setSubject(subject);
                            note.setTopic(topic);
                            note.setBody(body);
                            note.setName(creatorName);
                            note.setNote_id(noteId);
                            note.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            showDialog();
                            reference
                                    .child(getString(R.string.dbnode_notes))
                                    .child(noteId).setValue(note)

                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                switch(selectData.size()) {
                                                    case 1:
                                                        uploadImage(noteId, 0);
                                                    break;
                                                    case 2:
                                                        uploadImage(noteId, 0);
                                                        uploadImage(noteId, 1);
                                                    break;
                                                    case 3:
                                                        uploadImage(noteId, 0);
                                                        uploadImage(noteId, 1);
                                                        uploadImage(noteId, 2);
                                                    break;
                                                    case 4:
                                                        uploadImage(noteId, 0);
                                                        uploadImage(noteId, 1);
                                                        uploadImage(noteId, 2);
                                                        uploadImage(noteId, 3);
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                Toast.makeText(NewNoteActivity.this, "Note Saved", Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(NewNoteActivity.this, "Unable to Save Note Try again Later", Toast.LENGTH_LONG).show();
                                            }
                                            hideDialog();
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

                } else {
                    Toast.makeText(NewNoteActivity.this, "There is an empty field", Toast.LENGTH_LONG).show();
                }

            }
            else{
                    textview = (EditText) findViewById(R.id.editText);
                    textview2 = (EditText) findViewById(R.id.editText2);
                    edit = (EditText) findViewById(R.id.editTextTextMultiLine);
                    final String subject = textview.getText().toString();
                    final String topic = textview2.getText().toString();
                    final String body = edit.getText().toString();
                    if (!subject.equals("") && !topic.equals("") && !body.equals("")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(NewNoteActivity.this);
                        alert.setMessage("Are you Sure you want to Save?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Notes note = new Notes();
                                noteId = reference
                                        .push().getKey();
                                note.setSubject(subject);
                                note.setTopic(topic);
                                note.setBody(body);
                                note.setName(creatorName);
                                note.setNote_id(noteId);
                                note.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                showDialog();
                                reference
                                        .child(getString(R.string.dbnode_notes))
                                        .child(noteId).setValue(note)

                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    //uploadImage(noteId);

                                                    Toast.makeText(NewNoteActivity.this, "Note Saved", Toast.LENGTH_LONG).show();

                                                } else {
                                                    Toast.makeText(NewNoteActivity.this, "Unable to Save Note Try again Later", Toast.LENGTH_LONG).show();
                                                }
                                                hideDialog();
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

                    } else {
                        Toast.makeText(NewNoteActivity.this, "There is an empty field", Toast.LENGTH_LONG).show();
                    }

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void showDialog()
    {
        progressDialog = new ProgressDialog(NewNoteActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving..");
        progressDialog.show();
    }
    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void setName(){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users");
        Query query =reference1.orderByKey()
                //OR could use ->.orderByChild(getString(R.string.field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    //                        if (firebaseUser.getUid().equals(postSnapShot.getKey())) {


                    Users users = postSnapShot.getValue(Users.class);
                      creatorName = users.getName();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private  void uploadImage(final String note,final int j) {
//pathExtension = new ArrayList<>();
         uri = selectData.get(j);
           FirebaseStorage storage = FirebaseStorage.getInstance();
           StorageReference storageReference = storage.getReference(note);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            Notes notes = new Notes();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            final StorageReference ref = storageReference.child("/" + "attach_url" + j);
String extension = GetFileExtension(uri);
        FirebaseDatabase.getInstance().getReference().child("notes").child(noteId).child("type" + j).setValue(extension);
           // String fileExt = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            //pathExtension.add(fileExt);
            final UploadTask uploadTask = ref.putFile(uri);
           //final DatabaseReference fr= FirebaseDatabase.getInstance().getReference();
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("notes")
                                            .child(note)
                                            .child("attach_url" + j).setValue(task.getResult().toString());
                                }

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(NewNoteActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });



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





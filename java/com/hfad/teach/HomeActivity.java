package com.hfad.teach;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.UserType;
import com.hfad.teach.Models.Users;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private List<Notes>note1;
    private List<String>user;
    private String userName,key,userType,studentUser,teacherUser;
    private RecyclerView mRecyclerview;
    private NotesOverviewAdapter adapter;
    private ProgressBar mProgressBar;
    private TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //final FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users");
        Query query =reference1.orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Users users = postSnapShot.getValue(Users.class);
                    userType = users.getUser_type();
                }
                if(userType.equals("Teacher")){
                    FloatingActionButton fab = findViewById(R.id.floatingActionButton);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent1 = new Intent(getApplicationContext(), NewNoteActivity.class);
                            startActivity(intent1);
                        }
                    });
                }
                else if(userType.equals("Student")){
                    FloatingActionButton fab = findViewById(R.id.floatingActionButton);
                    fab.hide();
                    NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
                    nav.getMenu().removeItem(R.id.nav_new);
                    nav.getMenu().removeItem(R.id.nav_notes);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setTitle(R.string.home);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        setName();
        loadNoteOverview();

        //String kene = "Teacher User";
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
    /*else if(user.get(0) == "Teacher User"){
            Toast.makeText(HomeActivity.this,teacherUser,Toast.LENGTH_LONG).show();
        }**/
  }


   @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
       //SearchView searchView = (SearchView)item.getActionView();
       MenuItem menuItem = menu.findItem(R.id.search);
       SearchView searchView = (SearchView)menuItem.getActionView();

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               String userInput = newText.toLowerCase();
               List<Notes> newList = new ArrayList<>();
               if(note1 != null) {
                   for (Notes name : note1) {
                       if (name.getName() != null) {
                           if (name.getName().toLowerCase().contains(userInput)) {
                               newList.add(name);
                           }
                       } else {

                       }
                   }

                   adapter.updateList(newList);
                   mRecyclerview.setAdapter(adapter);
               }
               else{

               }
               return true;
           }
       });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item){
        switch(item.getItemId()){
            case R.id.search:
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    private void setName(){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users");
        Query query =reference1.orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Users users = postSnapShot.getValue(Users.class);
                    userName = users.getName();
                }
                View view;
                NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
                View header = nav.getHeaderView(0);
                name = (TextView)header.findViewById(R.id.name);
                email = (TextView)header.findViewById(R.id.email);
                name.setText(userName);
                email.setText(FirebaseAuth
                        .getInstance().getCurrentUser().getEmail());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadNoteOverview() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                note1 = new ArrayList<>();

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    //                        if (firebaseUser.getUid().equals(postSnapShot.getKey())) {

                    Notes note = postSnapShot.getValue(Notes.class);
                    note1.add(new Notes(note.getSubject(),note.getTopic(),note.getBody(),note.getName(),note.getUser_id(),note.getNote_id(),note.getAttach_url1(),note.getAttach_url1(),note.getAttach_url2(),note.getAttach_url3()));
                }

                adapter = new NotesOverviewAdapter(HomeActivity.this,note1);
                RecyclerView.LayoutManager mLayoutManager = new
                        LinearLayoutManager(HomeActivity.this);
                mRecyclerview.setLayoutManager(mLayoutManager);
                mRecyclerview.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;
        switch(id){
            case R.id.nav_home:
                intent = new Intent(this,HomeActivity.class);
                break;
            case R.id.nav_notes:
              intent = new Intent(this, MyNotesOverViewActivity.class);
              break;
            case R.id.nav_new:
                intent = new Intent(this,NewNoteActivity.class);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();}
        moveTaskToBack(true);
    }

}
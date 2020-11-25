package com.hfad.teach;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.teach.Models.NoteCollection;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.Users;

import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter extends ArrayAdapter<NoteCollection> {
    private static final String TAG = "NoteListAdapter";
    private int mLayoutResource;
    private Context mContext;
    private LayoutInflater mInflater;
    public NoteListAdapter(@NonNull Context context, int resource, @NonNull List<NoteCollection> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutResource = resource;
        mInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public static class ViewHolder{
        TextView subject,topic,name;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView==null){
            convertView =mInflater.inflate(mLayoutResource,parent,false);
            holder = new ViewHolder();
            holder.subject=(TextView)convertView.findViewById(R.id.subject);
            holder.topic=(TextView)convertView.findViewById(R.id.topic);
            holder.name=(TextView)convertView.findViewById(R.id.name);
        }
        else{
            holder =(ViewHolder)convertView.getTag();
        }
        try{

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query1 = reference.child("notes").orderByKey().equalTo(getItem(position).getCreator_id());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot:dataSnapshot.getChildren()){
                        String subject = singleSnapshot.getValue(Notes.class).getSubject();
                        String topic = singleSnapshot.getValue(Notes.class).getTopic();
                        holder.subject.setText(subject);
                        holder.topic.setText(topic);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Query query = reference.child(mContext.getString(R.string.dbnode_users))
                    .orderByKey().equalTo(getItem(position).getCreator_id());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot:dataSnapshot.getChildren()){
                        String noteBy = "Note By" + singleSnapshot.getValue(Users.class).getName();
                        holder.name.setText(noteBy);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch(NullPointerException e){
            Log.e(TAG, "getView: NullPointerException: ", e.getCause() );
        }
        return convertView;
    }
}

package com.hfad.teach;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.MyNoteActivity;

import java.util.Collections;
import java.util.List;

public class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.ViewHolder> {
    private Context mContext;
    private List<Notes> subTopic;
    static String my;
public interface ImageViewInterface{
    void onImageViewLoaded(ImageView imageView);

}
//final ImageViewInterface imageViewInterface;
    public MyNotesAdapter(Context mContext, List<Notes> subTopic){
        this.mContext = mContext;
        this.subTopic = subTopic;

    }
    @NonNull
    @Override
    public MyNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_notes_card, parent, false);
        return new MyNotesAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotesAdapter.ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        TextView text1 = (TextView)cardView.findViewById(R.id.subject);
        TextView text2 = (TextView)cardView.findViewById(R.id.topic);
        ImageView img = (ImageView)cardView.findViewById(R.id.delete);
        final Notes note = subTopic.get(position);

//imageViewInterface.onImageViewLoaded(img);

        text1.setText(note.getSubject());
        my = note.getNote_id();
        text2.setText(note.getTopic());
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardView.getContext(), MyNoteActivity.class);
                intent.putExtra("position", position);
                cardView.getContext().startActivity(intent);}});
       /* img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(cardView.getContext());
                alert.setMessage("Are you Sure you want to Delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notes");
                        reference.child(note.getNote_id())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(cardView.getContext(),"Note Deleted",Toast.LENGTH_LONG).show();

                                }
                                else{Toast.makeText(cardView.getContext(),"Unable to Delete Note Try again Later",Toast.LENGTH_LONG).show();
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
        });**/
    }

    @Override
    public int getItemCount() {
        return subTopic.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}

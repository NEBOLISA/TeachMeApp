package com.hfad.teach;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class NotesOverviewAdapter extends RecyclerView.Adapter<NotesOverviewAdapter.ViewHolder> {
    private Context mContext;
    private List<Notes> subTopic;
    public NotesOverviewAdapter(Context mContext, List<Notes> subTopic){
        this.mContext = mContext;
        this.subTopic = subTopic;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
final CardView cardView = holder.cardView;
        TextView text1 = (TextView)cardView.findViewById(R.id.subject);
        TextView text2 = (TextView)cardView.findViewById(R.id.topic);
        TextView text3 = (TextView)cardView.findViewById(R.id.name);
Notes note = subTopic.get(position);


        text1.setText(note.getSubject());
        text2.setText(note.getTopic());
        text3.setText("By: " + note.getName());
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardView.getContext(), NoteActivity.class);
                intent.putExtra("position", position);
                cardView.getContext().startActivity(intent);}});
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
    public void updateList(List<Notes>newList){
        subTopic=new ArrayList<>();
        subTopic.addAll(newList);
        notifyDataSetChanged();
    }
}

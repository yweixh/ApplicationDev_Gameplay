package com.example.final_gameplaydb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_gameplaydb.Model.Question;
import com.example.final_gameplaydb.R;

import java.util.List;

public class GameplayAdapter extends FirestoreRecyclerAdapter<Question, GameplayAdapter.GameplayHolder> {

    public GameplayAdapter(@NonNull FirestoreRecyclerOptions<Question> options) {
        super(options);
    }

    @NonNull
    @Override
    public GameplayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gameplay, parent, false);
        return new GameplayHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull GameplayHolder holder, int position, @NonNull Question model) {
        holder.question.setText(model.getQuestion());
        holder.first.setText(model.getOptionA());
        holder.second.setText(model.getOptionB());
        holder.third.setText(model.getOptionC());
        holder.fourth.setText(model.getOptionD());
    }

    static class GameplayHolder extends RecyclerView.ViewHolder {
        TextView question, first, second, third, fourth;

        public GameplayHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.Question);
            first = itemView.findViewById(R.id.FirstChoice);
            second = itemView.findViewById(R.id.SecondChoice);
            third = itemView.findViewById(R.id.ThirdChoice);
            fourth = itemView.findViewById(R.id.FourthChoice);
        }
    }
}

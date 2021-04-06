package com.example.tejidospulido_app.Presentador.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tejidospulido_app.Model.Classes.Valoration;
import com.example.tejidospulido_app.R;

import java.util.ArrayList;

public class AdapterValorations extends RecyclerView.Adapter<AdapterValorations.ViewHolder> {

    ArrayList<Valoration> valorations;

    public AdapterValorations(ArrayList<Valoration> valorations){
        this.valorations = valorations;
    }

    @NonNull
    @Override
    public AdapterValorations.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_valoration, parent,false);
        return new AdapterValorations.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterValorations.ViewHolder holder, int position) {
        Valoration v = valorations.get(position);
        if (v!= null) {
            holder.customerName.setText(v.getUsername());
            holder.valorationComment.setText(v.getComentario());
            String val = v.getValoracion();
            if (val.equals("0") || val.equals("1")) {
                holder.valorationImage.setImageResource(R.drawable.ic_rating1);
            } else if (val.equals("2")) {
                holder.valorationImage.setImageResource(R.drawable.ic_rating2);
            } else if (val.equals("3")) {
                holder.valorationImage.setImageResource(R.drawable.ic_rating3);
            } else if (val.equals("4")) {
                holder.valorationImage.setImageResource(R.drawable.ic_rating4);
            } else {
                holder.valorationImage.setImageResource(R.drawable.ic_rating5);
            }
        }
    }

    @Override
    public int getItemCount() {
        return valorations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView customerName, valorationComment;
        ImageView valorationImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            valorationComment = itemView.findViewById(R.id.valorationComment);
            valorationImage = itemView.findViewById(R.id.valorationImage);
        }
    }
}



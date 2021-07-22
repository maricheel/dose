package com.example.if_dose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.if_dose.Models.Aliment;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView namealim,unite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namealim=itemView.findViewById(R.id.nomalim);
            unite=itemView.findViewById(R.id.unite );
        }
    }
    private Context context;
    private ArrayList<Aliment> listamlim;

    public RecyclerAdapter(Context context, ArrayList<Aliment> listalim){
        this.context=context;
        this.listamlim=listalim;
    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.listview_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
                Aliment aliment=listamlim.get(position);
                holder.namealim.setText(aliment.getName());
                holder.unite.setText(aliment.getQuantiteA());
    }




    @Override
    public int getItemCount() {
        return listamlim.size();
    }
}

package com.example.asus.academiccalendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_kategori extends RecyclerView.Adapter<adapter_kategori.kategoriViewHolder> {

    private Context context;
    private ArrayList<Kategori> kategoriList;
    private OnItemClickListener onItemClickListener;

    public adapter_kategori(Context context, ArrayList<Kategori> kategoriList) {
        this.context = context;
        this.kategoriList = kategoriList;
    }

    public ArrayList<Kategori> getKategoriList() {
        return kategoriList;
    }

    public void setKategoriList(ArrayList<Kategori> kategoriList) {
        this.kategoriList = kategoriList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener1) {
        this.onItemClickListener = onItemClickListener1;
    }

    @NonNull
    @Override
    public adapter_kategori.kategoriViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.master_kategori, viewGroup, false);
        adapter_kategori.kategoriViewHolder kat_vh = new adapter_kategori.kategoriViewHolder(view);
        return kat_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_kategori.kategoriViewHolder kategoriViewHolder, int i) {
        Kategori kat = kategoriList.get(i);
        kategoriViewHolder.namakat.setText(kat.getNama());
        if (kat.getNotifikasi() == -1){
            kategoriViewHolder.notifikasi.setText("NOTIFIKASI : NO");
            kategoriViewHolder.daybefore.setVisibility(View.INVISIBLE);
        }
        else {
            kategoriViewHolder.notifikasi.setText("NOTIFIKASI : YES");
            kategoriViewHolder.daybefore.setVisibility(View.VISIBLE);
            kategoriViewHolder.daybefore.setText("Day Before : " + kat.getNotifikasi() + "");
        }
        kategoriViewHolder.warna.setBackgroundColor(kat.getWarna());
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    public class kategoriViewHolder extends RecyclerView.ViewHolder {

        TextView namakat, notifikasi, daybefore;
        ImageView warna;
        ConstraintLayout edit;

        public kategoriViewHolder(@NonNull final View itemView) {
            super(itemView);
            namakat = itemView.findViewById(R.id.namakat);
            notifikasi = itemView.findViewById(R.id.notifikasi);
            daybefore = itemView.findViewById(R.id.day);
            edit = itemView.findViewById(R.id.imgviewedit);
            warna = itemView.findViewById(R.id.warna);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onClick(itemView,getAdapterPosition());
                    }
                }
            });
        }
    }
}
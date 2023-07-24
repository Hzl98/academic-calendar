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

public class adapter_kegiatan extends RecyclerView.Adapter<adapter_kegiatan.kegiatanViewholder> {

    private Context context;
    private ArrayList<Kegiatan> kegiatanList;
    private OnItemClickListener onItemClickListener;

    public adapter_kegiatan(Context context, ArrayList<Kegiatan> kegiatanList) {
        this.context = context;
        this.kegiatanList = kegiatanList;
    }

    public ArrayList<Kegiatan> getKegiatanList() {
        return kegiatanList;
    }

    public void setKegiatanList(ArrayList<Kegiatan> kegiatanList) {
        this.kegiatanList = kegiatanList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener1) {
        this.onItemClickListener = onItemClickListener1;
    }

    @NonNull
    @Override
    public adapter_kegiatan.kegiatanViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.rcview_kegiatan, viewGroup, false);
        kegiatanViewholder keg_vh = new kegiatanViewholder(view);
        return keg_vh;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_kegiatan.kegiatanViewholder kegiatanViewholder, int i) {
        Kegiatan keg = kegiatanList.get(i);
        if (keg.getTglawal().equalsIgnoreCase(keg.getTglakhir())){
            kegiatanViewholder.txtviewtgl.setText(keg.getTglawal());
        }
        else {kegiatanViewholder.txtviewtgl.setText(keg.getTglawal() + " - " + keg.getTglakhir());}
        if (keg.getNama().length() > 28){
            kegiatanViewholder.txtviewnamakegiatan.setText(keg.getNama().substring(0, 28) + "...");
        }
        else {
            kegiatanViewholder.txtviewnamakegiatan.setText(keg.getNama());
        }
        kegiatanViewholder.imviewwarna.setBackgroundColor(keg.getWarna());
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    public class kegiatanViewholder extends RecyclerView.ViewHolder {

        TextView txtviewtgl, txtviewnamakegiatan;
        ImageView imviewwarna;
        ConstraintLayout imgviewedit;

        public kegiatanViewholder(@NonNull final View itemView) {
            super(itemView);
            txtviewtgl = itemView.findViewById(R.id.txtviewtgl);
            txtviewnamakegiatan = itemView.findViewById(R.id.txtviewnamakegiatan);
            imviewwarna = itemView.findViewById(R.id.warna);
            imgviewedit = itemView.findViewById(R.id.imgviewedit);

            imgviewedit.setOnClickListener(new View.OnClickListener() {
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

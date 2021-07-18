package com.polije.sipakost.aplikasikos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;



public class OneFragmentAdapter extends RecyclerView.Adapter<OneFragmentAdapter.ViewHolder> {
    Context context;
    List<DataKost> dataKostList;

    public OneFragmentAdapter(Context context, List<DataKost> dataKostList) {
        this.context = context;
        this.dataKostList = dataKostList;
    }

    @Override
    public OneFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_fragment_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OneFragmentAdapter.ViewHolder holder, int position) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");

        FirebaseRecyclerAdapter<DataKost,OneFragmentAdapter.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataKost, ViewHolder>(
                DataKost.class,
                R.layout.one_fragment_list,
                ViewHolder.class,
                db
        ) {

            @Override
            protected void populateViewHolder(ViewHolder viewHolder, DataKost model, int position) {
                final String post_key = getRef(position).getKey();
                DataKost dataKost = dataKostList.get(position);
                viewHolder.textNamaKos.setText(dataKost.getNamaKos());
                viewHolder.textJenisKos.setText(dataKost.getJenisKos());
                viewHolder.textHarga.setText(String.valueOf(dataKost.getHargaPerBulan()));

                //Loading image from Glide library.
                Glide.with(context).load(dataKost.getImageURL()).into(holder.imageView);

//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(context,post_key,Toast.LENGTH_SHORT).show();
//
//                        Intent deskripsiIntent = new Intent(context, DeskripsiActivity.class);
//                    }
//                });
            }
        };
        DataKost dataKost = dataKostList.get(position);
        holder.textNamaKos.setText(dataKost.getNamaKos());
        holder.textJenisKos.setText(dataKost.getJenisKos());
        holder.textHarga.setText(String.valueOf(dataKost.getHargaPerBulan()));

        //Loading image from Glide library.
        Glide.with(context).load(dataKost.getImageURL()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataKostList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textNamaKos, textJenisKos, textHarga;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            textNamaKos = (TextView) itemView.findViewById(R.id.textNamaKos);
            textJenisKos = (TextView) itemView.findViewById(R.id.textJenisKos);
            textHarga = (TextView) itemView.findViewById(R.id.textHarga);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            mView = itemView;

        }
    }
}

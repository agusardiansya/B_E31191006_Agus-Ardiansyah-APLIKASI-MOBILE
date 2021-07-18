package com.polije.sipakost.aplikasikos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * create an instance of this fragment.
 */
public class OneFragment extends Fragment{
    DatabaseReference db;
    RecyclerView mrvKos;
    OneFragmentAdapter mAdapter;
    private List<DataKost> dkList = new ArrayList<>();
    ProgressDialog progress;
    String idKos;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;

    public OneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OneFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static OneFragment newInstance(String param1, String param2) {
//        OneFragment fragment = new OneFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_one, container, false);
        View rootView =  inflater.inflate(R.layout.fragment_one, container, false);

        mrvKos = (RecyclerView) rootView.findViewById(R.id.rvKos);
        mrvKos.setHasFixedSize(true);
        mrvKos.setLayoutManager(new LinearLayoutManager(getContext()));
        mrvKos.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        // Assign activity this to progress dialog.
        progress = new ProgressDialog(getContext());

        // Setting up message in Progress dialog.
        progress.setMessage("Memuat Data Kost");

        // Showing progress dialog.
        progress.show();

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("dataKosts").child("detail");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DataKost dk = postSnapshot.getValue(DataKost.class);
//                    DataKost dk = new DataKost();
//                   String namaKos = dk.setNamaKos(dataSnapshot.child("detail").getValue(DataKost.class).getNamaKos().toString());
//                    String jenisKos = dk.setJenisKos(dataSnapshot.child("detail").getValue(DataKost.class).getJenisKos().toString());
//                    String image = dk.setImageURL(dataSnapshot.child("detail").getValue(DataKost.class).getImageURL());
//                    int harga = dk.setHargaPerBulan(Integer.valueOf(dataSnapshot.child("detail").getValue(DataKost.class).getHargaPerBulan().toString()));
//
                    dkList.add(dk);
                }

                FirebaseRecyclerAdapter<DataKost,OneFragmentAdapter.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataKost, OneFragmentAdapter.ViewHolder>(
                        DataKost.class,
                        R.layout.one_fragment_list,
                        OneFragmentAdapter.ViewHolder.class,
                        db
                ) {

                    @Override
                    protected void populateViewHolder(OneFragmentAdapter.ViewHolder viewHolder, DataKost model, int position) {
                        final String post_key = getRef(position).getKey();
                        DataKost dataKost = dkList.get(position);
                        viewHolder.textNamaKos.setText(dataKost.getNamaKos());
                        viewHolder.textHarga.setText(String.valueOf(dataKost.getHargaPerBulan())+ " /Bulan");
                        viewHolder.textJenisKos.setText(dataKost.getJenisKos());

                        Glide.with(OneFragment.this).load(dataKost.getImageURL()).into(viewHolder.imageView);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Toast.makeText(getContext(),post_key,Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getContext(), DetailKosActivity.class);
                                i.putExtra("idKos",post_key);
                                startActivity(i);
                            }
                        });
                    }
                };

                mAdapter = new OneFragmentAdapter(getContext(), dkList);

                mrvKos.setAdapter(firebaseRecyclerAdapter);

                // Hiding the progress dialog.
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progress.dismiss();
            }
        });



//        mAdapter = new FirebaseRecyclerAdapter<DataKost, DataKostViewHolder>(DataKost.class,
//                R.layout.one_fragment_list,
//                DataKostViewHolder.class,
//                mRef) {
//
//            @Override
//            public void populateViewHolder(DataKostViewHolder dk, DataKost dataKost, int i) {
//                dk.mNamaKos.setText(dataKost.getNamaKos());
//                dk.mJenisKos.setText(dataKost.getJenisKos());
//                dk.mHarga.setText(dataKost.getHargaPerBulan());
//
//            }
//        };

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}

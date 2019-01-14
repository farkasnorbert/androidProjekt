package farkasnorbert.sapientia.ms.androidprojekt.Other;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import farkasnorbert.sapientia.ms.androidprojekt.Activitys.AdViewActivity;
import farkasnorbert.sapientia.ms.androidprojekt.Activitys.LoginActivity;
import farkasnorbert.sapientia.ms.androidprojekt.Activitys.MainActivity;
import farkasnorbert.sapientia.ms.androidprojekt.Activitys.RegisterActivity;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WordViewHolder> {
    private ArrayList<Ad> mWordList;
    private LayoutInflater mInflater;

    public RecyclerViewAdapter(Context context,
                               ArrayList<Ad> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View mItemView = mInflater.inflate(R.layout.recycler_item,
                parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.WordViewHolder holder, final int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final Ad mCurrent = mWordList.get(position);
        holder.Ad.setOnClickListener(v -> {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mCurrent.addvNumber();
            mDatabase.child("data").child(mCurrent.getPhone()+"_"+mCurrent.getTitle()).child("vNumber").setValue(mCurrent.getvNumber());
            Intent i = new Intent(v.getContext(), AdViewActivity.class);
            i.putExtra("Ad", mCurrent);
            v.getContext().startActivity(i);
        });
        StorageReference ref = storage.getReference().child(mCurrent.getImg(0));
        holder.wordItemView.setText(mCurrent.getTitle());
        holder.description.setText(mCurrent.getSdesc());
        Glide.with(holder.mImage.getContext()).load(ref).into(holder.mImage);
        holder.vNumber.setText(mCurrent.getvNumber());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String p = dataSnapshot.child(mCurrent.getPhone()).child("pPicture").getValue(String.class);
                    if(!p.equals("")) {
                        StorageReference ref2 = storage.getReference().child(p);
                        Glide.with(holder.pPicture.getContext()).load(ref2).apply(RequestOptions.circleCropTransform().override(200,200)).into(holder.pPicture);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        public final TextView description;
        public final ImageView mImage;
        public final ImageView pPicture;
        public final TextView vNumber;
        final RecyclerViewAdapter mAdapter;
        ConstraintLayout Ad;

        public WordViewHolder(View itemView, RecyclerViewAdapter adapter) {
            super(itemView);

            wordItemView = itemView.findViewById(R.id.post_title);
            description = itemView.findViewById(R.id.post_desc);
            mImage = itemView.findViewById(R.id.post_image);
            vNumber = itemView.findViewById(R.id.vNumber);
            pPicture = itemView.findViewById(R.id.pPicture);
            Ad = itemView.findViewById(R.id.Ad);
            this.mAdapter = adapter;
        }
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

}
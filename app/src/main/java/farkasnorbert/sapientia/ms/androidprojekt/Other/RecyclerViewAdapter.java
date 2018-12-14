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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import farkasnorbert.sapientia.ms.androidprojekt.Activitys.AdViewActivity;
import farkasnorbert.sapientia.ms.androidprojekt.Modell.Ad;
import farkasnorbert.sapientia.ms.androidprojekt.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WordViewHolder> {
    private ArrayList<Ad> mWordList;
    private LayoutInflater mInflater;

    //private View.OnClickListener mOnClickListener;
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
        //mItemView.setOnClickListener(mOnClickListener);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.WordViewHolder holder, final int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final Ad mCurrent = mWordList.get(position);
        holder.Ad.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), AdViewActivity.class);
            i.putExtra("Ad", mCurrent);
            v.getContext().startActivity(i);
        });
        /*mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AdViewActivity.class);
                i.putExtra("Ad", mCurrent);
                v.getContext().startActivity(i);
            }
        };*/
        StorageReference ref = storage.getReference().child(mCurrent.getImg(0));
        holder.wordItemView.setText(mCurrent.getTitle());
        holder.description.setText(mCurrent.getSdesc());
        Glide.with(holder.mImage.getContext()).load(ref).into(holder.mImage);

    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        public final TextView description;
        public final ImageView mImage;
        final RecyclerViewAdapter mAdapter;
        ConstraintLayout Ad;

        public WordViewHolder(View itemView, RecyclerViewAdapter adapter) {
            super(itemView);

            wordItemView = itemView.findViewById(R.id.post_title);
            description = itemView.findViewById(R.id.post_desc);
            mImage = itemView.findViewById(R.id.post_image);
            Ad = itemView.findViewById(R.id.Ad);
            this.mAdapter = adapter;
        }
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

}
package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WordViewHolder> {
    private ArrayList<Ad> mWordList;
    private LayoutInflater mInflater;
    public RecyclerViewAdapter(Context context,
                               ArrayList<Ad> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        public final TextView description;
        public final ImageView mImage;
        final RecyclerViewAdapter mAdapter;
        public WordViewHolder(View itemView, RecyclerViewAdapter adapter) {
            super(itemView);

            wordItemView = itemView.findViewById(R.id.post_title);
            description=itemView.findViewById(R.id.post_desc);
            mImage = itemView.findViewById(R.id.post_image);

            this.mAdapter = adapter;
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.recycler_item,
                parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.WordViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Ad mCurrent = mWordList.get(position);
        StorageReference ref = storage.getReference().child(mCurrent.getImg(0));

        holder.wordItemView.setText(mCurrent.getTitle());
        holder.description.setText(mCurrent.getSdesc());
        Glide.with(holder.mImage.getContext()).load(ref).into(holder.mImage);





    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }



}

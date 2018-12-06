package farkasnorbert.sapientia.ms.androidprojekt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class AdViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public AdViewHolder(@NonNull View itemView, View mView) {
        super(itemView);
        this.mView = mView;
    }

    public void setTitle(String title){
        TextView post_title=(TextView)mView.findViewById(R.id.post_title);
        post_title.setText(title);
    }
    public void setDescription(String description){
        TextView post_description=(TextView)mView.findViewById(R.id.post_desc);
        post_description.setText(description);
    }
    public void setImage(Context ctx, String image){
        ImageView post_image=(ImageView)mView.findViewById(R.id.post_image);

    }


}
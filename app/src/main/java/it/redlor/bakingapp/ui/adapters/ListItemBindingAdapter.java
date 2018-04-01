package it.redlor.bakingapp.ui.adapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.redlor.bakingapp.R;

public class ListItemBindingAdapter {

    @BindingAdapter({"image"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        Uri uri = Uri.parse(imageUrl).buildUpon().build();

        if (imageUrl == null) {
            Picasso.with(imageView.getContext())
                    .load(R.drawable.wifi)
                    .into(imageView);
        } else {
            Picasso.with(imageView.getContext())
                    .load(uri.toString())
                    .into(imageView);
        }
    }
}

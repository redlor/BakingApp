package it.redlor.bakingapp.ui.adapters;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.redlor.bakingapp.R;

/**
 * Adapter to bind the image
 */

public class ListItemBindingAdapter {

    @BindingAdapter({"image"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        Uri uri = Uri.parse(imageUrl).buildUpon().build();

        if (imageUrl == null | imageUrl.equals("")) {
            Picasso.with(imageView.getContext())
                    .load(R.drawable.potoffood)
                    .into(imageView);
        } else {
            try {
                Picasso.with(imageView.getContext())
                        .load(uri.toString())
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

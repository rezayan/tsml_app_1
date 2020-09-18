package ir.topcoders.pol.utils.binding_adapters;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ir.topcoders.pol.utils.AppConstants;

public class LoadImageBindingAdapter {

    @BindingAdapter(value = {"android:src", "bind:placeholder", "bind:roadId", "bind:bridgeId", "bind:damageId"}, requireAll = false)
    public static void loadImage(ImageView imageView, String fileName, int placeHolder, String roadId, String bridgeId, String damageId) {
        if (fileName != null) {
            RequestOptions requestOptions = new RequestOptions();
            if (placeHolder != 0) {
                requestOptions = requestOptions.placeholder(placeHolder);
            }
            String path;
            if (!TextUtils.isEmpty(roadId)) {
                path = AppConstants.getFilesStorageFolder(imageView.getContext());
                path += roadId;

                if (!TextUtils.isEmpty(bridgeId)) {
                    path += "/" + bridgeId;
                    if (!TextUtils.isEmpty(damageId)) {
                        path += "/" + damageId;
                    }
                }
                path += "/" + fileName;
            } else
                path = fileName;

            Glide.with(imageView.getContext())
                    .load(path)
                    .apply(requestOptions)
                    .into(imageView);
        } else if (placeHolder != 0) {
            Glide.with(imageView.getContext())
                    .load(placeHolder)
                    .into(imageView);
        }
    }
}
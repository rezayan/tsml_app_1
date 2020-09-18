package ir.topcoders.pol.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import ir.topcoders.pol.R;

public class ImageUtils {

    private static final int BASE_GALLERY_REQUEST_CODE = 10000;
    private static final int BASE_CAMERA_REQUEST_CODE = 20000;
    public static final int MAX_IMAGE_SIZE = 1000;

    public static void showImagePicker(Activity activity, int requestCode) {
        final String items[] = new String[]{activity.getString(R.string.title_from_gallery), activity.getString(R.string.title_from_camera)};

        new AlertDialog.Builder(activity)
                .setTitle(R.string.title_select)
                .setItems(items, (d, choice) -> {
                    if (choice == 0)
                        launchGallery(activity, requestCode);
                    else if (choice == 1)
                        launchCamera(activity, requestCode);
                }).setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel()).show();
    }

    public static void launchGallery(Activity activity, int requestCode) {
        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        }

        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.title_select_image_from_gallery)),
                BASE_GALLERY_REQUEST_CODE + requestCode);
    }

    public static void launchCamera(Activity activity, int requestCode) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, BASE_CAMERA_REQUEST_CODE + requestCode);
    }

    public static String getSelectedImage(Context context, int requestCode, int resultCode, Intent data, String tempStorageFolder) {
        try {
            if (requestCode >= BASE_CAMERA_REQUEST_CODE) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap thumbnail = (Bitmap) extras.get("data");
                        if (thumbnail != null) {
                            File temp = new File(tempStorageFolder, UUID.randomUUID().toString());
                            writeToFile(thumbnail, temp);
                            return temp.getAbsolutePath();
                        }
                    }
                }
            } else if (requestCode >= BASE_GALLERY_REQUEST_CODE) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    Bitmap ResultBitmap = createBitmap(context, selectedImage);
                    ResultBitmap = resizeBitmap(ResultBitmap, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);

                    if (ResultBitmap != null) {
                        File temp = new File(tempStorageFolder, UUID.randomUUID().toString());
                        writeToFile(ResultBitmap, temp);
                        return temp.getAbsolutePath();
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeToFile(Bitmap image, File temp) throws Exception {
        temp.getParentFile().mkdirs();
        temp.createNewFile();
        FileOutputStream out = new FileOutputStream(temp);
        image.compress(Bitmap.CompressFormat.JPEG, 85, out);
    }

    public static Bitmap createBitmap(Context context, Uri imageUri) throws FileNotFoundException {
        String imagePath = getPath(context, imageUri);
        boolean useFileDecoding = (imagePath != null);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 4;
        bmOptions.inPurgeable = true;

        Bitmap result;

        if (useFileDecoding) {
            result = BitmapFactory.decodeFile(imagePath, bmOptions);
            result = modifyOrientation(result, imagePath);
        } else {
            InputStream is = context.getContentResolver().openInputStream(imageUri);
            result = BitmapFactory.decodeStream(is, null, bmOptions);
            result = modifyOrientation(result, imageUri.toString());
        }

        return result;
    }

    public static Bitmap resizeBitmap(Bitmap image, int maxWidth, int maxHeight) {
        try {
            if (image == null)
                return null;
            if (maxHeight > 0 && maxWidth > 0) {
                int width = image.getWidth();
                int height = image.getHeight();
                float ratioBitmap = (float) width / (float) height;
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;
                if (ratioMax > 1) {
                    finalWidth = (int) ((float) maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float) maxWidth / ratioBitmap);
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
                return image;
            } else {
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor;
        if (Build.VERSION.SDK_INT > 19) {
            try {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(uri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, sel, new String[]{id}, null);
            } catch (Exception ignored) {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
            }
        } else {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
        }
        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        } catch (NullPointerException e) {

        }
        return path;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) {
        try {
            ExifInterface ei = new ExifInterface(image_absolute_path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotate(bitmap, 90);

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotate(bitmap, 180);

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotate(bitmap, 270);

                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    return flip(bitmap, true, false);

                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return flip(bitmap, false, true);

                default:
                    return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static int getActualRequestCode(int requestCode) {
        if (requestCode >= BASE_CAMERA_REQUEST_CODE)
            return requestCode - BASE_CAMERA_REQUEST_CODE;
        else if (requestCode >= BASE_GALLERY_REQUEST_CODE)
            return requestCode - BASE_GALLERY_REQUEST_CODE;
        return requestCode;
    }

    public static void openImageByDefaultApp(Context context, File filePath) {
        if (filePath != null && filePath.exists()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(filePath), "image/*");
            context.startActivity(intent);
        }
    }
}

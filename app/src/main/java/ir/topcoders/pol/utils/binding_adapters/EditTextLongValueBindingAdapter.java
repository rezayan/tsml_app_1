package ir.topcoders.pol.utils.binding_adapters;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.EditText;

public class EditTextLongValueBindingAdapter {

    @BindingAdapter("android:text")
    public static void setText(EditText view, long value) {
        view.setText(String.valueOf(value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static long getText(EditText view) {
        try {
            if (view.getText().toString().length() > 0) {
                String text = view.getText().toString();
                return Long.parseLong(text);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
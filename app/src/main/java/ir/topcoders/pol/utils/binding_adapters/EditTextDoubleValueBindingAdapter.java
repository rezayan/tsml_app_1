package ir.topcoders.pol.utils.binding_adapters;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.EditText;

public class EditTextDoubleValueBindingAdapter {

    @BindingAdapter("android:text")
    public static void setText(EditText view, double value) {
        view.setText(String.valueOf(value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getText(EditText view) {
        try {
            if (view.getText().toString().length() > 0) {
                String text = view.getText().toString();
                if (text.endsWith("."))
                    text = text.replace(".", "");
                return Double.parseDouble(text);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
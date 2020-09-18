package ir.topcoders.pol.utils.binding_adapters;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

@InverseBindingMethods(
        @InverseBindingMethod(type = AdapterView.class,
                attribute = "android:selectedValue",
                method = "getSelectedItem"
        ))
public class SelectedValueBindingAdapter {
    @BindingAdapter("android:selectedValueAttrChanged")
    public static void setSelectedValueListener(AdapterView view,
                                                final InverseBindingListener attrChanged) {
        if (attrChanged == null) {
            view.setOnItemSelectedListener(null);
        } else {
            view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attrChanged.onChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    attrChanged.onChange();
                }
            });
        }
    }

    @BindingAdapter("android:selectedValue")
    public static void setSelectedValue(AdapterView<?> view, Object selectedValue) {
        Adapter adapter = view.getAdapter();
        if (adapter == null) {
            return;
        }
        // I haven't tried this, but maybe setting invalid position will
        // clear the selection?
        int position = AdapterView.INVALID_POSITION;

        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(selectedValue.toString())) {
                position = i;
                break;
            }
        }
        view.setSelection(position);
    }

}
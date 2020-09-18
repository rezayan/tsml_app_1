package ir.topcoders.pol.view.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ir.topcoders.pol.R;
import ir.topcoders.pol.data.LocalSettings;
import ir.topcoders.pol.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    public static void showActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void onSaveClick(View view) {
        if (isValidData())
            saveSettings();
        else
            Toast.makeText(this, "اطلاعات را تکمیل کنید!", Toast.LENGTH_SHORT).show();
    }

    private void saveSettings() {
        LocalSettings settings = new LocalSettings(this);
        String sdcardPath = getSdCardPath();
        settings.putOperatorInfo(binding.nameEditText.getText().toString().trim()
                , binding.cellNumberEditText.getText().toString().trim());
        settings.putSdcardPath(sdcardPath);
        RoadListActivity.showActivity(this);
        finish();
    }

    private String getSdCardPath() {
        String path = null;

        File sdCardFile = null;
        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
        } else {
            sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }

        return sdCardFile.getAbsolutePath();
    }

    private boolean isValidData() {
        boolean validate = true;

        binding.nameEditText.setError(null);
        binding.cellNumberEditText.setError(null);

        if (TextUtils.isEmpty(binding.nameEditText.getText().toString().trim())) {
            binding.nameEditText.setError("تکمیل شود");
            validate = false;
        }

        if (TextUtils.isEmpty(binding.cellNumberEditText.getText().toString().trim())
                || binding.cellNumberEditText.getText().toString().trim().length() < 11) {
            binding.cellNumberEditText.setError("تکمیل شود");
            validate = false;
        }

        return validate;
    }
}

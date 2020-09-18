package ir.topcoders.pol.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalSettings {

    private final SharedPreferences mPref;

    public static final String PREF_FILE_NAME = "pref_file";
    private static final String PREF_KEY_OPERATOR_NAME = "PREF_KEY_OPERATOR_NAME";
    private static final String PREF_KEY_OPERATOR_CELL_NUMBER = "PREF_KEY_OPERATOR_CELL_NUMBER";
    private static final String PREF_KEY_SDCARD_PATH = "PREF_KEY_SDCARD_PATH";

    @Inject
    public LocalSettings(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void putOperatorInfo(String operatorName, String operatorCellNumber) {
        mPref.edit()
                .putString(PREF_KEY_OPERATOR_NAME, operatorName)
                .putString(PREF_KEY_OPERATOR_CELL_NUMBER, operatorCellNumber)
                .apply();
    }

    public void putSdcardPath(String sdcardPath) {
        mPref.edit()
                .putString(PREF_KEY_SDCARD_PATH, sdcardPath)
                .apply();
    }

    @Nullable
    public String getOperatorName() {
        return mPref.getString(PREF_KEY_OPERATOR_NAME, null);
    }

    @Nullable
    public String getOperatorCellNumber() {
        return mPref.getString(PREF_KEY_OPERATOR_CELL_NUMBER, null);
    }

    @Nullable
    public String getSdCardPath() {
        return mPref.getString(PREF_KEY_SDCARD_PATH, null);
    }
}
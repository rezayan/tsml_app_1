package ir.topcoders.pol.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import ir.topcoders.pol.data.LocalSettings;
import ir.topcoders.pol.data.model.Bridge;

public class AppConstants {
    public static final String DATABASE_NAME = "database.db";
    public static final String DATABASE_FOLDER = "database";
    public static final String BASE_FOLDER = "ROADS_APP";
    public static final String FILES_FOLDER = "Roads";
    public static final String TEMP_STORAGE_FOLDER = "TEMP";
    public static final String BACKUP_STORAGE_FOLDER = "BACKUPS";
    public static final int LOCATION_UPDATE_INTERVALS_IN_MILLIS = 1000;
    public static final int MAX_DISTANCE_CHANGE_TOLERANCE_IN_METERS = 0;

    public static String getBaseFolder(Context context) {
        String sdcard = new LocalSettings(context).getSdCardPath();
        return sdcard
                + "/" + AppConstants.BASE_FOLDER
                + "/";
    }

    public static String getDatabaseFolderPath(Context context) {
        String folder = getBaseFolder(context)
                + AppConstants.DATABASE_FOLDER;
        new File(folder).mkdirs();
        return folder;
    }

    public static String getDatabaseFilePath(Context context) {
        String folder = getDatabaseFolderPath(context);
        return folder + "/" + AppConstants.DATABASE_NAME;
    }

    public static String getFilesStorageFolder(Context context) {
        return getBaseFolder(context)
                + AppConstants.FILES_FOLDER
                + "/";
    }

    public static String getFilesStorageFolderByBridge(Context context, Bridge bridge, String fileName) {
        return getFilesStorageFolder(context)
                + bridge.roadId
                + "/" + bridge.bridgeId
                + "/" + fileName;
    }

    public static String getTempStorageFolder(Context context) {
        return getBaseFolder(context)
                + AppConstants.TEMP_STORAGE_FOLDER
                + "/";
    }

    public static String getBackupStorageFolder(Context context) {
        return getBaseFolder(context)
                + AppConstants.BACKUP_STORAGE_FOLDER
                + "/";
    }
}

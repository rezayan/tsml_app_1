package ir.topcoders.pol.utils;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ir.topcoders.pol.data.model.Road;
import ir.topcoders.pol.data.source.RoadRepository;
import ir.topcoders.pol.data.source.local.MyDatabase;

public class BackupUtils {

    public static File createBackup(Context context, MyDatabase myDatabase, Road selectedRoad) {
        ArrayList<String> selectedRoadIds = new ArrayList<>();
        selectedRoadIds.add(selectedRoad.roadId);
        String filePrefixName = selectedRoad.startName + "__" + selectedRoad.stopName + "__";
        return createBackup(context, myDatabase, selectedRoadIds, filePrefixName);
    }

    public static File createBackup(Context context, MyDatabase myDatabase, ArrayList<String> selectedRoadIds) {
        return createBackup(context, myDatabase, selectedRoadIds, "");
    }

    public static File createBackup(Context context, MyDatabase sourceDatabase, ArrayList<String> selectedRoadIds, @NonNull String filePrefixName) {
        String currentDate = Utils.getPersianDateAndTime("-");
        String databaseFolderPath = AppConstants.getDatabaseFolderPath(context);
        String clonedDatabaseFolderPath = databaseFolderPath + "-BACKUP";
        String targetZipFile = AppConstants.getBackupStorageFolder(context) + filePrefixName + currentDate + ".zip";
        MyDatabase clonedDatabase = null;
        ZipOutputStream out = null;

        try {
            checkpointOnDatabase(sourceDatabase);
            clonedDatabase = cloneDatabase(context, databaseFolderPath, clonedDatabaseFolderPath);
            removeUnselectedRoads(clonedDatabase, selectedRoadIds);
            out = createZipStream(targetZipFile);
            addSelectedRoadsToZipFile(context, out, selectedRoadIds);
            addDatabaseToZipFile(context, out, clonedDatabaseFolderPath);
            releaseAll(clonedDatabase, clonedDatabaseFolderPath);
            return new File(targetZipFile);
        } catch (Exception e) {
            e.printStackTrace();
            rollbackProcedure(clonedDatabase, clonedDatabaseFolderPath, targetZipFile);
            return null;
        } finally {
            closeZipStream(out);
        }
    }

    private static void checkpointOnDatabase(MyDatabase sourceDatabase) throws InterruptedException {
        sourceDatabase.rawDao().rawQuery(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
        Thread.sleep(2000);
    }

    private static MyDatabase cloneDatabase(Context context, String originalDatabaseFolderPath, String clonedDatabaseFolderPath) throws IOException, InterruptedException {
        FileUtils.deleteFolder(clonedDatabaseFolderPath);
        Thread.sleep(2000);
        new File(clonedDatabaseFolderPath).mkdirs();
        FileUtils.copyFolder(originalDatabaseFolderPath, clonedDatabaseFolderPath);
        Thread.sleep(1000);
        String clonedDatabasePath = clonedDatabaseFolderPath + "/" + AppConstants.DATABASE_NAME;
        return Room.databaseBuilder(context, MyDatabase.class, clonedDatabasePath).build();
    }

    private static void removeUnselectedRoads(MyDatabase clonedDatabase, ArrayList<String> selectedRoadIds) throws InterruptedException {
        RoadRepository roadRepository = new RoadRepository(clonedDatabase.roadDao());
        List<Road> removeList = createRemoveList(roadRepository, selectedRoadIds);
        if (removeList != null)
            for (Road road : removeList)
                roadRepository.removeRoad(road.roadId);

        clonedDatabase.rawDao().rawQuery(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
        Thread.sleep(1000);
    }

    private static List<Road> createRemoveList(RoadRepository roadRepository, ArrayList<String> selectedRoadIds) {
        List<Road> allRoads = roadRepository.getRoadsSync();
        List<Road> removeList = new ArrayList<>();
        for (Road road : allRoads) {
            boolean exist = false;
            for (String id : selectedRoadIds) {
                if (road.roadId.equalsIgnoreCase(id)) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                removeList.add(road);
        }
        return removeList;
    }

    private static ZipOutputStream createZipStream(String targetZipFile) throws IOException {
        File zipFile = new File(targetZipFile);
        zipFile.getParentFile().mkdirs();
        zipFile.createNewFile();
        return new ZipOutputStream(new FileOutputStream(zipFile));
    }

    private static void addSelectedRoadsToZipFile(Context context, ZipOutputStream out, ArrayList<String> selectedRoadIds) throws IOException {
        File baseDir = new File(AppConstants.getBaseFolder(context));
        for (String roadId : selectedRoadIds) {
            File sourceFolder = new File(baseDir.getAbsolutePath() + "/" + AppConstants.FILES_FOLDER, roadId);
            zipSubFolder(out, sourceFolder, baseDir.getPath().length());
        }
    }

    private static void addDatabaseToZipFile(Context context, ZipOutputStream out, String databaseFolderPath) throws IOException {
        File baseDir = new File(AppConstants.getBaseFolder(context));
        zipSubFolder(out, new File(databaseFolderPath), baseDir.getPath().length());
    }

    private static void releaseAll(MyDatabase clonedDatabase, String clonedDatabaseFolder) throws InterruptedException {
        if (clonedDatabase != null)
            clonedDatabase.close();
        Thread.sleep(1000);
        FileUtils.deleteFolder(clonedDatabaseFolder);
        Thread.sleep(1000);
    }

    private static void rollbackProcedure(MyDatabase clonedDatabase, String clonedDatabaseFolderPath, String targetZipFile) {
        try {
            if (clonedDatabase != null)
                clonedDatabase.close();
            Thread.sleep(1000);
            FileUtils.deleteFolder(clonedDatabaseFolderPath);
            FileUtils.deleteFile(targetZipFile);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void closeZipStream(@Nullable ZipOutputStream out) {
        try {
            if (out != null)
                out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {
        File[] fileList = folder.listFiles();
        if (fileList != null)
            for (File file : fileList) {
                if (file.isDirectory())
                    zipSubFolder(out, file, basePathLength);
                else
                    zipFile(out, file, basePathLength);
            }
    }

    private static void zipFile(ZipOutputStream out, File file, int basePathLength) throws IOException {
        final int BUFFER = 2048;
        byte data[] = new byte[BUFFER];

        String unmodifiedFilePath = file.getPath();
        String relativePath = unmodifiedFilePath.substring(basePathLength + 1);

        FileInputStream fi = new FileInputStream(unmodifiedFilePath);
        BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);

        ZipEntry entry = new ZipEntry(relativePath);
        entry.setTime(file.lastModified()); // to keep modification time after unzipping
        out.putNextEntry(entry);

        int count;
        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
        out.closeEntry();
    }
}

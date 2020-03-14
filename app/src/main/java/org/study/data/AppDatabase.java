package org.study.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.study.data.dao.ProgramCategoryDao;
import org.study.data.dao.ProgramDetailDao;
import org.study.data.dao.ProgramInformationDao;
import org.study.data.entity.ProgramCategoryEntity;
import org.study.data.entity.ProgramDetailEntity;
import org.study.data.entity.ProgramInformationEntity;
import org.study.utilities.Constants;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {ProgramCategoryEntity.class, ProgramInformationEntity.class, ProgramDetailEntity.class},
        version = 17,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private final static String DB_NAME = "school-db";

    private static AppDatabase sInstance;

    private static final Object sLock = new Object();

    public abstract ProgramCategoryDao programCategoryDao();

    public abstract ProgramInformationDao programInformationDao();

    public abstract ProgramDetailDao programDetailDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public static AppDatabase buildDatabase(final Context appContext) {
        if (sInstance == null) {
            Log.i(Constants.tag_data, "Build Database.");

            sInstance = Room.databaseBuilder(appContext.getApplicationContext(),
                    AppDatabase.class, DB_NAME)
                    .addCallback(new Callback() {

                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // pre-populate tables
                            prePopulateTables("Populate tables onCreate.");
                        }

                        @Override
                        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                            super.onDestructiveMigration(db);
                            // pre-populate tables
                            prePopulateTables("Populate tables onDestructiveMigration.");
                        }

                        public void prePopulateTables(String message) {
                            Log.i(Constants.tag_data, message);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                addDelay();

                                AppDatabase database = AppDatabase.getInstance(appContext);
                                try {
                                    List<ProgramCategoryEntity> programCategories = DataGenerator.generateProgramCategory(appContext);
                                    Log.i(Constants.tag_data, "Generate ProgramCategory Data.");

                                    List<ProgramInformationEntity> programInformations = DataGenerator.generateProgramInformation(appContext);
                                    Log.i(Constants.tag_data, "Generate ProgramInformation Data.");

                                    List<ProgramDetailEntity> programDetails = DataGenerator.generateProgramDetail(appContext);
                                    Log.i(Constants.tag_data, "Generate ProgramDetail Data.");

                                    insertData(database, programCategories, programInformations, programDetails);
                                    Log.i(Constants.tag_data, "Pre-populate tables completed.");

                                    database.setDatabaseCreated();
                                } catch (Exception e) {
                                    Log.e(Constants.tag_data, "Error in Pre-populate tables process.", e);
                                }
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DB_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
            ignored.printStackTrace();
        }
    }

    private static void insertData(final AppDatabase database,
                                   final List<ProgramCategoryEntity> programCategories,
                                   final List<ProgramInformationEntity> programInformations,
                                   final List<ProgramDetailEntity> programDetails) {
        database.runInTransaction(() -> {
            database.programCategoryDao().insertAll(programCategories);
            database.programInformationDao().insertAll(programInformations);
            database.programDetailDao().insertAll(programDetails);
        });
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }
}

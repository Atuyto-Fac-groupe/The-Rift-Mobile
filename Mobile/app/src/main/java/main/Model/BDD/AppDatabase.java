package main.Model.BDD;

import main.Model.Histoire;
import main.Model.Notes;

import android.content.Context;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.Executors;

@Database(entities = {Notes.class, Histoire.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract NotesDao notesDao();
    public abstract HistoireDao histoireDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "rift_database"
            )
                    .fallbackToDestructiveMigration()
                    .setQueryExecutor(Executors.newSingleThreadExecutor())
                    .build();
        }
        return INSTANCE;
    }
}
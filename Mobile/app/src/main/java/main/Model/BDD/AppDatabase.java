package main.Model.BDD;

import main.Model.GridCell;
import main.Model.GridMap;
import main.Model.Notes;

import android.content.Context;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import main.Model.Router;

import java.util.concurrent.Executors;

@Database(entities = {Notes.class, Router.class, GridMap.class, GridCell.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract NotesDao notesDao();
    public abstract RouterDao routerDao();
    public abstract GridMapDao gridMapDao();
    public abstract GridCellDao gridCellDao();



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
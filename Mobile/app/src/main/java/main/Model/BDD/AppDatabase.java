package main.Model.BDD;

import main.Model.BDD.Cartography.*;
import main.Model.cartography.Calibration;
import main.Model.cartography.GridCell;
import main.Model.cartography.GridMap;
import main.Model.Notes;

import android.content.Context;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import main.Model.cartography.Router;

import java.util.concurrent.Executors;

@Database(entities = {Notes.class, Router.class, GridMap.class, GridCell.class, Calibration.class, CalibrationRouterCrossRef.class, GridCellCrossRef.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract NotesDao notesDao();
    public abstract RouterDao routerDao();
    public abstract GridMapDao gridMapDao();
    public abstract GridCellDao gridCellDao();
    public abstract CalibrationDao calibrationDao();



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
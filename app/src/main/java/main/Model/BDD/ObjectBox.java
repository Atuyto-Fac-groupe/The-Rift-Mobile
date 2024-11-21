package main.Model.BDD;

import android.content.Context;
import android.util.Log;
import com.example.therift.BuildConfig;
import io.objectbox.BoxStore;
import io.objectbox.android.Admin;
import main.Model.MyObjectBox;

public class ObjectBox {
    private static BoxStore store;

    public static void init(Context context) {
        store = MyObjectBox.builder()
                .androidContext(context)
                .build();
        if (BuildConfig.DEBUG) {
            boolean started = new Admin(store).start(context);
            Log.i("ObjectBoxAdmin", "Started: " + started);
        }
    }

    public static BoxStore get() {
        return store;
    }
}
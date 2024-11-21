package main.Controler;

import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.therift.R;
import com.google.android.material.tabs.TabLayout;
import main.View.FragmentHistoire;
import main.View.FragmentMessage;
import main.View.FragmentNote;

public class TableControler implements TabLayout.OnTabSelectedListener {


    private final FrameLayout fragment;
    private final AppCompatActivity activity;
    public TableControler(FrameLayout fragment, AppCompatActivity activity) {
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();

        switch (position) {
            case 0:
                this.activity.getSupportFragmentManager().beginTransaction()
                        .replace(fragment.getId(), new FragmentHistoire())
                        .addToBackStack(fragment.getTransitionName())
                        .commit();
                break;
            case 1:
                this.activity.getSupportFragmentManager().beginTransaction()
                        .replace(fragment.getId(), new FragmentNote())
                        .addToBackStack(fragment.getTransitionName())
                        .commit();
                break;
            case 2:
                this.activity.getSupportFragmentManager().beginTransaction()
                        .replace(fragment.getId(), new FragmentMessage())
                        .addToBackStack(fragment.getTransitionName())
                        .commit();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

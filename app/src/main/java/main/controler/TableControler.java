package main.controler;

import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;
import main.view.FragmentHistoire;
import main.view.FragmentMessage;
import main.view.FragmentNote;

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
            default:
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

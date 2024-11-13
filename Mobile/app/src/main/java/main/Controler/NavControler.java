package main.Controler;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.therift.R;
import com.example.therift.databinding.MainActivityBinding;
import main.View.FragmentHistoire;
import main.View.FragmentMessage;
import main.View.FragmentNote;
import main.View.MainActivity;

public class NavControler implements View.OnClickListener{

    private MainActivityBinding binding;
    private FrameLayout fragment;
    private AppCompatActivity activity;

    public NavControler(FrameLayout fragment, MainActivityBinding binding, AppCompatActivity activity) {
        this.binding = binding;
        this.fragment = fragment;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btNote.getId()){
            this.resetButton();
            this.binding.tvTitleWindow.setText("NOTE");
            this.binding.btNote.setBackground(this.activity.getDrawable(R.drawable.background_buton_navigation_ative));
            this.binding.btNote.setImageDrawable(this.activity.getDrawable(R.drawable.ic_note_white));
            this.activity.getSupportFragmentManager().beginTransaction()
                    .replace(fragment.getId(), new FragmentNote())
                    .addToBackStack(fragment.getTransitionName())
                    .commit();
        }
        if (v.getId() == binding.btHistoire.getId()){
            this.resetButton();
            this.binding.tvTitleWindow.setText("HISTOIRE");
            this.binding.btHistoire.setBackground(this.activity.getDrawable(R.drawable.background_buton_navigation_ative));
            this.binding.btHistoire.setImageDrawable(this.activity.getDrawable(R.drawable.ic_key_white));
            this.activity.getSupportFragmentManager().beginTransaction()
                    .replace(fragment.getId(), new FragmentHistoire())
                    .addToBackStack(fragment.getTransitionName())
                    .commit();
        }
        if (v.getId() == binding.btMessages.getId()){
            this.resetButton();
            this.binding.tvTitleWindow.setText("MESSAGES");
            this.binding.btMessages.setBackground(this.activity.getDrawable(R.drawable.background_buton_navigation_ative));
            this.binding.btMessages.setImageDrawable(this.activity.getDrawable(R.drawable.ic_message_white));
            this.activity.getSupportFragmentManager().beginTransaction()
                    .replace(fragment.getId(), new FragmentMessage())
                    .addToBackStack(fragment.getTransitionName())
                    .commit();
        }
    }
    private void resetButton(){
        this.binding.btNote.setBackground(this.activity.getDrawable(R.drawable.background_buton_navigation_inative));
        this.binding.btNote.setImageDrawable(this.activity.getDrawable(R.drawable.ic_note_purple));
        this.binding.btHistoire.setBackground(this.activity.getDrawable(R.drawable.background_buton_navigation_inative));
        this.binding.btHistoire.setImageDrawable(this.activity.getDrawable(R.drawable.ic_key_purple));
        this.binding.btMessages.setBackground(this.activity.getDrawable(R.drawable.background_buton_navigation_inative));
        this.binding.btMessages.setImageDrawable(this.activity.getDrawable(R.drawable.ic_message_purple));
    }
}

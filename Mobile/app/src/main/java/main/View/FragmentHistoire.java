package main.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.therift.R;
import com.example.therift.databinding.FragmentHistoireBinding;

import main.Model.BDD.AppDatabase;
import main.Model.Histoire;

import java.util.List;

public class FragmentHistoire extends Fragment {

    private FragmentHistoireBinding binding;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoireBinding.inflate(getLayoutInflater());
        db = AppDatabase.getInstance(this.getContext());

        deleteAllStoriesAndAddNew();

        new Thread(() -> {
            List<Histoire> displayedHistoires = db.histoireDao().getAllDisplayedHistoires();

            this.getActivity().runOnUiThread(() -> {
                for (Histoire histoire : displayedHistoires) {
                    updateHistoire(histoire); // Display each previously displayed histoire
                }
            });
        }).start();

        return binding.getRoot();
    }

    public void sendHistoire(int number) {
        new Thread(() -> {
            List<Histoire> histoires = db.histoireDao().getAllHistoires();

            if (number >= 0 && number < histoires.size()) {
                Histoire selectedHistoire = histoires.get(number);

                selectedHistoire.setDisplayed(true);
                db.histoireDao().updateHistoire(selectedHistoire);

                this.getActivity().runOnUiThread(() -> updateHistoire(selectedHistoire));
            } else {
                this.getActivity().runOnUiThread(() -> {
                    TextView errorMessage = new TextView(this.getContext());
                    errorMessage.setText("Histoire non trouvée.");
                    binding.liHistoires.addView(errorMessage);
                });
            }
        }).start();
    }

    private void updateHistoire(Histoire histoire) {
        LinearLayout parentLayout = this.binding.liHistoires;

        // Create a TextView for the histoire text
        TextView histoireTextView = new TextView(this.getContext());
        histoireTextView.setText(histoire.getHistoireText());
        histoireTextView.setTextSize(18);
        histoireTextView.setTextColor(Color.BLACK);

        // Create a TextView for the histoire tips
        TextView histoireTipsView = new TextView(this.getContext());
        histoireTipsView.setText(histoire.getHistoireTips());
        histoireTipsView.setTextSize(16);
        histoireTipsView.setTextColor(Color.GRAY);

        // Check if noButton is true. If so, show tips immediately, otherwise hide them initially
        if (histoire.getNoButton()) {
            histoireTipsView.setVisibility(View.VISIBLE); // Show tips if noButton is true
        } else {
            histoireTipsView.setVisibility(View.GONE); // Hide tips initially if noButton is false
        }

        // Layout parameters for histoire text
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textLayoutParams.setMargins(20, 20, 20, 10);
        histoireTextView.setLayoutParams(textLayoutParams);

        // Layout parameters for the tips
        LinearLayout.LayoutParams tipsLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tipsLayoutParams.setMargins(20, 0, 20, 20);
        histoireTipsView.setLayoutParams(tipsLayoutParams);

        // Add the TextView for the story to the parent layout first
        parentLayout.addView(histoireTextView);

        // Only create a button if noButton is false
        if (!histoire.getNoButton()) {
            // Create a button to show tips
            Button showTipsButton = new Button(this.getContext());
            showTipsButton.setText("Voir l'astuce");

            // Set the button's click listener to reveal the tips and remove the button
            showTipsButton.setOnClickListener(v -> {
                histoireTipsView.setVisibility(View.VISIBLE);
                parentLayout.removeView(showTipsButton); // Remove button after click
            });

            // Layout parameters for the button
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            buttonLayoutParams.setMargins(20, 0, 20, 20);
            showTipsButton.setLayoutParams(buttonLayoutParams);

            // Add the button to the layout after the story text
            parentLayout.addView(showTipsButton);
        }

        // Finally, add the TextView for the tips to the parent layout
        parentLayout.addView(histoireTipsView);
    }


    private void deleteAllStoriesAndAddNew() {
        new Thread(() -> {
            db.histoireDao().deleteAllHistoire();

            Histoire histoire0 = new Histoire();
            histoire0.setHistoireText("Votre histoire commence ici.");
            histoire0.setHistoireTips("");
            histoire0.setDisplayed(true);
            histoire0.setNoButton(true);

            db.histoireDao().insertHistoire(histoire0);

            Histoire histoire1 = new Histoire();
            histoire1.setHistoireText("Nouvelle histoire 1.");
            histoire1.setHistoireTips("Astuce : Trouvez l'indice.");
            histoire1.setDisplayed(false);
            histoire1.setNoButton(false);

            db.histoireDao().insertHistoire(histoire1);

            Histoire histoire2 = new Histoire();
            histoire2.setHistoireText("Nouvelle histoire 2.");
            histoire2.setHistoireTips("Astuce : Résolvez l'énigme.");
            histoire2.setDisplayed(false);
            histoire2.setNoButton(false);

            db.histoireDao().insertHistoire(histoire2);

        }).start();
    }
}

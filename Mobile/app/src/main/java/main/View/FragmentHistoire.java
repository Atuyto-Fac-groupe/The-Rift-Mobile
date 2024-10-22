package main.View;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.therift.R;
import com.example.therift.databinding.FragmentHistoireBinding;
import main.App;
import main.Controler.ColorChanger;
import main.Model.Stories;
import main.Model.SystemMessage;
import androidx.lifecycle.Observer;


import java.util.*;


public class FragmentHistoire extends Fragment {

    private FragmentHistoireBinding binding;
    private List<Stories> stories;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentHistoireBinding.inflate(getLayoutInflater());
        Stories.initStories(Objects.requireNonNull(this.getContext()).getResources());
        this.stories = Stories.getStories();




        App.systemMessages.observe(this, new Observer<List<SystemMessage>>() {

            @Override
            public void onChanged(List<SystemMessage> systemMessages) {
                updateStoriesByServer();
            }
        });

        return binding.getRoot();
    }

    private void updateStoriesByServer() {



        List<SystemMessage> systemMessages = App.systemMessages.getValue();
        this.binding.liHistoires.removeAllViews();
        this.stories.stream()
                .filter(story -> story.getOrder() == 1)
                .findFirst().ifPresent(this::updateHistoire);
        if(systemMessages == null || systemMessages.isEmpty()) {return;}
        for (SystemMessage systemMessage : systemMessages) {

            Map<String, Integer> codeToOrderMap = new HashMap<>();
            codeToOrderMap.put(getResources().getString(R.string.code_Enigma_1), 2);
            codeToOrderMap.put(getResources().getString(R.string.code_Enigma_2), 3);
            codeToOrderMap.put(getResources().getString(R.string.code_Enigma_3), 4);

            Integer orderToUpdate = codeToOrderMap.get(systemMessage.getCode());
            if (orderToUpdate != null) {
                this.stories.stream()
                        .filter(story -> story.getOrder() == orderToUpdate)
                        .findFirst()
                        .ifPresent(this::updateHistoire);
            }
        }

    }

    private void updateHistoire(Stories stories) {

        LinearLayout parentLayout = this.binding.liHistoires;
        Map<String, Object> sequences = this.textToMap(stories.getHistoireText());
        parentLayout.post(()-> {
            if (!sequences.isEmpty()) {
                Map.Entry<String, Object> firstEntry = sequences.entrySet().iterator().next();
                String firstKey = firstEntry.getKey();
                Object firstValue = firstEntry.getValue();
                if (firstKey.equals("picture")) {
                    parentLayout.addView(this.showImage((String) firstValue));
                } else if (firstKey.equals("sequential")) {
                    FrameLayout frameLayout = getFrameLayout((List<String>) firstValue);
                    parentLayout.addView(frameLayout);
                }
            }
            else{
                parentLayout.addView(this.setEnigmaText(stories));
                parentLayout.addView(this.setEnigmaTips(stories, parentLayout));
            }

            this.binding.idSvMain.post(() -> this.binding.idSvMain.fullScroll(View.FOCUS_DOWN));
        });




    }

    private FrameLayout getFrameLayout(List<String> colorsString) {
        List<Integer> colors = new ArrayList<>();
        Timer timer = new Timer();

        colorsString.forEach(s -> colors.add(this.getColorFromName(s)));
        FrameLayout frameLayout = new FrameLayout(this.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                this.binding.idSvMain.getHeight()
        );
        frameLayout.setLayoutParams(layoutParams);
        timer.schedule(new ColorChanger(frameLayout, colors, this.getActivity()), 1000, 1000);
        return frameLayout;
    }


    private TextView setEnigmaText(Stories stories) {
        TextView histoireTextView = new TextView(this.getContext());
        histoireTextView.setText(stories.getHistoireText());
        histoireTextView.setTextSize(18);
        histoireTextView.setTextColor(Color.BLACK);

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textLayoutParams.setMargins(20, 20, 20, 10);
        histoireTextView.setLayoutParams(textLayoutParams);

        return histoireTextView;
    }

    private TextView setEnigmaTips(Stories stories, LinearLayout parentLayout) {
        TextView histoireTipsView = new TextView(this.getContext());
        histoireTipsView.setText(stories.getHistoireTips());
        histoireTipsView.setTextSize(16);
        histoireTipsView.setTextColor(Color.GRAY);

        if (stories.getNoButton() && !Objects.equals(stories.getHistoireTips(), "")) {
            histoireTipsView.setVisibility(View.VISIBLE);
        } else {
            histoireTipsView.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams tipsLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tipsLayoutParams.setMargins(20, 0, 20, 20);
        histoireTipsView.setLayoutParams(tipsLayoutParams);

        if (!stories.getNoButton() && !Objects.equals(stories.getHistoireTips(), "")) {
            Button showTipsButton = new Button(this.getContext());
            showTipsButton.setText("Voir l'astuce");

            showTipsButton.setOnClickListener(v -> {
                histoireTipsView.setVisibility(View.VISIBLE);
                parentLayout.removeView(showTipsButton);
            });
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            buttonLayoutParams.setMargins(20, 0, 20, 20);
            showTipsButton.setLayoutParams(buttonLayoutParams);

        }
        return histoireTipsView;
    }

    private ImageView showImage(String path) {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(this.getResourceByName(path));
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        imageLayoutParams.setMargins(20, 20, 20, 20);
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @SuppressLint("DiscouragedApi")
    private Drawable getResourceByName(String resourceName) {
        int resourceId = getResources().getIdentifier(resourceName, "drawable", Objects.requireNonNull(getContext()).getPackageName());
        if (resourceId != 0) {
            return ContextCompat.getDrawable(getContext(), resourceId);
        }
        return null;
    }

    private Map<String, Object> textToMap(String sequence) {
        Map<String, Object> map = new HashMap<>();
        String[] elements = sequence.split("/");

        for (String element : elements) {
            if (element.contains(":")) {
                String[] parts = element.split(":", 2);
                String cle = parts[0].trim();
                String valeur = parts[1].trim();

                if (valeur.contains(",")) {
                    List<String> liste = Arrays.asList(valeur.split(","));
                    liste.replaceAll(String::trim);
                    map.put(cle, liste);
                } else {
                    map.put(cle, valeur);
                }
            }
        }

        return map;
    }

    private int getColorFromName(String colorName) {
        switch (colorName) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.GRAY;
            case "purple":
                return Color.MAGENTA;
            default:
                return Color.BLACK;
        }
    }
}

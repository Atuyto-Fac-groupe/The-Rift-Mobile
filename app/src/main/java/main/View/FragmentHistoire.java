package main.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.graphics.Color;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.therift.R;
import com.example.therift.databinding.FragmentHistoireBinding;
import com.example.therift.databinding.QrcodeAskingLayoutBinding;
import com.google.gson.Gson;
import main.App;
import main.Controler.ColorChanger;
import main.Controler.OnQrCodeScan;
import main.Model.Message;
import main.Model.Stories;
import main.Model.SystemMessage;
import androidx.lifecycle.Observer;


import java.util.*;

import static android.app.Activity.RESULT_OK;

/**
 * FragmentHistoire est un fragment qui gère l'affichage des histoires et le scan des QR codes.
 * Il observe les messages du système et met à jour l'interface utilisateur en conséquence.
 */
public class FragmentHistoire extends Fragment{

    private FragmentHistoireBinding binding;
    private List<Stories> stories;
    private ActivityResultLauncher<Intent> qrCodeScanLauncher;


    /**
     * Méthode appelée pour créer la vue du fragment.
     *
     * @param inflater           Le LayoutInflater utilisé pour gonfler la vue.
     * @param container          Le conteneur dans lequel la vue sera placée.
     * @param savedInstanceState L'état précédemment enregistré du fragment, si disponible.
     * @return La vue du fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = FragmentHistoireBinding.inflate(getLayoutInflater());
        Stories.initStories(this.getResources());
        this.stories = Stories.getStories();
        this.initActivityResultLauncher();
        App.systemMessages.observe(getViewLifecycleOwner(), new Observer<List<SystemMessage>>() {

            @Override
            public void onChanged(List<SystemMessage> systemMessages) {
                if (!systemMessages.isEmpty()) {
                    SystemMessage lastSystemMessage = systemMessages.get(systemMessages.size() - 1);
                    boolean inSuccess = App.roomCode.stream()
                            .anyMatch(code -> code.equals(lastSystemMessage.getCode()));
                    if (!inSuccess) {
                        needQRCodeScann(lastSystemMessage.getCode());
                    }
                    else {
                        updateStoriesByServer();
                    }
                }
                else updateStoriesByServer();


            }
        });

        return binding.getRoot();
    }

    /**
     * Initialise le launcher pour le scan de QR code.
     */
    private void initActivityResultLauncher() {
        qrCodeScanLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String qrCodeContent = result.getData().getStringExtra("QR_CODE_CONTENT");
                if (qrCodeContent != null) {
                    if (getResources().getString(R.string.code_Enigma_2).equals(qrCodeContent)){
                        App.roomCode.add(qrCodeContent);
                        this.updateStoriesByServer();
                    }
                    if (getResources().getString(R.string.code_Enigma_2).equals(qrCodeContent) || getResources().getString(R.string.code_Enigma_3).equals(qrCodeContent)){
                        boolean inSuccess = App.roomCode.stream()
                                .anyMatch(code -> code.equals(getResources().getString(R.string.code_Enigma_2)));
                        if (inSuccess) {
                            App.roomCode.add(qrCodeContent);
                            SystemMessage systemMessage = new SystemMessage("Android", qrCodeContent);
                            Gson gson = new Gson();
                            Message message = new Message(gson.toJson(systemMessage), "2", "1");
                            App.socketManager.sendMessage(gson.toJson(message));
                            this.updateStoriesByServer();
                        }
                        else {
                            Toast.makeText(getContext(), "Vous vous êtes trompé de salle.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } else {
                Toast.makeText(getContext(), "Scan annulé", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Affiche un message demandant à l'utilisateur de scanner un QR code.
     *
     * @param code Le code qui nécessite un scan de QR code.
     */
    private void needQRCodeScann(String code){
        this.binding.liHistoires.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View itemLayout = inflater.inflate(R.layout.qrcode_asking_layout, this.binding.liHistoires, false);
        QrcodeAskingLayoutBinding qrCodeBinding = QrcodeAskingLayoutBinding.bind(itemLayout);
        qrCodeBinding.roomTextView.setTextColor(Color.WHITE);
        if (getResources().getString(R.string.code_Enigma_2).equals(code)){
            qrCodeBinding.roomTextView.setText("Retrouve moi en salle 108");
        }
        if (getResources().getString(R.string.code_Enigma_3).equals(code)){
            qrCodeBinding.roomTextView.setText("Retrouve moi en salle 118");
        }
        qrCodeBinding.qrScannerImageView.setOnClickListener((v -> {
            Intent intent = new Intent(getActivity(), QRCodeScanner.class);
            qrCodeScanLauncher.launch(intent);
        }));
        this.binding.liHistoires.addView(itemLayout);

    }

    /**
     * Met à jour les histoires affichées en fonction des messages du serveur.
     */
    private void updateStoriesByServer() {

        List<SystemMessage> systemMessages = App.systemMessages.getValue();
        this.binding.liHistoires.removeAllViews();
        this.stories.stream()
                .filter(story -> story.getOrder() == 2)
                .findFirst().ifPresent(this::updateHistoire);
        if(systemMessages == null || systemMessages.isEmpty()) {return;}
        for (SystemMessage systemMessage : systemMessages) {

            Map<String, Integer> codeToOrderMap = new HashMap<>();
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

    /**
     * Met à jour l'affichage d'une histoire.
     *
     * @param stories L'histoire à afficher.
     */
    private void updateHistoire(Stories stories) {

        LinearLayout parentLayout = this.binding.liHistoires;
        Map<String, Object> sequences = this.textToMap(stories.getHistoireText());
        String[] parts = stories.getHistoireText().split("/.*?/");
        parentLayout.post(()-> {
            if (!sequences.isEmpty()) {
                Map.Entry<String, Object> firstEntry = sequences.entrySet().iterator().next();
                String firstKey = firstEntry.getKey();
                Object firstValue = firstEntry.getValue();
                if (firstKey.equals("picture")) {
                    if (parts.length > 0){
                        parentLayout.addView(this.setEnigmaText(parts[0]));
                    }
                    parentLayout.addView(this.showImage((String) firstValue));
                    if (parts.length > 0){
                        parentLayout.addView(this.setEnigmaText(parts[1]));
                    }
                } else if (firstKey.equals("sequential")) {
                    if (parts.length > 0){
                        parentLayout.addView(this.setEnigmaText(parts[0]));
                    }
                    FrameLayout frameLayout = getFrameLayout((List<String>) firstValue);
                    parentLayout.addView(frameLayout);
                    if (parts.length > 0){
                        parentLayout.addView(this.setEnigmaText(parts[1]));
                    }
                }
            }
            else{
                parentLayout.addView(this.setEnigmaText(stories.getHistoireText()));
                parentLayout.addView(this.setEnigmaTips(stories, parentLayout));
            }

            this.binding.idSvMain.post(() -> this.binding.idSvMain.fullScroll(View.FOCUS_DOWN));
        });
    }

    /**
     * Crée un FrameLayout qui change de couleur.
     *
     * @param colorsString Liste de chaînes représentant les couleurs.
     * @return Un FrameLayout avec un changement de couleur.
     */
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

    /**
     * Crée un TextView pour afficher un texte d'énigme.
     *
     * @param stories Le texte à afficher.
     * @return Un TextView contenant le texte d'énigme.
     */
    private TextView setEnigmaText(String stories) {
        TextView histoireTextView = new TextView(this.getContext());
        histoireTextView.setText(stories);
        histoireTextView.setTextSize(18);
        histoireTextView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
//        textLayoutParams.setMargins(20, 20, 20, 10);
        histoireTextView.setLayoutParams(textLayoutParams);

        return histoireTextView;
    }

    /**
     * Crée un TextView pour afficher des astuces d'énigme.
     *
     * @param stories      L'histoire à laquelle les astuces appartiennent.
     * @param parentLayout Le LayoutParent dans lequel les astuces seront affichées.
     * @return Un TextView contenant les astuces d'énigme.
     */
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
//        tipsLayoutParams.setMargins(20, 0, 20, 20);
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
//            buttonLayoutParams.setMargins(20, 0, 20, 20);
            showTipsButton.setLayoutParams(buttonLayoutParams);

        }
        return histoireTipsView;
    }

    /**
     * Affiche une image à partir d'une chaîne de caractères représentant l'URL de l'image.
     *
     * @param path Le chemin de l'image.
     * @return Une ImageView contenant l'image.
     */
    private ImageView showImage(String path) {
        final ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(this.getResourceByName(path));

        // Utilisation de post() pour attendre que la vue soit attachée avant d'obtenir ses dimensions
        imageView.post(new Runnable() {
            @Override
            public void run() {
                // Obtenir la largeur et la hauteur du conteneur après que l'image soit attachée
                int containerWidth = ((ViewGroup) imageView.getParent()).getWidth();
                int containerHeight = ((ViewGroup) imageView.getParent()).getHeight();

                // Obtenir les dimensions de l'image
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();

                // Calculer le ratio de l'image
                float imageRatio = (float) imageWidth / (float) imageHeight;
                float containerRatio = (float) containerWidth / (float) containerHeight;

                // Déterminer les dimensions ajustées de l'image
                int newWidth = containerWidth;
                int newHeight = containerHeight;

                if (imageRatio > containerRatio) {
                    // Si l'image est plus large que le conteneur, ajuster la hauteur
                    newHeight = (int) (containerWidth / imageRatio);
                } else if (imageRatio < containerRatio) {
                    // Si l'image est plus haute que le conteneur, ajuster la largeur
                    newWidth = (int) (containerHeight * imageRatio);
                }

                // Vérifier et ajuster les dimensions pour éviter la coupure
                if (newWidth > containerWidth) {
                    newWidth = containerWidth;
                    newHeight = (int) (newWidth / imageRatio);
                }
                if (newHeight > containerHeight) {
                    newHeight = containerHeight;
                    newWidth = (int) (newHeight * imageRatio);
                }

                // Définir les LayoutParams en fonction des nouvelles dimensions
                LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(newWidth, newHeight);

                // Centrer l'image dans le conteneur
                imageLayoutParams.gravity = Gravity.CENTER;

                imageView.setLayoutParams(imageLayoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
        });

        return imageView;
    }



    @SuppressLint("DiscouragedApi")
    private Drawable getResourceByName(String resourceName) {
        int resourceId = getResources().getIdentifier(resourceName, "drawable", this.getActivity().getPackageName());
        if (resourceId != 0) {
            return ContextCompat.getDrawable(getContext(), resourceId);
        }
        return null;
    }


    /**
     * Transforme un texte en une carte de séquences.
     *
     * @param sequence Le texte à transformer.
     * @return Une carte représentant les séquences.
     */
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

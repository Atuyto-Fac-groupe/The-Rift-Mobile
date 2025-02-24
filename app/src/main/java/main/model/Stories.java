package main.model;


import android.content.res.Resources;
import com.example.therift.R;
import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import main.model.BDD.ObjectBox;

import java.util.List;

@Entity
public class Stories {

    @Id
    private long ind;


    private String histoireText;

    private String histoireTips;

    private boolean displayed;

    private boolean noButton;

    private int order;

    public Stories(long ind, String histoireText, String histoireTips, boolean displayed, boolean noButton, int order) {
        this.ind = ind;
        this.histoireText = histoireText;
        this.histoireTips = histoireTips;
        this.displayed = displayed;
        this.noButton = noButton;
        this.order = order;
    }


    public Stories(String histoireText, String histoireTips, int order) {
        this.histoireText = histoireText;
        this.histoireTips = histoireTips;
        this.order = order;
    }


    public long getInd() {
        return ind;
    }

    public void setInd(long ind) {
        this.ind = ind;
    }

    public String getHistoireText() {
        return histoireText;
    }

    public void setHistoireText(String histoireText) {
        this.histoireText = histoireText;
    }

    public String getHistoireTips() {
        return histoireTips;
    }

    public void setHistoireTips(String histoireTips) {
        this.histoireTips = histoireTips;
    }

    public boolean getDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public boolean getNoButton() {
        return noButton;
    }

    public void setNoButton(boolean noButton) {
        this.noButton = noButton;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public boolean isNoButton() {
        return noButton;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public static void initStories(Resources resources){
        Box<Stories> storiesBox = ObjectBox.get().boxFor(Stories.class);
        storiesBox.removeAll();
        Stories stories1 = new Stories(resources.getString(R.string.Enigma_1), "", 2);
        Stories stories2 = new Stories(resources.getString(R.string.Enigma_2), "", 3);
        Stories stories3 = new Stories(resources.getString(R.string.Enigma_3), "", 4);
        storiesBox.put(stories1, stories2, stories3);

    }

    public static List<Stories> getStories(){
        Box<Stories> storiesBox = ObjectBox.get().boxFor(Stories.class);
        return storiesBox.getAll();
    }
}

package main.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "histoire")
public class Histoire {

    @PrimaryKey(autoGenerate = true)
    private Long idHistoire;

    @ColumnInfo(name = "histoireText")
    private String histoireText;

    @ColumnInfo(name = "histoireTips")
    private String histoireTips;

    @ColumnInfo(name = "displayed")
    private boolean displayed;

    private boolean noButton;

    public Long getIdHistoire() {
        return idHistoire;
    }

    public void setIdHistoire(Long idHistoire) {
        this.idHistoire = idHistoire;
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
}

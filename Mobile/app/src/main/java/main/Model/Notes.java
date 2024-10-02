package main.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private Long idNotes;

    @ColumnInfo(name = "containerText")
    private String containerText;

    public Long getIdNotes() {
        return idNotes;
    }

    public void setIdNotes(Long idNotes) {
        this.idNotes = idNotes;
    }

    public String getContainerText() {
        return containerText;
    }

    public void setContainerText(String containerText) {
        this.containerText = containerText;
    }

    public Notes() {
        this.containerText = "";
    }
}

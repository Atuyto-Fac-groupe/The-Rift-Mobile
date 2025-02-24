package main.model;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity()
public class Notes {

    @Id
    private Long idNotes;

    private String containerText;


    public Notes(Long idNotes, String containerText) {
        this.idNotes = idNotes;
        this.containerText = containerText;
    }
    public Notes() {
        this.containerText = "";
    }

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


}

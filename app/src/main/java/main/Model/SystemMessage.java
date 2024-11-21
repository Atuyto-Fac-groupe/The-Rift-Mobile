package main.Model;

public class SystemMessage {

    private String titre;
    private String code;

    public SystemMessage(String titre, String code) {
        this.titre = titre;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}

package thomasapp.wineforfriendsnd;

/**
 * Created by thoma on 29/01/2018.
 */

public class WinesGETSET {

    private String chateaux;
    private String commentaire;
    private String usermail;
    private String type;
    private String pseudo;
    private String coupdecoeur;


    public String getCoupdecoeur() {
        return coupdecoeur;
    }

    public void setCoupdecoeur(String coupdecoeur) {
        this.coupdecoeur = coupdecoeur;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getChateaux() {
        return chateaux;
    }

    public void setChateaux(String chateaux) {
        this.chateaux = chateaux;
    }
}

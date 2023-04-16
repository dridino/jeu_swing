import java.awt.*;

public class Jeu {
    public static void main(String []args) {
        EventQueue.invokeLater(() -> {
        final Plateau plateau = new Plateau();
        System.out.print(plateau.toString());
            Vue vue = new Vue(plateau);
        });
    }
}

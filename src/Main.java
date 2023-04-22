import java.awt.*;
import java.util.ArrayList;

public class Main {
    public static void main(String []args) {
        EventQueue.invokeLater(() -> {
            final Game game = new Game(new ArrayList<>());
            Vue vue = new Vue(game);
        });
    }
}

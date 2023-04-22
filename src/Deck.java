import java.util.ArrayList;

public class Deck<E> {
    private ArrayList<E> elements;
    private DeckType type;

    public Deck(ArrayList<E> elements, DeckType type) {
        this.elements = elements;
        this.type = type;
    }

    public DeckType getType() {
        return this.type;
    }

    public int getRemaining() {
        return this.elements.size();
    }

    public E pick() {
        if (this.getRemaining() > 0) {
            final E elem = this.elements.remove(0);
            return elem;
        }
        return null;
    }
}

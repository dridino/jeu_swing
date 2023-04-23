import java.util.ArrayList;
import java.util.Collections;

public class Deck<E> extends Observable {
    private ArrayList<E> elements;
    private DeckType type;

    public Deck(ArrayList<E> elements, DeckType type) {
        Collections.shuffle(elements);
        this.elements = elements;
        this.type = type;
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public boolean isNotEmpty() {
        return !this.isEmpty();
    }

    public void add(E elem) {
        this.elements.add(elem);
        notifyObservers();
    }

    public E getFirst() {
        return this.elements.get(this.getRemaining() - 1);
    }

    public ArrayList<E> getAll() {
        return this.elements;
    }

    public void reset() {
        this.elements.removeIf(e -> true);
    }

    public void addAll(ArrayList<E> elements) {
        Collections.shuffle(elements);
        this.elements.addAll(elements);
        notifyObservers();
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

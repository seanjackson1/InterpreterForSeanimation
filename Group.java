import java.util.ArrayList;

public class Group {
    private ArrayList<Pixel> list;

    public Group() {
        list = new ArrayList<Pixel>();
    }

    public Group(Pixel a) {
        list = new ArrayList<Pixel>();
        list.add(a);
    }

    public Group(ArrayList<Pixel> list) {
        this.list = new ArrayList<Pixel>();
        for (Pixel a : list)
            this.list.add(a);
    }

    public ArrayList<Pixel> getList() {
        return list;
    }

    public void add(Pixel a) {
        list.add(a);
    }

    public void add(ArrayList<Pixel> list) {
        for (Pixel a : list)
            this.list.add(a);
    }

    public void show(GridDisplay display) {
        for (Pixel a : list)
            a.show(display);
    }

    public void hide(GridDisplay display, Color background) {
        for (Pixel a : list)
            a.hide(display, background);
    }

    public void shift(int deltaX, int deltaY) {
        for (Pixel a : list) {
            a.shift(deltaX, deltaY);
        }
    }

    public boolean isInside(int width, int length) {
        for (Pixel a : list) {
            if (!a.isInside(width, length)) {
                return false;
            }
        }
        return true;
    }
}
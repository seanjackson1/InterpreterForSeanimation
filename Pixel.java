public class Pixel {
    private Location loc;
    private Color color;

    public Pixel(int x, int y, Color color) {
        loc = new Location(x, y);
        this.color = color;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public int getRow() {
        return loc.getRow();
    }

    public int getCol() {
        return loc.getCol();
    }

    public void setRow(int r) {
        loc = new Location(r, loc.getCol());
    }

    public void setCol(int c) {
        loc = new Location(loc.getRow(), c);
    }

    public Color getColor() {
        return color;
    }

    public void show(GridDisplay display) {
        display.setColor(loc, color);
    }

    public void hide(GridDisplay display, Color background) {
        display.setColor(loc, background);
    }

    public void shift(int deltaX, int deltaY) {
        loc = new Location(loc.getRow() + deltaX, loc.getCol() + deltaY);
    }

    public boolean isInside(int width, int height) {
        return (loc.getRow() >= 0 && loc.getRow() < width && loc.getCol() >= 0 && loc.getCol() < height);
    }
}

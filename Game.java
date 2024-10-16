public class Game {
  private GridDisplay display;
  private Location heroLoc;
  private String heroImage;

  public Game() {
    // create GridDisplay with 3 rows and 5 columns, and title "Game"
    display = new GridDisplay(3, 5);

    display.setTitle("Game");

    // put hero in bottom-left corner
    heroLoc = new Location(2, 0);
    heroImage = "circle.png";
    display.setImage(heroLoc, heroImage);

  }

  public void play() {
    while (true) {
      // wait 100 milliseconds for mouse or keyboard
      display.pause(100);

      // check if any locations clicked or keys pressed
      Location clickLoc = display.checkLastLocationClicked();
      int key = display.checkLastKeyPressed();

      if (clickLoc != null) {
        // a location was clicked
        locationClicked(clickLoc);
      }

      if (key != -1) {
        // a key was pressed
        keyPressed(key);
      }

      step();
    }
  }

  // called when the user clicks on a location.
  // that location is passed to this method.
  private void locationClicked(Location loc) {
    // change color at loc
    display.setColor(loc, new Color(255, 0, 0));
  }

  // called when the user presses a key.
  // each key on the keyboard has a unique key code.
  // that key code is passed to this method.
  private void keyPressed(int key) {
    // print key code
    System.out.println("key code:  " + key);

    if (key == 32) {
      // space key was pressed

      // pick new location for hero
      int col = heroLoc.getCol();
      Location newLoc;
      if (col == 4)
        newLoc = new Location(2, 0); // bottom-left cell
      else
        newLoc = new Location(2, col + 1); // next cell to right

      // erase image at old location
      display.setImage(heroLoc, null);

      // remember new location
      heroLoc = newLoc;

      // show image at new location
      display.setImage(heroLoc, heroImage);
    } else {
      // some other key was pressed

      // show help message
      display.showMessageDialog("Press SPACE to move.\nClick to change color.");
    }
  }

  // this method is called at regular intervals
  public void step() {
    // maybe change color of random location to black
    if (Math.random() < 0.1) {
      int row = (int) (Math.random() * 3);
      int col = (int) (Math.random() * 5);
      display.setColor(new Location(row, col), new Color(0, 0, 0));
    }
  }

  // this code starts a game when you click the run button
  public static void main(String[] args) {
    Game g = new Game();
    g.play();
  }
}

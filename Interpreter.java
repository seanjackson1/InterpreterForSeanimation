import java.io.*;
import java.util.*;

public class Interpreter {
  private static Scanner in = new Scanner(System.in);

  private static GridDisplay display;
  private static Color background;
  private static int rowsFromDrawing;
  private static int colsFromDrawing;

  public static void main(String[] args) throws FileNotFoundException, IOException {
    Parser p = new Parser();

    p.addPrefix("print", 1); // print PREcedes 1 argument (e.g. "print 3")
    p.addPrefix("setGrid", 3);
    p.addPrefix("Color", 3);
    p.addPrefix("Pixel", 3);
    p.addPrefix("newGroup", 0);
    p.addPrefix("true", 0);
    p.addPrefix("false", 0);
    p.addPrefix("Group", 1);
    p.addPrefix("addToGroup", 2);
    p.addPrefix("while", 2);
    p.addPrefix("if", 2);
    p.addPrefix("show", 1);
    p.addPrefix("hide", 1);
    p.addPrefix("pause", 1);
    p.addPrefix("shift", 3);
    p.addPrefix("getRow", 1);
    p.addPrefix("getCol", 1);
    p.addPrefix("setRow", 2);
    p.addPrefix("setCol", 2);
    p.addPrefix("randInt", 2);
    p.addPrefix("isInside", 3);
    p.addPrefix("sqrt", 1);
    p.addPrefix("square", 1);
    p.addPrefix("abs", 1);
    p.addPrefix("drawImage", 3);
    p.addPrefix("imageFromDrawing", 3);

    p.addInfix("+", 2); // + falls IN between 2 arguments (e.g. "2 + 3")
    p.addInfix("*", 2);
    p.addInfix("/", 2);
    p.addInfix("-", 2);
    p.addInfix("=", 2);
    p.addInfix("<", 2);
    p.addInfix(">", 2);
    p.addInfix("==", 2);
    p.addInfix("!=", 2);
    p.addDelimiters("(", ")");
    p.addDelimiters("{", "}");

    State state = new State();

    Object program = p.parse(load("testing copy.txt")); // use to parse a string literal

    // Object program = p.parse(load("program.txt")); //use to parse a program
    // stored in a file
    // Object program = p.parse(input()); //use to parse a string entered by the
    // user

    eval(program, state);

  }

  public static Object eval(Object exp, State state) throws FileNotFoundException, IOException {
    if (exp instanceof Integer) {
      // the value of an integer is itself
      return exp;
    } else if (exp instanceof String) {
      String varName = (String) exp;
      return state.getVariableValue(varName);
    } else {
      // must be a List
      List list = (List) exp;

      if (list.get(0).equals("{")) {
        for (int i = 1; i < list.size() - 1; i++)
          eval(list.get(i), state);
        return "OK";
      }

      if (list.get(0).equals("(")) {
        Object argument = list.get(1);
        return eval(argument, state);
      }

      if (list.get(0).equals("while")) {

        while ((Boolean) eval(list.get(1), state)) {
          eval(list.get(2), state);
        }

        return "OK";
      }

      if (list.get(0).equals("sqrt")) {
        Object argument = list.get(1);
        return (int) (Math.sqrt((int) eval(argument, state)) + .5);
      }

      if (list.get(0).equals("abs")) {
        Object argument = list.get(1);
        return (int) Math.abs((int) eval(argument, state));
      }

      if (list.get(0).equals("isInside")) {
        Object arg1 = list.get(1);
        Object arg2 = list.get(2);
        Object arg3 = list.get(3);
        try {
          return ((Group) eval(arg1, state)).isInside((Integer) eval(arg2, state), (Integer) eval(arg3, state));
        } catch (Exception e) {
          return ((Pixel) eval(arg1, state)).isInside((Integer) eval(arg2, state), (Integer) eval(arg3, state));
        }
      }

      if (list.get(0).equals("if")) {

        if ((Boolean) eval(list.get(1), state)) {
          eval(list.get(2), state);
        }

        return "OK";
      }

      if (list.get(0).equals("print")) {
        System.out.println(eval(list.get(1), state));
        return "OK";
      }

      if (list.get(0).equals("show")) {
        try {
          ((Pixel) eval(list.get(1), state)).show(display);
        } catch (Exception e) {
          ((Group) eval(list.get(1), state)).show(display);
        }
        return "OK";
      }

      if (list.get(0).equals("hide")) {
        try {
          ((Pixel) eval(list.get(1), state)).hide(display, background);
        } catch (Exception e) {
          ((Group) eval(list.get(1), state)).hide(display, background);
        }
        return "OK";
      }

      if (list.get(0).equals("pause")) {
        try {
          display.repaint();
          Thread.sleep((Integer) eval(list.get(1), state));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return "OK";
      }

      if (list.get(0).equals("true")) {
        return true;
      }

      if (list.get(0).equals("false")) {
        return false;
      }

      if (list.get(0).equals("shift")) {
        try {
          ((Pixel) eval(list.get(1), state)).shift((Integer) eval(list.get(2), state),
              (Integer) eval(list.get(3), state));
        } catch (Exception e) {
          ((Group) eval(list.get(1), state)).shift((Integer) eval(list.get(2), state),
              (Integer) eval(list.get(3), state));
        }
        return "OK";
      }

      if (list.get(0).equals("getRow")) {
        return ((Pixel) eval(list.get(1), state)).getRow();
      }

      if (list.get(0).equals("getCol")) {
        return ((Pixel) eval(list.get(1), state)).getCol();
      }

      if (list.get(0).equals("setRow")) {
        ((Pixel) eval(list.get(1), state)).setRow((Integer) eval(list.get(2), state));
        return "OK";
      }

      if (list.get(0).equals("setCol")) {
        ((Pixel) eval(list.get(1), state)).setCol((Integer) eval(list.get(2), state));
        return "OK";
      }

      if (list.get(0).equals("randInt")) {
        return (int) (Math.random() * (1 + (Integer) eval(list.get(2), state) - (Integer) eval(list.get(1), state))
            + (Integer) eval(list.get(1), state));
      }

      if (list.get(0).equals("Color")) // print EXP
      {
        return new Color((Integer) eval(list.get(1), state), (Integer) eval(list.get(2), state),
            (Integer) eval(list.get(3), state));
      }

      if (list.get(0).equals("Pixel")) // print EXP
      {
        int row = (Integer) eval(list.get(1), state);
        int col = (Integer) eval(list.get(2), state);

        Color color = (Color) eval(list.get(3), state);

        return new Pixel(row, col, color);
      }

      if (list.get(0).equals("Group")) // print EXP
      {

        Pixel a = (Pixel) eval(list.get(1), state);
        return new Group(a);

      }

      if (list.get(0).equals("newGroup")) // print EXP
      {
        return new Group();

      }

      if (list.get(0).equals("addToGroup")) // print EXP
      {
        try {
          Pixel a = (Pixel) eval(list.get(2), state);

          ((Group) eval(list.get(1), state)).add(a);
        } catch (Exception e) {
          Group a = (Group) eval(list.get(2), state);
          for (Pixel p : a.getList()) {
            ((Group) eval(list.get(1), state)).add(p);
          }

        }

        return "OK";
      }

      if (list.get(0).equals("setGrid")) // print EXP
      {
        int rows = (Integer) eval(list.get(1), state);
        int cols = (Integer) eval(list.get(2), state);

        background = (Color) eval(list.get(3), state);

        display = new GridDisplay(rows, cols);
        display.setTitle("Seanimation");
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < cols; j++) {
            display.setColor(new Location(i, j), background);
          }
        }
        return "OK";

      }

      if (list.get(0).equals("imageFromDrawing")) // print EXP
      {
        int row = (Integer) eval(list.get(1), state);
        int col = (Integer) eval(list.get(2), state);

        Color colour = (Color) eval(list.get(3), state);

        Group c = new Group();

        BufferedReader br = new BufferedReader(new FileReader("drawing.txt"));

        StringTokenizer st = new StringTokenizer(br.readLine());

        for (int i = 0; i < rowsFromDrawing; i++) {
          for (int j = 0; j < colsFromDrawing; j++) {
            if (Integer.parseInt(st.nextToken()) == 1)
              c.add(new Pixel(i + row, j + col, colour));
          }
          String s = br.readLine();
          if (s != null)
            st = new StringTokenizer(s);
        }

        br.close();

        return c;

      }

      if (list.get(0).equals("drawImage")) // print EXP
      {
        int rows = (Integer) eval(list.get(1), state);
        int cols = (Integer) eval(list.get(2), state);

        rowsFromDrawing = rows;
        colsFromDrawing = cols;

        background = (Color) eval(list.get(3), state);

        display = new GridDisplay(rows, cols);
        display.setTitle("Draw Image");
        int drawing[][] = new int[rows][cols];

        Location clickLoc = display.checkLastLocationClicked();
        int key = display.checkLastKeyPressed();

        while (key != 81 && key != 27) {
          // wait 100 milliseconds for mouse or keyboard
          display.pause(100);

          // check if any locations clicked or keys pressed

          clickLoc = display.checkLastLocationClicked();
          key = display.checkLastKeyPressed();

          if (clickLoc != null && clickLoc.getRow() >= 0 && clickLoc.getCol() >= 0) {
            display.setColor(clickLoc, background);
            drawing[clickLoc.getRow()][clickLoc.getCol()] = 1;
            display.repaint();
          }

        }

        PrintWriter pw = new PrintWriter(new File("drawing.txt"));

        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < cols; j++) {
            pw.print(drawing[i][j] + " ");
          }
          pw.println();
        }

        pw.close();

        return "OK";

      }

      if (list.get(1).equals("=")) // EXP + EXP
      {
        state.setVariableValue((String) list.get(0), eval(list.get(2), state));
        return "OK";
      }

      if (list.get(1).equals(">")) // EXP + EXP
      {
        int argument1 = (Integer) eval(list.get(0), state);
        int argument2 = (Integer) eval(list.get(2), state);

        return argument1 > argument2;
      }

      if (list.get(1).equals("<")) // EXP + EXP
      {
        int argument1 = (Integer) eval(list.get(0), state);
        int argument2 = (Integer) eval(list.get(2), state);

        return argument1 < argument2;
      }

      if (list.get(1).equals("==")) {
        int argument1 = (Integer) eval(list.get(0), state);
        int argument2 = (Integer) eval(list.get(2), state);

        return argument1 == argument2;

      }

      if (list.get(1).equals("!=")) {
        int argument1 = (Integer) eval(list.get(0), state);
        int argument2 = (Integer) eval(list.get(2), state);

        return argument1 != argument2;

      }

      if (list.get(1).equals("+")) // EXP + EXP
      {
        Object argument1 = list.get(0);
        Object argument2 = list.get(2);
        return (Integer) (eval(argument1, state)) + (Integer) (eval(argument2, state));
      }

      if (list.get(1).equals("*")) // EXP + EXP
      {
        Object argument1 = list.get(0);
        Object argument2 = list.get(2);
        return (Integer) (eval(argument1, state)) * (Integer) (eval(argument2, state));
      }

      if (list.get(1).equals("/")) // EXP + EXP
      {
        Object argument1 = list.get(0);
        Object argument2 = list.get(2);
        try {
          return (Integer) (eval(argument1, state)) / (Integer) (eval(argument2, state));
        } catch (Exception e) {
          return "Nah";
        }
      }

      if (list.get(1).equals("-")) // EXP + EXP
      {
        Object argument1 = list.get(0);
        Object argument2 = list.get(2);
        return (Integer) eval(argument1, state) - (Integer) eval(argument2, state);
      }

      throw new RuntimeException("unable to evaluate:  " + exp);
    }
  }

  public static String input() {
    return in.nextLine();
  }

  public static String load(String fileName) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        sb.append("\n");
        line = br.readLine();
      }
      br.close();
      return sb.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
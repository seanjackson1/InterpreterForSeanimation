import java.util.*;

public class State {
    private HashMap<String, Object> map;

    public State() {
        map = new HashMap<String, Object>();
    }

    public void setVariableValue(String s, Object E) {
        map.put(s, E);
    }

    public Object getVariableValue(String s) {
        return map.get(s);
    }
}

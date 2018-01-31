package Entity;

/**
 * Created by liyang on 2017/10/24.
 */

public class Result {
    private String state;
    private String result;

    public Result(String state, String result) {
        this.state = state;
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

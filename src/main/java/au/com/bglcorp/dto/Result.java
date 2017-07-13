package au.com.bglcorp.dto;

/**
 * Created by senthurshanmugalingm on 12/07/2017.
 */
public class Result<T> {

    T result;

    public Result(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

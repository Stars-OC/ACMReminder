package xyz.starsoc.pojo.codeforces;

import java.util.List;

public class CFRespond<T> {

    private String status;
    private List<T> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CFRespond{" +
                "status='" + status + '\'' +
                ", result=" + result +
                '}';
    }
}

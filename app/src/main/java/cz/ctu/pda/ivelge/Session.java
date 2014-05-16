package cz.ctu.pda.ivelge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Session {

    private long id;
    private long startTime;
    private long endTime;
    private String preTest;
    private String postTest;
    private List<Log> logs;

    public Session(long id, long startTime, long endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        logs = new ArrayList<Log>();
    }

    public Session(long id, long startTime, long endTime, String preTest, String postTest, List<Log> logs) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.preTest = preTest;
        this.postTest = postTest;
        this.logs = logs;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPreTest() {
        return preTest;
    }

    public void setPreTest(String preTest) {
        this.preTest = preTest;
    }

    public String getPostTest() {
        return postTest;
    }

    public void setPostTest(String postTest) {
        this.postTest = postTest;
    }


}

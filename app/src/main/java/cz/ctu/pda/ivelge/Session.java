package cz.ctu.pda.ivelge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedlosh on 16/05/2014.
 */
public class Session {

    private long id;
    private long startTime=-1;
    private long endTime;
    private String preTest;
    private String postTest;
    private List<Log> logs;
    private long testId;
    private String participantName;

    //For new instances
    public Session(String participantName) {
        id = -1;
        logs = new ArrayList<Log>();
        this.participantName=participantName;
        testId = -1;
    }

    //For DB import
    public Session(long id, long startTime, long endTime, String preTest, String postTest, List<Log> logs,long testId, String participantName) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.preTest = preTest;
        this.postTest = postTest;
        this.logs = logs;
        this.testId = testId;
        this.participantName=participantName;

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

    public String getParticipantName(){
        return participantName;
    }

    public void setParticipantName(String name){
        participantName=name;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public int getNumberOfLogs(){
        return logs.size();
    }
}

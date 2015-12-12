package com.floyd.diamond.event;

/**
 * Created by floyd on 15-12-12.
 */
public class AcceptTaskEvent {

    public AcceptTaskEvent(long taskId) {
        this.taskId = taskId;
    }
    public long taskId;

}

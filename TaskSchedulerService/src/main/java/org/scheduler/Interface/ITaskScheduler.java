package org.scheduler.Interface;

import org.handler.Interface.IHandler;
import org.handler.TaskContext;

public interface ITaskScheduler {
    String[] getTaskUids();
    TaskContext getTaskContext(String uid);
    void addTask(String uid, TaskContext task);
}

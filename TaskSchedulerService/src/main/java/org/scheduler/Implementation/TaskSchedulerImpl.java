package org.scheduler.Implementation;

import org.handler.TaskContext;
import org.scheduler.Interface.ITaskScheduler;

import java.util.HashMap;

public class TaskSchedulerImpl implements ITaskScheduler {
    private HashMap<String, TaskContext> taskList;

    public TaskSchedulerImpl() {
        this.taskList = new HashMap<>();
    }

    public void removeTask(String uid) {
        taskList.remove(uid);
    }

    @Override
    public void addTask(String uid, TaskContext task) {
        taskList.put(uid, task);
    }

    @Override
    public String[] getTaskUids() {
        return taskList.keySet().toArray(new String[0]);
    }

    @Override
    public TaskContext getTaskContext(String uid) {
        return taskList.get(uid);
    }
}
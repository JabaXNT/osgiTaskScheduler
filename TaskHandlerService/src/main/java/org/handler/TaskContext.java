package org.handler;

import org.handler.Implementation.TaskHandlerImpl;
import org.handler.Interface.IHandler;
import org.osgi.framework.ServiceReference;

import java.io.IOException;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskContext {

    private final IHandler task;
    private final Dictionary props;
    private Thread runningThread;

    public enum TaskState {
        TIMEDOUT, ARMED, RUNNING, PAUSED, REMOVED
    }
    
    private TaskState state;

    public TaskContext(ServiceReference<IHandler> taskRef, IHandler task) {
        this.state = TaskState.ARMED;
        this.task = task;
        this.props = taskRef.getProperties();
    }

    private void _startTask() {
        this.state = TaskState.RUNNING;
        int interval = (Integer) props.get("interval");
        while (true){
            try {
                System.out.println("Start handling task " + Thread.currentThread().threadId());
                task.handleTask(2000);
                System.out.println("Ended handling task " + Thread.currentThread().threadId());
                System.out.println("waiting for " + interval + "ms..." + Thread.currentThread().threadId());
                Thread.sleep(interval);
            }
            catch (RuntimeException e) {
                this.state = TaskState.TIMEDOUT;
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void startTask() {
        if (this.state != TaskState.ARMED) {
            System.out.println("Task is not ready to run");
            return;
        }
        runningThread = Thread.currentThread();
        _startTask();
    }

    public void pauseTask() throws InterruptedException {
        if (this.state != TaskState.RUNNING) {
            System.out.println("Task is not running");
            return;
        }
        runningThread.wait();
        this.state = TaskState.PAUSED;
        System.out.println("Task paused");
    }

    public void removeTask() {
        this.state = TaskState.REMOVED;
        runningThread.interrupt();
        System.out.println("Task removed");
    }

    public TaskState getState() {
        return state;
    }
    public Map<String, Object> getProperties() {
        return (Map<String, Object>) Collections.list(props.keys()).stream()
                .collect(Collectors.toMap(key -> key, props::get));
    }
}
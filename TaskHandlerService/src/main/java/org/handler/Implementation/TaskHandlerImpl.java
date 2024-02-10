// TaskHandlerImpl.java
package org.handler.Implementation;

import org.handler.Interface.IHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class TaskHandlerImpl implements IHandler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private void _handleTask() throws InterruptedException {
        System.out.println("I'm working..." + currentThread().threadId());
        sleep(3000);
        System.out.println("I'm done..." + currentThread().threadId());
    }

    public void handleTask(long timeout) {
        final var future = scheduler.schedule(() -> System.out.println("huyg"), 2, TimeUnit.SECONDS);
        try {
            _handleTask();
        } catch (InterruptedException e) {
            System.out.println("I'm interrupted..." + currentThread().threadId());
            throw new RuntimeException(e);
        }
        if (!future.isDone()) {
            future.cancel(false);
        }
    }
}
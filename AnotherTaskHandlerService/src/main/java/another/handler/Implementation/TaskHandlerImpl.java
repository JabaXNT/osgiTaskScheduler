// TaskHandlerImpl.java
package another.handler.Implementation;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import com.global.handler.IHandler;
public class TaskHandlerImpl implements IHandler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private void _handleTask() throws InterruptedException {
        System.out.println("I'm working in another bundle..." + currentThread().threadId());
        sleep(2000);
        System.out.println("I'm done..." + currentThread().threadId());
    }

    public void handleTask(long timeout) {
        final var future = scheduler.schedule(currentThread()::interrupt, timeout, TimeUnit.MILLISECONDS);
        try {
            _handleTask();
        } catch (InterruptedException e) {
            System.out.println("I'm interrupted..." + currentThread().threadId());
            throw new RuntimeException("Task timed out", e);
        }
        if (!future.isDone()) {
            future.cancel(false);
        }
    }
}
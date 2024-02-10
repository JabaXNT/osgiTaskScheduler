package org.scheduler;

import org.handler.Implementation.TaskHandlerImpl;
import org.handler.Interface.IHandler;
import org.handler.TaskContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
//import org.osgi.service.event.Event;
//import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.scheduler.Implementation.TaskSchedulerImpl;

import java.util.HashMap;
import java.util.Map;

public class TaskSchedulerActivator implements BundleActivator {

    static final String TASK_ADDED = "ru/unicron/service/scheduler/TaskEvent/TASK_ADDED";
    static final String TASK_TIMEDOUT = "ru/unicron/service/scheduler/TaskEvent/TASK_TIMEDOUT";
    static final String TASK_REMOVED = "ru/unicron/service/scheduler/TaskEvent/TASK_REMOVED";
    private ServiceTracker<IHandler, IHandler> serviceTracker;
//    private EventAdmin eventAdmin;

    @Override
    public void start(BundleContext bundleContext) {
//        ServiceReference<EventAdmin> eventAdminReference = bundleContext.getServiceReference(EventAdmin.class);
//        eventAdmin = bundleContext.getService(eventAdminReference);
        TaskSchedulerImpl taskScheduler = new TaskSchedulerImpl();
        System.out.println("Scheduler initialized");
        serviceTracker = new ServiceTracker<>(
                bundleContext,
                IHandler.class.getName(),
                new ServiceTrackerCustomizer<>() {
                    @Override
                    public IHandler addingService(ServiceReference<IHandler> reference) {
                        System.out.println("TaskSchedulerActivator: Adding service..." + " " + reference.getProperty("uid") + " " + Thread.currentThread().threadId());
                        IHandler taskHandler = bundleContext.getService(reference);
                        System.out.println("getService: " + taskHandler);
                        int interval = (Integer) reference.getProperty("interval");
                        String uid = (String) reference.getProperty("uid");
                        TaskContext taskContext = new TaskContext(reference, taskHandler);
                        new Thread(taskContext::startTask).start();
                        System.out.println("TaskSchedulerActivator: Task started... " + reference.getProperty("uid") + " " + Thread.currentThread().threadId());
                        taskScheduler.addTask(uid, taskContext);
                        sendEvent(TASK_ADDED, reference);
                        return taskHandler;
                    }

                    @Override
                    public void modifiedService(ServiceReference<IHandler> reference, IHandler service) {
                    }

                    @Override
                    public void removedService(ServiceReference<IHandler> reference, IHandler service) {
                        String uid = (String) reference.getProperty("uid");
                        taskScheduler.getTaskContext(uid).removeTask();
                        taskScheduler.removeTask(uid);
                        sendEvent(TASK_REMOVED, reference);
                        bundleContext.ungetService(reference);
                    }
                }
        );

        serviceTracker.open();
    }

    @Override
    public void stop(BundleContext bundleContext) {
        System.out.println("TaskSchedulerActivator: Stopping service...");
        serviceTracker.close();
    }

    private void sendEvent(String eventType, ServiceReference<IHandler> reference) {
        Map<String, Object> properties = new HashMap<>();
        String taskUid = (String) reference.getProperty("uid");
        Integer taskInterval = (Integer) reference.getProperty("interval");
        Integer taskPriority = (Integer) reference.getProperty("priority");

        properties.put("taskUid", taskUid);
        properties.put("taskInterval", taskInterval);
        properties.put("taskPriority", taskPriority);
//        Event event = new org.osgi.service.event.Event(eventType, properties);
//        eventAdmin.postEvent(event);
        System.out.println("Task event: " + eventType + ", uid: " + taskUid);
    }
}
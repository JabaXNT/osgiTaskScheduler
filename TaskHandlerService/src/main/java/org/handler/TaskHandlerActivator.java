package org.handler;

import org.handler.Implementation.TaskHandlerImpl;
import org.handler.Interface.IHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import static java.lang.Thread.currentThread;

public class TaskHandlerActivator implements BundleActivator {

    ServiceRegistration serviceRegistration;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Handler initialized");
        String uid = "some-uid";
        int interval = 2000;
        int priority = 1;
        Dictionary properties = new Hashtable();
        properties.put("uid", uid);
        properties.put("interval", interval);
        properties.put("priority", priority);
        TaskHandlerImpl taskHandler = new TaskHandlerImpl();
        System.out.println("TaskHandlerActivator: Starting service...");
        serviceRegistration = context.registerService(IHandler.class.getName(), taskHandler, properties);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("TaskHandlerActivator: Stopping service... " + currentThread().threadId());
        serviceRegistration.unregister();
    }
}
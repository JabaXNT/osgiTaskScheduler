package another.handler;

import another.handler.Implementation.TaskHandlerImpl;
import com.global.handler.IHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;

import static java.lang.Thread.currentThread;

public class AnotherTaskHandlerActivator implements BundleActivator {

    ServiceRegistration serviceRegistration;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Another Handler initialized");

        // Задаем свойства для регистрации сервиса
        String uid = "another-uid";
        int interval = 3000;
        int timeout = 5000;
        Dictionary properties = new Hashtable();
        properties.put("uid", uid);
        properties.put("interval", interval);
        properties.put("timeout", timeout);

        TaskHandlerImpl taskHandler = new TaskHandlerImpl();
        System.out.println("TaskHandlerActivator: Starting service...");

        // Регистрируем сервис
        serviceRegistration = context.registerService(IHandler.class.getName(), taskHandler, properties);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("TaskHandlerActivator: Stopping service... " + currentThread().threadId());
        serviceRegistration.unregister();
    }
}
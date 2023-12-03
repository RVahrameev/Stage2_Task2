package vtb.courses.stage2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CacheInvocationHandler implements InvocationHandler {
    private HashMap<Object, Boolean> objectHashMap;
    public CacheInvocationHandler() {
        objectHashMap = new HashMap<Object, Boolean>();
    }

    public void addObjectToCache(Object object) {
        objectHashMap.put(object, true);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Setter.class)) {
            System.out.println("Object state was changed!");
            objectHashMap.put(proxy, true);
        } else if (method.isAnnotationPresent(Cache.class)) {
            if (objectHashMap.containsKey(proxy)) {
                if (objectHashMap.get(proxy).booleanValue()) {
                    System.out.println("Call main cached method!");
                    objectHashMap.put(proxy, false);
                    return method.invoke(proxy, args);
                } else {
                    System.out.println("Cached object not changed, skip method call!");
                }
            }
        }
        return null;
    }
}

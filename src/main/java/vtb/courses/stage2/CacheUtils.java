package vtb.courses.stage2;

import java.lang.reflect.Proxy;

public class CacheUtils {
    //https://javarush.com/groups/posts/2281-dinamicheskie-proksi
    private static CacheUtils cacheUtils;
    private CacheInvocationHandler cacheInvocationHandler;

    public CacheUtils() {
        cacheInvocationHandler = new CacheInvocationHandler();
    }

    public static Object cache(Object object) {
        if (cacheUtils == null) {
            cacheUtils = new CacheUtils();
        }
        cacheUtils.cacheInvocationHandler.addObjectToCache(object);
        return Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CacheInvocationHandler());
    }
}

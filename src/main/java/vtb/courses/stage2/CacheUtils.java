package vtb.courses.stage2;

import java.lang.reflect.Proxy;

public class CacheUtils {
    //https://javarush.com/groups/posts/2281-dinamicheskie-proksi
    public static Cachable cache(Cachable object) {
        return (Cachable) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CacheInvocationHandler(object));
    }
}

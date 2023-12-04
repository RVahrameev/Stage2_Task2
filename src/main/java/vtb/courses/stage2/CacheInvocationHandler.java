package vtb.courses.stage2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *  Класс CacheInvocationHandler перехватывает вызовы методов интерфейса {@link Cachable} прокси-объекта
 *  Реализует логику кэширования вызовов методов помеченных аннотацией @Cache
 *  Делает перевызов интерфейсных методов исходного объекта помеченных аннотацией @Cache,
 *  только в том случае, если состояние объекта было изменено или объект находся в исходном состоянии.
 *  Изменение объекта определяется фиксацией вызовов интерфейсных методов помеченных аннотацией @Setter.
 *  Остальные интерфейсный методы перевызываются на исходном объекте без изменения логики работы.
 */
public class CacheInvocationHandler implements InvocationHandler {
    private final Cachable cachableObject;
    private boolean changed;
    public CacheInvocationHandler(Cachable cachableObject) {
        this.cachableObject = cachableObject;
        // Первоначальное состояние = "Изменён", т.к. первый вызов метода обязательно должено отработать
        this.changed = true;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            if (changed) {
                changed = false;
            } else {
                System.out.println("    Cached object not changed, skip method call!");
                return null;
            }
        } else if (method.isAnnotationPresent(Setter.class)) {
            System.out.println("    Object state start to change!");
            changed = true;
        }
        // Если дошли до этой точки, то просто вызываем на проксируемом объекте интерфейсный метод
        return method.invoke(cachableObject, args);
    }
}

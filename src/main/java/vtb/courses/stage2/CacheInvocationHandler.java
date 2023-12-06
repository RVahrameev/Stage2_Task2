package vtb.courses.stage2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 *  Класс <b>CacheInvocationHandler</b> перехватывает вызовы методов интерфейса T прокси-объекта
 *  Реализует логику кэширования вызовов методов помеченных аннотацией <b>@Cache</b>
 *  Делает перевызов интерфейсных методов, помеченных аннотацией <b>@Cache</b>, исходного объекта <b>cachableObject</b>
 *  только в том случае, если состояние объекта было изменено или объект находся в исходном состоянии.
 *  Изменение объекта определяется фиксацией вызовов интерфейсных методов помеченных аннотацией <b>@Setter</b>.
 *  Остальные интерфейсный методы перевызываются на исходном объекте без изменения логики работы.
 *  <p>
 *  Для того чтобы задать отслеживаемый объект, используется метод <b>cache()</b>
 *  <p>
 *  Идея позаимствована тут <a href="https://javarush.com/groups/posts/2281-dinamicheskie-proksi">https://javarush.com/groups/posts/2281-dinamicheskie-proksi</a>
 */
public class CacheInvocationHandler<T> implements InvocationHandler {
    private T cachedObject;
    private boolean cachedObjectChanged;
    private final HashMap<Method, Object> lastValues;

    public CacheInvocationHandler() {
        lastValues = new HashMap<>();
    }

    public T cache(T object) {
        this.cachedObject = object;
        // Первоначальное состояние = "Изменён", т.к. первый вызов метода обязательно должено отработать
        this.cachedObjectChanged = true;

        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Method cachedObjectMethod = cachedObject.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (cachedObjectMethod.isAnnotationPresent(Cache.class)) {
                if (cachedObjectChanged) {
                    cachedObjectChanged = false;
                    Object lastValue = method.invoke(cachedObject, args);
                    lastValues.put(method, lastValue);
                    return lastValue;
                } else {
                    System.out.println("Cached object not changed, skip method " + method.getName() + " call!");
                    return lastValues.get(method);
                }
            } else if (cachedObjectMethod.isAnnotationPresent(Setter.class)) {
                System.out.println("Object state start to change!");
                cachedObjectChanged = true;
            }
        } catch (NoSuchMethodException e) {}

        // Если дошли до этой точки, то просто вызываем на проксируемом объекте перехваченный метод
        System.out.println("Call native object method " + method.getName());
        return method.invoke(cachedObject, args);
    }
}

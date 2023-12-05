package vtb.courses.stage2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        if (method.isAnnotationPresent(Cache.class)) {
            if (cachedObjectChanged) {
                cachedObjectChanged = false;
            } else {
                System.out.println("Cached object not changed, skip method " + method.getName() + " call!");
                return null;
            }
        } else if (method.isAnnotationPresent(Setter.class)) {
            System.out.println("Object state start to change!");
            cachedObjectChanged = true;
        }

        // Если дошли до этой точки, то просто вызываем на проксируемом объекте интерфейсный метод
        System.out.println("Call native object method " + method.getName());
        return method.invoke(cachedObject, args);
    }
}

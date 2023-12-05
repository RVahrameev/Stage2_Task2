package vtb.courses.stage2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CachableTest {

    private Cachable proxyObject;

    public static class CachableClass implements Cachable{

        public int sourceData;
        public long calcData;
        @Override
        public void cachableMethod() {
            calcData = (long) sourceData * (long) sourceData;
        }

        @Override
        public void setterMethod(Object object){
            sourceData = (Integer)object;
        }

        @Override
        public void simpleMethod() {
            sourceData = sourceData / 2;
        }
    }

    @Test
    @DisplayName("Тестирование прокси класса и функционала кэширования")
    public void test(){
        // Создаём объект, который будем кешировать. Т.е. создавать для него прокси
        CachableClass cachableObject = new CachableClass();

        // Сначала создаём прокси объект для нашего тестового кэшируемого объекта
        Assertions.assertDoesNotThrow(() -> {proxyObject = new CacheInvocationHandler<Cachable>().cache(cachableObject);}, "Не удалось создать прокси для объекта cachableObject");

        // Устанавливаем первоначальное значение, для проверки того что отработает первичный запуска кэшируемого метода
        cachableObject.sourceData = 10;

        // Вызываем на прокси кэшируемый метод
        Assertions.assertDoesNotThrow(() -> proxyObject.cachableMethod(), "Не удалось на прокси объекта вызвать cachableMethod 1");

        // Проверяем результат работы кэшируемого метода, т.к. это первый вызов он должен был отработать
        Assertions.assertEquals(100, cachableObject.calcData, "caldData должна равнятся 100. Не отработал cachableMethod?");

        // Вызываем не кэшируемый метод, он должен отработать
        Assertions.assertDoesNotThrow(() -> proxyObject.simpleMethod(), "Не удалось на прокси объекта вызвать не аннотированный метод simpleMethod");

        // Вызываем на прокси кэшируемый метод, для проверки того что он пройдёт вхолостую
        Assertions.assertDoesNotThrow(() -> proxyObject.cachableMethod(), "Не удалось на прокси объекта вызвать cachableMethod 2");

        // По не изменившемуся значению calcData, проверяем что прокси заглушил вызов кэшируемого метода
        Assertions.assertEquals(100, cachableObject.calcData, "caldData должна равнятся 100. Прокси не сработал, вызвал cachableMethod исходного объекта, хотя не должен был");

        // Устанавливаем новое значение sourceData через вызов setterMethod, которые считается меняющим состояние объекта
        Assertions.assertDoesNotThrow(() -> proxyObject.setterMethod(20), "Не удалось на прокси объекта вызвать setterMethod(20)");

        // Вызываем на прокси кэшируемый метод, в этот раз он должен отработать, т.к. ему предшествовал вызов setterMethod
        Assertions.assertDoesNotThrow(() -> proxyObject.cachableMethod(), "Не удалось на прокси объекта вызвать cachableMethod 3");

        // По изменившемуся значению calcData, проверяем что прокси вызвал кэшируемый метод
        Assertions.assertEquals(400, cachableObject.calcData, "caldData должна равнятся 400. Прокси сработал не корректно, т.к. должен был вызвать cachableMethod исходного объекта");
    }
}

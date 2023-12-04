package vtb.courses.stage2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        CachableClass srcObject = new CachableClass();
        Cachable cachableClass = CacheUtils.cache(srcObject);
        System.out.println("Вызов cachableMethod");
        cachableClass.cachableMethod();
        System.out.println("Вызов cachableMethod");
        cachableClass.cachableMethod();
        System.out.println("Вызов метода меняющего состояние");
        cachableClass.setterMethod();
        System.out.println("Вызов cachableMethod");
        cachableClass.cachableMethod();
        System.out.println("Вызов simpleMethod");
        cachableClass.simpleMethod();
    }
}

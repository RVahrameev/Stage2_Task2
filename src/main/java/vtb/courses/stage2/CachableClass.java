package vtb.courses.stage2;

public class CachableClass implements Cachable{

    public void cachableMethod() {
        System.out.println("cachableMethod executed");
    }

    public void setterMethod(Object object) {
        System.out.println("Object state changed!");
    }
}

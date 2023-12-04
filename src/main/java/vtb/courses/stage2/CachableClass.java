package vtb.courses.stage2;

public class CachableClass implements Cachable{
    public void cachableMethod() {
        System.out.println("        cachableMethod executed");
    }

    public void setterMethod(){
        System.out.println("        setterMethod executed");
    }

    public void simpleMethod() {
        System.out.println("        simpleMethod executed");
    }
}

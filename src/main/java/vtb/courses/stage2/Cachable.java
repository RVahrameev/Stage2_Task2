package vtb.courses.stage2;

public interface Cachable {
    @Cache
    long cachableMethod();

    @Setter
    void setterMethod(Object object);

    void simpleMethod();
}

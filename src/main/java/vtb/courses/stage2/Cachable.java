package vtb.courses.stage2;

public interface Cachable {
    @Cache
    void cachableMethod();

    @Setter
    void setterMethod(Object object);

    void simpleMethod();
}

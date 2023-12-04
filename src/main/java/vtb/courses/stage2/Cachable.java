package vtb.courses.stage2;

public interface Cachable {
    @Cache
    void cachableMethod();

    @Setter
    void setterMethod();

    void simpleMethod();
}

package ResponsiPBO;

public abstract class Data {
    String id;
    String nama;
    String email;
    String password;

    abstract void detailData();

    abstract void resetData();

    abstract void tampilTabel();
}

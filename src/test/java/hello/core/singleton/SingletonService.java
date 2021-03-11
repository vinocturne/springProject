package hello.core.singleton;

public class SingletonService {
    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }
    //생성자를 private으로 설정해서 외부에서 new 키워드로 생성할 수 없도록 만든다.
    //무조건 꺼내 쓸 때에는 getInstance로만 꺼내 쓸 수 있도록 한다.
    private SingletonService() {

    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }


}

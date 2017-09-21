package springboot.demo;

public class RealSubject implements Subject {
    @Override
    public void send() {
        System.out.println("I want to rent my house");
    }

    @Override
    public void hello(String str) {
        System.out.println("hello: " + str);
    }
}

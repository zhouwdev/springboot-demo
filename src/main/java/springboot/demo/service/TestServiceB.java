package springboot.demo.service;

import org.springframework.stereotype.Service;

@Service
public class TestServiceB implements ITestService {
    @Override
    public void test(String text) {
        System.out.println(this.getClass().getName() + "---" + text);
    }

    @Override
    public Integer getType() {
        return 2;
    }
}

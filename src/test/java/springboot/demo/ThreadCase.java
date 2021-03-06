package springboot.demo;

import org.assertj.core.util.Compatibility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhouwei on 2017/5/27.
 */
public class ThreadCase {

    private CountDownLatch startSignal = new CountDownLatch(1);//开始阀门
    private CountDownLatch doneSignal = null;//结束阀门
    private CopyOnWriteArrayList<Long> list = new CopyOnWriteArrayList<Long>();
    private AtomicInteger err = new AtomicInteger();//原子递增
    private ConcurrentTask[] task = null;

    public ThreadCase(ConcurrentTask... task) {
        this.task = task;
        if (task == null) {
            System.out.println("task can not null");
            System.exit(1);
        }
        doneSignal = new CountDownLatch(task.length);
        start();
    }

    private void start() {


        try {
            //创建线程，并将所有线程等待在阀门处
            createThread();
            //打开阀门
            startSignal.countDown();//递减锁存器的计数，如果计数到达零，则释放所有等待的线程
            doneSignal.await();//等待所有线程都执行完毕
            System.out.println("all done!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //计算执行时间
        getExeTime();
    }

    /**
     * 初始化所有线程，并在阀门处等待
     */
    private void createThread() {
        long len = doneSignal.getCount();
        for (int i = 0; i < len; i++) {
            final int j = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        startSignal.await();//使当前线程在锁存器倒计数至零之前一直等待
                        long start = System.currentTimeMillis();
                        task[j].run();
                        long end = (System.currentTimeMillis() - start);
                        list.add(end);
                    } catch (Exception e) {
                        err.getAndIncrement();//相当于err++
                    }
                    doneSignal.countDown();
                }
            }).start();
        }
    }

    /**
     * 计算平均响应时间
     */
    private void getExeTime() {
        int size = list.size();
        List<Long> _list = new ArrayList<Long>(size);
        _list.addAll(list);
        Collections.sort(_list);
        long min = _list.get(0);
        long max = _list.get(size - 1);
        long sum = 0L;
        for (Long t : _list) {
            sum += t;
        }
        long avg = sum / size;
        System.out.println("min: " + min);
        System.out.println("max: " + max);
        System.out.println("avg: " + avg);
        System.out.println("err: " + err.get());
    }

    public interface ConcurrentTask {
        void run();
    }

    public static void main(String[] args) {
       /* ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));*/
        ExecutorService es = Executors.newCachedThreadPool();

        for(int i=0;i<15;i++){
            MyTask myTask = new MyTask(i);
            es.execute(myTask);
          /*  System.out.println("线程池中线程数目："+es.getPoolSize()+"，队列中等待执行的任务数目："+
                    es.getQueue().size()+"，已执行玩别的任务数目："+es.getCompletedTaskCount());*/
        }
        es.shutdown();
    }
}

class MyTask implements Runnable {
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
    }
}

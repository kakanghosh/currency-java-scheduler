package com.concurrent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public class RequestExecutor implements Runnable {
    final BlockingQueue<Request> blockingQueue;
    final BlockingQueue<Request> responseQueue;

    final Timer timer;
    public RequestExecutor(BlockingQueue<Request> blockingQueue, BlockingQueue<Request> responseQueue) {
        this.blockingQueue = blockingQueue;
        this.responseQueue = responseQueue;
        timer = new Timer();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (responseQueue.isEmpty()) {
                    final Request message = blockingQueue.take();
                    System.out.println("Message is going to process: "+message);
                    message.expire = System.currentTimeMillis() + 2000L;
                    responseQueue.put(message);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (message.isExpired() && responseQueue.remove(message)) {
                                System.out.println("RequestExecutor: Time out: " + message);
                            }
                        }
                    }, 2200);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.concurrent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ResponseHandler implements Runnable {
    Logger logger = Logger.getLogger(ResponseHandler.class.getName());
    final BlockingQueue<Request> responseQueue;
    final Random random;

    final ConcurrentHashMap<Integer, Request> concurrentHashMap;

    final Timer timer;

    public ResponseHandler(BlockingQueue<Request> responseQueue) {
        this.responseQueue = responseQueue;
        random = new Random();
        concurrentHashMap = new ConcurrentHashMap<>();
        timer = new Timer();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final Request request = responseQueue.take();
                if (request.isMultiple) {
                    if (concurrentHashMap.containsKey(request.id)) {
                        final Request firstRequest = concurrentHashMap.get(request.id);
                        System.out.println("Taken out response multi: " + firstRequest + " " + request);
                        concurrentHashMap.remove(request.id);
                    } else {
                        concurrentHashMap.put(request.id, request);
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                concurrentHashMap.remove(request.id);
                            }
                        }, 2000);
                    }
                } else {
                    System.out.println("Taken out response: " + request);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.concurrent;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = Logger.getLogger(Main.class.getName());

        final BlockingQueue<Request> messageQueue = new ArrayBlockingQueue<>(100);
        final BlockingQueue<Request> messageResponseQueue = new ArrayBlockingQueue<>(1);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        final RequestExecutor requestExecutor = new RequestExecutor(messageQueue, messageResponseQueue);
        final RequestHandler requestHandler = new RequestHandler(messageQueue);
        final ResponseHandler responseHandler = new ResponseHandler(messageResponseQueue);

        executorService.execute(requestExecutor);
        executorService.execute(requestHandler);
        executorService.execute(responseHandler);

        executorService.shutdown();
        boolean result = executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.MICROSECONDS);
    }
}

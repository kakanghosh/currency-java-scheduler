package com.concurrent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class RequestHandler implements Runnable {

    Logger logger = Logger.getLogger(RequestHandler.class.getName());
    final BlockingQueue<Request> blockingQueue;
    final Random random;

    public RequestHandler(BlockingQueue<Request> blockingQueue) {
        this.blockingQueue = blockingQueue;
        random = new Random();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(200);
                final int id = random.nextInt(1, 20);
                final boolean isEven = id % 2 == 0;
                final Request request = new Request(id, isEven, isEven ? 0 : 1, isEven ? 1 : -1);
                if (blockingQueue.contains(request)) {
                    logger.warning("Request is already exist for: " + request);
                } else if (request.isMultiple) {
                    final Request firstRequest = new Request(request.id, true, request.channel1, -1, "First Part");
                    final Request secondRequest = new Request(request.id, true, request.channel2, -1, "Second Part");
                    System.out.println("Request put: " + request);
                    blockingQueue.put(firstRequest);
                    blockingQueue.put(secondRequest);
                } else {
                    blockingQueue.put(request);
                    System.out.println("Request put: " + request);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

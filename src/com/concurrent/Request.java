package com.concurrent;

import java.util.Objects;

public class Request {
    int id;
    boolean isMultiple;
    int channel1;
    int channel2;

    String additionalMessage;

    long expire;

    public Request(int id, boolean isMultiple, int channel1, int channel2) {
        this.id = id;
        this.isMultiple = isMultiple;
        this.channel1 = channel1;
        this.channel2 = channel2;
    }

    public Request(int id, boolean isMultiple, int channel1, int channel2, String additionalMessage) {
        this(id, isMultiple, channel1, channel2);
        this.additionalMessage = additionalMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Request) {
            return Objects.equals(id, ((Request) obj).id);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", isMultiple=" + isMultiple +
                ", channel1=" + channel1 +
                ", channel2=" + channel2 +
                ", additionalMessage='" + additionalMessage + '\'' +
                '}';
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expire;
    }
}

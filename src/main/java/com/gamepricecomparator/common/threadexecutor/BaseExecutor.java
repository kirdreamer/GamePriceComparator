package com.gamepricecomparator.common.threadexecutor;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseExecutor {
    protected Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public abstract void start();

    public void interrupt() {
        setRunning(false);
        thread.interrupt();
    }

    boolean isRunning() {
        return running.get();
    }

    void setRunning(boolean isRunning) {
        running.set(isRunning);
    }
}

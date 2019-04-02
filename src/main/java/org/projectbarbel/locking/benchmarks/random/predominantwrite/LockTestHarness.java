package org.projectbarbel.locking.benchmarks.random.predominantwrite;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Consumer;

import io.github.benas.randombeans.api.EnhancedRandom;

public class LockTestHarness {

    protected Runnable injectedReadOperation;
    protected Runnable injectedWriteOperation;
    
    protected final Consumer<Integer> read = new Consumer<Integer>() {
        @Override
        public void accept(Integer duration) {
            lock.readLock().lock();
            try {
                sleepSilent(duration);
                injectedReadOperation.run();
            } finally {
                lock.readLock().unlock();
            }
        }
    };

    protected final Consumer<Integer> write = new Consumer<Integer>() {
        @Override
        public void accept(Integer duration) {
            lock.writeLock().lock();
            try {
                sleepSilent(duration);
                injectedWriteOperation.run();
            } finally {
                lock.writeLock().unlock();
            }
        }
    };

    protected int[] idletime;
    protected int[] processingtime;
    protected Consumer<Integer>[] workmix;

    protected final List<String> randomStrings = EnhancedRandom.randomListOf(10, String.class);

    protected ReadWriteLock lock;

    public final ThreadLocal<Object> publish = new ThreadLocal<Object>();


    protected int[] getRandomNumberInRange(int quantity, int min, int max) {
        Random r = new Random();
        int[] result = new int[quantity];
        for (int i = 0; i < quantity; i++) {
            result[i] = r.nextInt((max - min) + 1) + min;
        }
        return result;
    }

    protected void sleepSilent(Integer duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }
    
    protected void doMeasure() {
        int[] randomNumbers = getRandomNumberInRange(3, 0, 9);
        sleepSilent(idletime[randomNumbers[0]]);
        workmix[randomNumbers[1]].accept(processingtime[randomNumbers[2]]);
    }
    
}

package org.projectbarbel.locking.benchmarks.random.predominantread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Random realistic batch mix performance.
 * 
 */
@State(Scope.Benchmark)
public class LockBenchmark_Random_PredominantRead_ReentrantReadWriteLock_Batch extends LockTestHarness {

    @SuppressWarnings("unchecked")
    public LockBenchmark_Random_PredominantRead_ReentrantReadWriteLock_Batch() {
        idletime = new int[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        processingtime = new int[]{ 10, 10, 20, 20, 40, 40, 80, 80, 100, 100 };
        workmix = new Consumer[] { read, read, read, read, write, write, read, read,
                read, read };
        
        lock = new ReadWriteLock() {
            private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
            @Override
            public Lock writeLock() {
                return lock.writeLock();
            }
            @Override
            public Lock readLock() {
                return lock.readLock();
            }
        };
        
        injectedReadOperation = new Runnable() {
            
            @Override
            public void run() {
                int[] index = getRandomNumberInRange(1, 0, 9);
                publish.set(randomStrings.get(index[0]));
            }
        };
        
        injectedWriteOperation = new Runnable() {
            
            @Override
            public void run() {
                int[] index = getRandomNumberInRange(2, 0, 9);
                String value = randomStrings.get(index[0]);
                randomStrings.set(index[1], value);
            }
        };
    }
    
    @Benchmark
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations=5, time=5, timeUnit=TimeUnit.SECONDS)
    @Measurement(iterations=5, time=5, timeUnit=TimeUnit.SECONDS)
    @Threads(1)
    public void measureThroughput_singleThread() {
        doMeasure();
    }
    
    @Benchmark
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations=5, time=5, timeUnit=TimeUnit.SECONDS)
    @Measurement(iterations=5, time=5, timeUnit=TimeUnit.SECONDS)
    @Threads(2)
    public void measureThroughput_twoThreads() {
        doMeasure();
    }
    
}

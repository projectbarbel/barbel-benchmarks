package org.projectbarbel.locking.benchmarks;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import io.github.benas.randombeans.api.EnhancedRandom;

/**
 * Whats the througput on highly concurrent read and write operations with {@link ReentrantLock}?
 * What's the impact on increasing count of highly concurrent reader threads?
 *
 */
public class Verfification_Benchmark {

    @State(Scope.Group)
    public static class BenchmarkState {
        private final List<String> randomStrings = EnhancedRandom.randomListOf(100, String.class);
        private final Lock lock = new ReentrantLock();
        private final Lock readlock = lock;
        private final Lock writelock = lock;
        public ThreadLocal<Object> publish = new ThreadLocal<Object>();
        private AtomicInteger counter = new AtomicInteger();

        @Setup
        public void setup() {
            System.out.println("Counter on setup: " + counter.get());
        }

        @TearDown
        public void tearDown() {
            System.out.println("Counter on teardown: " + counter.get());
        }
        
        public BenchmarkState() {
        }
    }

    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("a")
    @GroupThreads(1)
    @Warmup(time=1)
    @Measurement(time=1)
    public void a_measureThroughput_fastreader_1(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.readlock.lock();
        try {
            int index = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            state.publish.set(state.randomStrings.get(index));
        } finally {
            state.readlock.unlock();
        }
    }

    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("a")
    @GroupThreads(1)
    @Warmup(time=1)
    @Measurement(time=1)
    public void a_measureThroughput_fastwriter_1(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.writelock.lock();
        try {
            int readIndex = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            int writeIndex = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            String value = state.randomStrings.get(readIndex);
            state.randomStrings.set(writeIndex, value);
        } finally {
            state.writelock.unlock();
        }
    }

    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("b")
    @GroupThreads(1)
    @Warmup(time=1)
    @Measurement(time=1)
    public void b_measureThroughput_fastreader_1(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.readlock.lock();
        try {
            int index = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            state.publish.set(state.randomStrings.get(index));
        } finally {
            state.readlock.unlock();
        }
    }
    
    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("b")
    @GroupThreads(1)
    @Warmup(time=1)
    @Measurement(time=1)
    public void b_measureThroughput_fastreader_2(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.readlock.lock();
        try {
            int index = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            state.publish.set(state.randomStrings.get(index));
        } finally {
            state.readlock.unlock();
        }
    }
    
    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("b")
    @GroupThreads(1)
    @Warmup(time=1)
    @Measurement(time=1)
    public void b_measureThroughput_fastwriter_1(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.writelock.lock();
        try {
            int readIndex = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            int writeIndex = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            String value = state.randomStrings.get(readIndex);
            state.randomStrings.set(writeIndex, value);
        } finally {
            state.writelock.unlock();
        }
    }
    
    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("c")
    @GroupThreads(4)
    @Warmup(time=1)
    @Measurement(time=1)
    public void c_measureThroughput_fastreader_1_4threads(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.readlock.lock();
        try {
            int index = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            state.publish.set(state.randomStrings.get(index));
        } finally {
            state.readlock.unlock();
        }
    }
    
    @Benchmark
    @Fork(2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Group("c")
    @GroupThreads(1)
    @Warmup(time=1)
    @Measurement(time=1)
    public void c_measureThroughput_fastwriter_1_1thread(BenchmarkState state) throws InterruptedException {
        state.counter.incrementAndGet();
        state.writelock.lock();
        try {
            int readIndex = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            int writeIndex = PersonBenchmarkState.getRandomNumberInRange(0, 99);
            String value = state.randomStrings.get(readIndex);
            state.randomStrings.set(writeIndex, value);
        } finally {
            state.writelock.unlock();
        }
    }
    
}

package org.projectbarbel;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.projectbarbel.histo.BarbelHisto;
import org.projectbarbel.histo.BarbelHistoBuilder;
import org.projectbarbel.histo.BarbelHistoCore;
import org.projectbarbel.histo.BarbelMode;
import org.projectbarbel.histo.model.BitemporalStamp;
import org.projectbarbel.histo.model.DefaultDocument;

public class BarbelHistoCore_Throughput_MultiThreaded {

    @State(Scope.Benchmark)
    public static class BarbelHistoState {
        @Setup
        public void setup() {
            System.out.println("setup");
        }
        @TearDown
        public void tearDown() {
            System.out.println(((BarbelHistoCore<DefaultDocument>)histo).size());
        }
        private BarbelHisto<DefaultDocument> histo = BarbelHistoBuilder.barbel().withMode(BarbelMode.BITEMPORAL).build();;
    }

    
    @Benchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Threads(4)
    public void measureThroughput(BarbelHistoState state) throws InterruptedException {
        state.histo.save(new DefaultDocument(UUID.randomUUID().toString(), BitemporalStamp.createActive(), "data"), LocalDate.now(), LocalDate.MAX);
    }
    
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BarbelHistoCore_Throughput_MultiThreaded.class.getSimpleName())
                .mode(Mode.Throughput)
                .build();

        new Runner(opt).run();
    }

}

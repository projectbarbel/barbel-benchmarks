package org.projectbarbel;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.projectbarbel.histo.BarbelHisto;
import org.projectbarbel.histo.BarbelHistoBuilder;
import org.projectbarbel.histo.BarbelMode;
import org.projectbarbel.histo.model.BitemporalStamp;
import org.projectbarbel.histo.model.DefaultDocument;

public class BarbelHistoCore_Throughput {

    @State(Scope.Thread)
    public static class BarbelHistoState {
        @Setup
        public void setup() {
            System.out.println("setup");
        }
        private BarbelHisto<DefaultDocument> histo = BarbelHistoBuilder.barbel().withMode(BarbelMode.BITEMPORAL).build();;
    }

    
    @Benchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureThroughput(BarbelHistoState histo) throws InterruptedException {
        histo.histo.save(new DefaultDocument("id", BitemporalStamp.createActive(), "data"), LocalDate.now(), LocalDate.MAX);
    }
    
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BarbelHistoCore_Throughput.class.getSimpleName())
                .forks(1)
                .mode(Mode.Throughput)
                .build();

        new Runner(opt).run();
    }

}

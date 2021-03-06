package reactor.examples.create.model;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyEventProcessorImpl implements MyEventProcessor {
    private MyEventListener<String> eventListener;
    private final ScheduledExecutorService executor;

    public MyEventProcessorImpl(){
        this.executor =  Executors.newSingleThreadScheduledExecutor();
    }

    public MyEventProcessorImpl(int threadNumber){
        this.executor =  Executors.newScheduledThreadPool(threadNumber);
    }

    @Override
    public void register(MyEventListener<String> eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void dataChunk(String... values) {
        executor.schedule(() -> eventListener.onDataChunk(Arrays.asList(values)),
                500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void processComplete() {
        executor.schedule(() -> eventListener.processComplete(),
                500, TimeUnit.MILLISECONDS);
    }
}

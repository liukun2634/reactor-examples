package reactor.examples.create;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.examples.create.model.MyEventListener;
import reactor.examples.create.model.MyEventProcessor;
import reactor.examples.create.model.MyEventProcessorImpl;
import reactor.examples.util.LoggerUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateExamples {
    private final static Logger logger = LoggerFactory.getLogger(CreateExamples.class);

    //Flux.create(): Asynchronous and Multi-threaded

    @Test
    public void useCreate() {
        MyEventProcessor myEventProcessor = new MyEventProcessorImpl(3);

        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register(
                    new MyEventListener<>() {
                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                                sleep(500);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });
        });

        bridge.subscribe(data -> LoggerUtil.logInfo(logger, data));

        myEventProcessor.dataChunk("foo", "bar", "bazz");
        sleep(500);
        myEventProcessor.dataChunk("foo2", "bar2", "bazz2");
        sleep(500);
        myEventProcessor.dataChunk("foo3", "bar3", "bazz3");

        sleep(2000);
        myEventProcessor.processComplete();
    }

    private void sleep(int milliSeconds){
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

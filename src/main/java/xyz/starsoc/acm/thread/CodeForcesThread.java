package xyz.starsoc.acm.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CodeForcesThread {

    private Logger logger = LoggerFactory.getLogger("CodeForcesThread");

    public void run() {

        Runnable runnable = () -> {



        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 20, 60, TimeUnit.SECONDS);
    }
}

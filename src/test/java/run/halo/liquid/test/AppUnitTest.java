package run.halo.liquid.test;

import org.junit.jupiter.api.Test;
import run.halo.liquid.utils.CPUWatchDog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Dioxide.CN
 * @date 2023/5/22 23:34
 * @since 1.0
 */
public class AppUnitTest {

    CPUWatchDog watchDog = new CPUWatchDog();

    @Test
    public void cpuUnitTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println("[" + currentTime.format(formatter) + " CPU-Monitor]" + Arrays.toString(watchDog.getBatchCPULoad()));
        }, 0, 3, TimeUnit.SECONDS);
        while (true) {}
    }

}

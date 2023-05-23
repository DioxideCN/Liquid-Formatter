package run.halo.liquid.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

/**
 * @author Dioxide.CN
 * @date 2023/5/22 23:52
 * @since 1.0
 */
public class CPUWatchDog {
    static {
        NativeLoader.loadNative(CPUWatchDog.class, "libCPUWatchDog.dll");
    }

    private final OperatingSystemMXBean osBean;

    public CPUWatchDog() {
        osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public int getNumberOfCores() {
        return osBean.getAvailableProcessors();
    }

    // native area

    public native double getCPULoad(int cpuIndex); // single cpu
    public native double[] getBatchCPULoad(); // cpu matrix
}


package de.kon.praxisarbeit;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import com.sun.management.OperatingSystemMXBean;


import java.lang.management.ManagementFactory;

@Deprecated
public class MemoryMonitor implements Runnable {


    private volatile boolean running = true;
    private double peakMB = 0;
    public void stop() {
        running = false;
    }
    public double getPeakMB() {
        return peakMB;
    }
    @Override

    public void run() {
        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        while (running) {
            // Committed Virtual Memory ≈ Native + Heap
            ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            long usedBytes = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
            usedBytes = usedBytes / 1_000;
            double usedMB = usedBytes / (1024.0 * 1024.0);
            if (usedMB > peakMB) peakMB = usedMB;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
    }


}
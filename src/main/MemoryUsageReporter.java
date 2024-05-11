package main;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryUsageReporter {
    private MemoryMXBean memoryBean;

    public MemoryUsageReporter() {
        memoryBean = ManagementFactory.getMemoryMXBean();
    }

    public void reportMemoryUsage() {
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        System.out.println("Heap Memory Usage:");
        System.out.println("   Initial: " + heapMemoryUsage.getInit() / (1024 * 1024) + " MB");
        System.out.println("   Used: " + heapMemoryUsage.getUsed() / (1024 * 1024) + " MB");
        System.out.println("   Committed: " + heapMemoryUsage.getCommitted() / (1024 * 1024) + " MB");
        System.out.println("   Max: " + heapMemoryUsage.getMax() / (1024 * 1024) + " MB");
    }
}

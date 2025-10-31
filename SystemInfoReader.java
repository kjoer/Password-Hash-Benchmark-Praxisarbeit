package de.kon.praxisarbeit;


public class SystemInfoReader {
    public static SysInfo readSystemInfo() {
        oshi.SystemInfo si = new oshi.SystemInfo();
        oshi.hardware.HardwareAbstractionLayer hal = si.getHardware();

        oshi.hardware.CentralProcessor processor = hal.getProcessor();
        String cpuName = processor.getProcessorIdentifier().getName();
        int cores = processor.getLogicalProcessorCount();
        long frequency = processor.getProcessorIdentifier().getVendorFreq() / 1_000_000;

        oshi.hardware.GlobalMemory memory = hal.getMemory();
        long totalMemory = memory.getTotal() / (1024 * 1024 * 1024);
        long availableMemory = memory.getAvailable() / (1024 * 1024 * 1024);

        return new SysInfo(cpuName, cores, frequency, totalMemory, availableMemory);
    }
}
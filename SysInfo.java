package de.kon.praxisarbeit;

public class SysInfo {
    private String processorName;
    private int processorCores;
    private long processorFrequencyMHz;
    private long totalMemoryGB;
    private long availableMemoryGB;

    public SysInfo(String processorName, int processorCores, long processorFrequencyMHz,
                   long totalMemoryGB, long availableMemoryGB) {
        this.processorName = processorName;
        this.processorCores = processorCores;
        this.processorFrequencyMHz = processorFrequencyMHz;
        this.totalMemoryGB = totalMemoryGB;
        this.availableMemoryGB = availableMemoryGB;
    }

    public String getProcessorName() { return processorName; }
    public int getProcessorCores() { return processorCores; }
    public long getProcessorFrequencyMHz() { return processorFrequencyMHz; }
    public long getTotalMemoryGB() { return totalMemoryGB; }
    public long getAvailableMemoryGB() { return availableMemoryGB; }

    @Override
    public String toString() {
        return String.format("CPU: %s (%d cores @ %d MHz), RAM: %d/%d GB",
                processorName, processorCores, processorFrequencyMHz,
                availableMemoryGB, totalMemoryGB);
    }
}

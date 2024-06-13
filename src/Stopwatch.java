public class Stopwatch {
    private int startTime = 0;
    private int endTime = 0;
    private boolean running;
    
    // Start a stopwatch
    public void start() {
        this.startTime = (int)(System.currentTimeMillis() / 1000);
        this.running = true;
    }

    // Stop a stopwatch
    public void stop() {
        this.endTime = (int)(System.currentTimeMillis() / 1000);
        this.running = false;
    }

    // Reset a stopwatch
    public void reset() {
        startTime = 0;
        endTime = 0;
    }

    // Get elapsed time in seconds
    public int getTimeInSeconds() {
        int timeInSeconds;
        if (running) {
            timeInSeconds = (int)(System.currentTimeMillis() / 1000) - startTime;
        }
        else {
            timeInSeconds = endTime - startTime;
        }
        return timeInSeconds;
    }

    //

}

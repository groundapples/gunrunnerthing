import java.security.Timestamp;

public class SlowmoTracker {
    private long timeSlowStart = 0;
    private double timeSlowDuration;
    private double slowAmount;
    private double activeSlowAmount = 1;

    SlowmoTracker(double timeSlowDuration, double slowAmount) {
        this.timeSlowDuration = timeSlowDuration;
        this.slowAmount = slowAmount;
    }

    public void activateSlow() {
        if (timeSlowStart == 0) {
            activeSlowAmount = slowAmount;
            timeSlowStart = System.currentTimeMillis();
        } else {
            activeSlowAmount = 1;
            timeSlowStart = 0;
        }
    }

    public double getActiveSlowAmount() {
        return activeSlowAmount;
    }
}

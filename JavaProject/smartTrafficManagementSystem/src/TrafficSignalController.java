import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TrafficSignalController {

    private final Rectangle redLight;
    private final Rectangle greenLight;
    private final Rectangle yellowLight;
    private final Label timerLabel;
    private int minSignalDuration = 30000; // Minimum duration for each light (30 seconds)
    private int[] signalDurations = {30000, 5000, 30000}; // Initial durations: red, yellow, green
    private int currentSignalIndex = 0; // 0: Red, 1: Yellow, 2: Green
    private Timeline timerUpdate; // Declare timerUpdate here

    public TrafficSignalController(Rectangle redLight, Rectangle greenLight, Rectangle yellowLight, Label timerLabel) {
        this.redLight = redLight;
        this.greenLight = greenLight;
        this.yellowLight = yellowLight;
        this.timerLabel = timerLabel;
        updateLights();
    }

    public void startSignalCycle() {
        runSignalCycle();
    }

    private void runSignalCycle() {
        int currentDuration = Math.max(signalDurations[currentSignalIndex], minSignalDuration);

        Timeline signalTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(currentDuration),
                        event -> {
                            currentSignalIndex = (currentSignalIndex + 1) % 3;
                            updateLights();
                            startTimer(currentDuration / 1000); // Pass the duration in seconds
                            runSignalCycle();
                        }
                )
        );
        signalTimeline.setCycleCount(Timeline.INDEFINITE);
        signalTimeline.play();
    }

    private void updateLights() {
        redLight.setFill(Color.GRAY);
        yellowLight.setFill(Color.GRAY);
        greenLight.setFill(Color.GRAY);

        if (currentSignalIndex == 0) {
            redLight.setFill(Color.RED);
        } else if (currentSignalIndex == 1) {
            yellowLight.setFill(Color.YELLOW);
        } else {
            greenLight.setFill(Color.GREEN);
        }
    }

    private void startTimer(int duration) {
        final int[] remainingTime = {duration};

        timerUpdate = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTime[0]--;
            timerLabel.setText("Time Remaining: " + remainingTime[0] + "s");
            if (remainingTime[0] <= 0) {
                timerUpdate.stop();
            }
        }));

        timerUpdate.setCycleCount(duration);
        timerUpdate.play();
    }

    public void adjustSignalBasedOnTraffic(int trafficDensity) {
        if (trafficDensity > 80) {
            signalDurations[2] = 45000; // Increase green light duration
        } else if (trafficDensity > 50) {
            signalDurations[2] = 30000; // Standard green light duration
        } else {
            signalDurations[2] = 15000; // Decrease green light duration
        }
    }
}

















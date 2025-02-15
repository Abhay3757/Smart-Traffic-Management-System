import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TrafficSimulator extends Application {

    private TrafficSignalController controller;
    private Label trafficDensityLabel;
    private Label routeSuggestionLabel;
    private Label roadConditionLabel;
    private Label timerLabel;
    private Label driverInputLabel;
    private TextField roadConditionInput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create rectangles for the traffic lights
        Rectangle redLight = new Rectangle(100, 100, Color.GRAY);
        redLight.setStroke(Color.BLACK);
        redLight.setStrokeWidth(2);

        Rectangle yellowLight = new Rectangle(100, 100, Color.GRAY);
        yellowLight.setStroke(Color.BLACK);
        yellowLight.setStrokeWidth(2);

        Rectangle greenLight = new Rectangle(100, 100, Color.GRAY);
        greenLight.setStroke(Color.BLACK);
        greenLight.setStrokeWidth(2);

        // Headings for traffic lights and map
        Label trafficLightHeading = new Label("Traffic Lights");
        trafficLightHeading.setFont(new Font("Arial", 20));
        trafficLightHeading.setStyle("-fx-padding: 10px;");

        // Stack the traffic lights vertically
        VBox trafficLightBox = new VBox(10, trafficLightHeading, redLight, yellowLight, greenLight);
        trafficLightBox.setStyle("-fx-alignment: center; -fx-padding: 20px; -fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 5px;");

        timerLabel = new Label("Time Remaining: 30s");
        timerLabel.setFont(new Font("Arial", 16));

        // Labels and inputs for user data
        Label roadConditionLabelTitle = new Label("Road Condition:");
        roadConditionInput = new TextField("Clear");
        roadConditionInput.setPadding(new javafx.geometry.Insets(5));
        roadConditionInput.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: white;");

        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5px 10px;");
        updateButton.setOnAction(event -> updateDriverInput());

        // Labels to display simulated data
        trafficDensityLabel = new Label("Traffic Density: 0%");
        routeSuggestionLabel = new Label("Route Suggestion: None");
        roadConditionLabel = new Label("Road Condition: Clear");
        driverInputLabel = new Label(""); // Label for driver input messages

        // Style the labels
        trafficDensityLabel.setFont(new Font("Arial", 16));
        routeSuggestionLabel.setFont(new Font("Arial", 16));
        roadConditionLabel.setFont(new Font("Arial", 16));
        driverInputLabel.setFont(new Font("Arial", 16));
        driverInputLabel.setStyle("-fx-text-fill: blue; -fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");

        roadConditionLabelTitle.setFont(new Font("Arial", 16));
        roadConditionLabelTitle.setStyle("-fx-padding: 5px;");

        // Create a map image with traffic and car symbols
        Image mapImage = new Image("file:C:/Users/abhaa/JavaProject/istockphoto-1306807452-612x612.jpg");
        ImageView mapView = new ImageView(mapImage);
        mapView.setFitWidth(600);
        mapView.setPreserveRatio(true);

        // Overlay symbols on the map
        Image trafficSymbol = new Image("file:C:/Users/abhaa/JavaProject/png-clipart-computer-icons-traffic-light-symbol-traffic-light-driving-traffic-sign.png");
        Image carSymbol = new Image("file:C:/Users/abhaa/JavaProject/images.png");
        ImageView trafficSymbolView = new ImageView(trafficSymbol);
        ImageView carSymbolView = new ImageView(carSymbol);

        // Position symbols centrally on the map
        trafficSymbolView.setFitWidth(50);
        trafficSymbolView.setPreserveRatio(true);
        trafficSymbolView.setTranslateX(-250); // Adjusted to move the traffic symbol right
        trafficSymbolView.setTranslateY(-150); // Adjusted to lower the traffic symbol

        carSymbolView.setFitWidth(50);
        carSymbolView.setPreserveRatio(true);
        carSymbolView.setTranslateX(100); // Adjusted to move the car symbol left
        carSymbolView.setTranslateY(-50); // Adjusted to move the car symbol up

        StackPane mapPane = new StackPane(mapView, trafficSymbolView, carSymbolView);

        // Heading for the map
        Label mapHeading = new Label("Chennai Location");
        mapHeading.setFont(new Font("Arial", 20));
        mapHeading.setStyle("-fx-padding: 10px;");

        VBox mapBox = new VBox(10, mapHeading, mapPane);
        mapBox.setStyle("-fx-padding: 20px; -fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 5px;");

        // Set up the layout
        HBox topBox = new HBox(10, new VBox(10, roadConditionLabelTitle, roadConditionInput, updateButton));
        topBox.setStyle("-fx-padding: 10px;");

        VBox statusBox = new VBox(10, timerLabel, trafficDensityLabel, routeSuggestionLabel, roadConditionLabel);
        statusBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px;");

        VBox driverMessageBox = new VBox(10, driverInputLabel); // Box for driver input messages
        driverMessageBox.setStyle("-fx-padding: 10px; -fx-alignment: top-right;");

        // Layout arrangement
        HBox centerBox = new HBox(20, trafficLightBox, mapBox); // Place traffic lights and map side by side
        centerBox.setStyle("-fx-alignment: center; -fx-padding: 20px;");

        BorderPane root = new BorderPane();
        root.setTop(topBox); // Inputs and buttons at the top
        root.setLeft(statusBox); // Status updates on the left
        root.setCenter(centerBox); // Traffic lights and map side by side in the center
        root.setRight(driverMessageBox); // Driver input messages on the right

        Scene scene = new Scene(root, 1200, 800); // Adjusted size for better layout

        primaryStage.setTitle("Smart Traffic Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        controller = new TrafficSignalController(redLight, greenLight, yellowLight, timerLabel);
        controller.startSignalCycle();

        // Update traffic density, route suggestion, and road conditions
        javafx.animation.Timeline updateTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(10), event -> updateTrafficData()));
        updateTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        updateTimeline.play();
    }

    private void updateTrafficData() {
        int trafficDensity = (int) (Math.random() * 101); // Randomize traffic density
        String[] routes = {"Route A", "Route B", "Route C"};
        String routeSuggestion = routes[new java.util.Random().nextInt(routes.length)];
        String simulatedRoadCondition = simulateRoadCondition();

        javafx.application.Platform.runLater(() -> {
            trafficDensityLabel.setText("Traffic Density: " + trafficDensity + "%");
            routeSuggestionLabel.setText("Route Suggestion: " + routeSuggestion);
            roadConditionLabel.setText("Road Condition: " + simulatedRoadCondition);
        });
    }

    private String simulateRoadCondition() {
        // Simulate random road conditions
        String[] conditions = {"Clear", "Slippery", "Under Construction"};
        return conditions[new java.util.Random().nextInt(conditions.length)];
    }

    private void updateDriverInput() {
        String roadCondition = roadConditionInput.getText().trim();

        javafx.application.Platform.runLater(() -> {
            driverInputLabel.setText("user123: Road Condition: " + roadCondition);
        });
    }
}























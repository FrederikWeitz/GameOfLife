package somejavafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameOfLife extends Application {
	
	private int canvasX = 400;
	private int canvasY = 400;
	private int fieldX = 20;
	private int fieldY = 20;
	private int stepMillis = 200;
	
	// Pilzfeld
	MushRoomField field;
	
	// Timeline
	Timeline timeline;
	Canvas canvas;
	
	// grafische Elemente
	private ImageView startImage = new ImageView(new Image("/images/control_play_blue.png"));
	private ImageView stopImage = new ImageView(new Image("/images/control_pause_blue.png"));
	
	HBox root = new HBox();
	Button startButton = new Button();
	
	private Border startBorder = new Border(new BorderStroke(Color.YELLOWGREEN, BorderStrokeStyle.SOLID,
			new CornerRadii(2), new BorderWidths(2)));
	private Border pauseBorder = new Border(new BorderStroke(Color.ROYALBLUE, BorderStrokeStyle.SOLID,
			new CornerRadii(2), new BorderWidths(2)));
	private Background startBackground = new Background(new BackgroundFill(Color.PALEGREEN, 
			new CornerRadii(2), new Insets(2)));
	private Background pauseBackground = new Background(new BackgroundFill(Color.LIGHTSKYBLUE, 
			new CornerRadii(2), new Insets(2)));
	private Font smallLabelFont = new Font(10);
	private Font bigFieldFont = new Font(18);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Scene
		Scene scene = new Scene(root, canvasX+204, canvasY+4);
		
		// Canvas
		Pane canvasPane = new Pane();
		canvasPane.setBorder(new Border(new BorderStroke(Color.ALICEBLUE, BorderStrokeStyle.SOLID, 
				CornerRadii.EMPTY, new BorderWidths(2))));
		canvasPane.setPrefSize(canvasX+4, canvasY+4);
		
		canvas = new Canvas(canvasX,canvasY);
		root.getChildren().add(canvasPane);
		canvasPane.getChildren().add(canvas);
		canvas.setTranslateX(2);
		canvas.setTranslateY(2);
		
		// Controll Elements
		VBox control = new VBox(5);
		root.getChildren().add(control);
		control.setPadding(new Insets(2));
		control.setAlignment(Pos.TOP_RIGHT);
		
		// StartButton
		startButton.setGraphic(startImage);
		startButton.setPrefSize(200, 40);
		startButton.setBorder(pauseBorder);
		startButton.setBackground(pauseBackground);
		control.getChildren().addAll(startButton);
		
		//
		Button randomButton = new Button("Zufallsverteilung");
		randomButton.setGraphic(new ImageView(new Image("/images/setting_tools.png")));
		randomButton.setPrefSize(200, 40);
		randomButton.setBorder(pauseBorder);
		randomButton.setBackground(pauseBackground);
		control.getChildren().addAll(randomButton);
		
		// Pilze hinzufügen
		HBox addButtonBox = new HBox(5);
		TextField addField = new TextField("10");
		addField.setPrefHeight(40);
		addField.setPrefWidth(150);
		addField.setFont(bigFieldFont);
		Button addButton = new Button();
		addButton.setGraphic(new ImageView(new Image("/images/add.png")));
		addButtonBox.getChildren().addAll(addField, addButton);
		Label addButtonLabel = new Label("neue Pilze hinzufügen");
		addButtonLabel.setPrefWidth(200);
		addButtonLabel.setFont(smallLabelFont);
		addButtonLabel.setAlignment(Pos.CENTER);
		addButtonLabel.setTranslateY(-6);
		control.getChildren().addAll(addButtonBox, addButtonLabel);
		
		// Slider
		Slider timeSlider = new Slider(2, 1000, 200);
		timeSlider.setOrientation(Orientation.HORIZONTAL);
		Label sliderLabel = new Label("Geschwindigkeit in Millisekunden: 200");
		sliderLabel.setPrefWidth(200);
		sliderLabel.setAlignment(Pos.CENTER);
		sliderLabel.setFont(smallLabelFont);
		sliderLabel.setTranslateY(-6);
		control.getChildren().addAll(timeSlider,sliderLabel);
		
		// Canvas Size
		HBox canvasSizeBox = new HBox(5);
		canvasSizeBox.setPadding(new Insets(5));
		ToggleGroup toggleSizeGroup = new ToggleGroup();
		RadioButton smallCanvas = new RadioButton("400*400");
		RadioButton middleCanvas = new RadioButton("600*600");
		RadioButton bigCanvas = new RadioButton("800*800");
		
		smallCanvas.setToggleGroup(toggleSizeGroup);
		middleCanvas.setToggleGroup(toggleSizeGroup);
		bigCanvas.setToggleGroup(toggleSizeGroup);
		
		smallCanvas.setFont(smallLabelFont);
		middleCanvas.setFont(smallLabelFont);
		bigCanvas.setFont(smallLabelFont);
		
		smallCanvas.setUserData(400);
		middleCanvas.setUserData(600);
		bigCanvas.setUserData(800);
		
		smallCanvas.setSelected(true);
		
		Label canvasSizeLabel = new Label("  Größe des Pilzfeldes");
		canvasSizeLabel.setFont(smallLabelFont);
		canvasSizeLabel.setTranslateY(10);
		canvasSizeLabel.setPrefWidth(200);
		canvasSizeLabel.setAlignment(Pos.BOTTOM_LEFT);
		
		canvasSizeBox.getChildren().addAll(smallCanvas,middleCanvas,bigCanvas);
		control.getChildren().addAll(canvasSizeLabel, canvasSizeBox);
		
		// Field Size
		Slider mushroomSlider = new Slider(10,100,20);
		mushroomSlider.setOrientation(Orientation.HORIZONTAL);
		mushroomSlider.setBlockIncrement(10);
		Label mushroomLabel = new Label("Rasterung des Feldes: 20");
		mushroomLabel.setFont(smallLabelFont);
		mushroomLabel.setTranslateY(-6);
		mushroomLabel.setPrefWidth(200);
		mushroomLabel.setAlignment(Pos.CENTER);
		
		control.getChildren().addAll(mushroomSlider,mushroomLabel);
		
		// MushroomField
		field = new MushRoomField();
		
		field.setCanvas(canvas);
		field.setFieldCoordinates(fieldX, fieldY);
		
		field.randomizeField();
		
		// Event Handling
		startButton.setOnAction((ActionEvent event) -> {
			if (timeline==null || timeline.statusProperty().getValue()==Animation.Status.STOPPED) {
				timelineCreate();
				timelinePlay();
			} else if (timeline.statusProperty().getValue()==Animation.Status.RUNNING) {
				timelinePause();
			} else if (timeline.statusProperty().getValue()==Animation.Status.PAUSED){
				timelinePlay();
			} 
		});
		
		addButton.setOnAction((ActionEvent event) -> {
			boolean t;
			try {
				t = (timeline.statusProperty().getValue()==Animation.Status.RUNNING) ? true : false;
			} catch (Exception e) {
				t = false;
			}
			if (t) timeline.pause();
			startButton.setGraphic(startImage);
			startButton.setBorder(pauseBorder);
			startButton.setBackground(pauseBackground);
			int i;
			try {
				i = Integer.parseInt(addField.getText());
			} catch (Exception e) {
				i = 10;
			}
			field.addMushrooms(i);
			if (t) timelinePlay();
		});
		
		randomButton.setOnAction((ActionEvent event) -> {
			field.randomizeField();
		});
		
		timeSlider.valueProperty().addListener((ChangeListener) -> {
			sliderLabel.setText("Geschwindigkeit in Millisekunden: " + String.valueOf((int)timeSlider.getValue()));
			boolean t;
			try {
				t = (timeline.statusProperty().getValue()==Animation.Status.RUNNING) ? true : false;
			} catch (Exception e) {
				t = false;
			}
			stepMillis = (int)timeSlider.getValue();
			timelineCreate();
			if (t) timelinePlay();
		});
		
		toggleSizeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> toggleValue, Toggle oldToggle, Toggle newToggle) {
				canvasX = (int)newToggle.getUserData();
				canvasY = canvasX;
				canvasPane.setPrefSize(canvasX+4, canvasY+4);
				primaryStage.setWidth(canvasX+216);
				primaryStage.setHeight(canvasY+16);
				setCanvas(canvasX, canvasY);
			}
		});
		
		mushroomSlider.valueProperty().addListener((ChangeListener) -> {
			if (timeline != null) {
				if (timeline.statusProperty().getValue()==Animation.Status.RUNNING) {
					timeline.stop();
				}
			}
			double t = mushroomSlider.getValue();
			fieldX = (int)t;
			fieldY = (int)t;
			setCanvas(fieldX, fieldY);
			mushroomLabel.setText("Rasterung des Feldes: "+fieldX);
		});
		
		// Stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Game of Life");
		primaryStage.show();
	}
	
	private void timelineCreate() {
		if (timeline!=null) {
			timeline.stop();
		}
		timeline = new Timeline(new KeyFrame(Duration.millis(stepMillis), (x1) -> {field.nextStep();}));
		timeline.setCycleCount(Animation.INDEFINITE);
	}
	
	private void timelinePause() {
		if (timeline!=null) {
			timeline.pause();
			startButton.setGraphic(startImage);
			startButton.setBorder(pauseBorder);
			startButton.setBackground(pauseBackground);
		}
	}
	
	private void timelinePlay() {
		if (timeline!=null) {
			timeline.play();
			startButton.setGraphic(stopImage);
			startButton.setBorder(startBorder);
			startButton.setBackground(startBackground);
		}
	}
	
	private void setCanvas(int xMax, int yMax) {
		xMax = xMax-xMax%10;
		yMax = yMax-yMax%10;
		canvas.setHeight(canvasY);
		canvas.setWidth(canvasX);
		field.setFieldCoordinates(fieldX, fieldY);
	}

	public static void main(String[] args) {
		launch(args);
	}

}

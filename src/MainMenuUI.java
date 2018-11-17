import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainMenuUI extends Application {

    private List<Node> test = new ArrayList<>();
    private GridPane layout;
    private double[] screenDim;
    private Stage primaryStage;
    private String state;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenDim = new double[] {primaryScreenBounds.getWidth() / 2, primaryScreenBounds.getHeight() / 2};
        primaryStage.setTitle("CardGame");
        initialiseMainScreenElements();
        setUpMainScreenLayout();
        startScreen();
    }

    private void initialiseMainScreenElements() {
        String[] initStringsButton = {"Start", "Help", "Settings"};
        Label label = new Label("CardGame");
        label.setTextFill(Color.web("#ffffff"));
        label.setFont(new Font(60.0));
        test.add(label);
        for (String initStringButton : initStringsButton) {
            Button button = new Button(initStringButton);
            button.setFont(new Font(20.0));
            button.setOnAction( __ ->
                    changeScreen(button.getText()));
            test.add(button);
        }
    }

    private void setUpMainScreenLayout() {
        layout = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(), column2 = new ColumnConstraints(), column3 = new ColumnConstraints();
        ColumnConstraints[] columnArray = {column1, column2, column3};

        BackgroundImage image = new BackgroundImage(new Image("/resources/Poker_cards_and_chips.jpg", screenDim[0], screenDim[1], false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        layout.setBackground(new Background(image));

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);
        layout.setAlignment(Pos.CENTER);

        for (Node t : test) {
            GridPane.setHalignment(t, HPos.CENTER);
        }
        for (int i = 0; i < 3; i++) {
            if (i == 1) {
                columnArray[i].setPercentWidth(60);
            }
            else {
                columnArray[i].setPercentWidth(20);
            }
        }

        int[][] gridPaneNums = {{1, 0}, {1, 10}, {1, 14}, {1, 18}};

        layout.getColumnConstraints().addAll(columnArray);

        int i = 0;
        for (Node t : test) {
            layout.add(t, gridPaneNums[i][0], gridPaneNums[i][1]);
            i++;
        }
    }

    private void initialiseSettingsScreenElements() {
        String[] initStringsSliderLabels = {"Total Cash", "Num of Players"};
        int[][] initSliderLevels = {{100, 10000, 1000}, {2, 8, 2}};
        int[] initSliderIncrements = {100, 1};
        Button button = new Button("<-");
        button.setOnAction( __ ->
                changeScreen(button.getText()));
        test.add(button);
        Label label = new Label("Settings");
        label.setFont(new Font(60.0));
        test.add(label);
        for (int i = 0; i < initStringsSliderLabels.length; i++) {
            Label titleLabel = new Label(initStringsSliderLabels[i]);
            Slider slider = new Slider(initSliderLevels[i][0], initSliderLevels[i][1], initSliderLevels[i][2]);
            Label valueLabel = new Label(String.valueOf((int)slider.getValue()));
            slider.setBlockIncrement(initSliderIncrements[i]);
            slider.setShowTickMarks(true);
            slider.setMinorTickCount(0);
            slider.setMajorTickUnit(initSliderIncrements[i]);
            slider.setSnapToTicks(true);
            slider.valueProperty().addListener((observable, oldValue, newValue) -> valueLabel.textProperty().setValue(String.valueOf((int) slider.getValue())));
            test.add(titleLabel);
            test.add(slider);
            test.add(valueLabel);
            if (i == 0) {
                Label bigBlindLabel = new Label("Big Blind: " + String.valueOf((int)slider.getValue() / 50));
                Label smallBlindLabel = new Label("Small Blind: " + String.valueOf((int)slider.getValue() / 100));
                slider.valueProperty().addListener(((observable, oldValue, newValue) -> bigBlindLabel.textProperty().setValue("Big Blind: " + String.valueOf((int)slider.getValue() / 50))));
                slider.valueProperty().addListener(((observable, oldValue, newValue) -> smallBlindLabel.textProperty().setValue("Small Blind: " + String.valueOf((int)slider.getValue() / 100))));
                test.add(bigBlindLabel);
                test.add(smallBlindLabel);
            }
        }

    }

    private void setUpSettingsScreenLayout() {
        layout = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(), column2 = new ColumnConstraints(), column3 = new ColumnConstraints(), column4 = new ColumnConstraints();
        ColumnConstraints[] columnArray = {column1, column2, column3, column4};

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        columnArray[0].setPercentWidth(10);
        columnArray[1].setPercentWidth(15);
        columnArray[2].setPercentWidth(60);
        columnArray[3].setPercentWidth(15);

        int[][] gridPaneNums = {{0, 0}, {2, 0}, {1, 3}, {2, 3}, {3, 3}, {1, 4}, {3, 4}, {1, 6}, {2, 6}, {3, 6}};

        layout.getColumnConstraints().addAll(columnArray);

        int i = 0;
        for (Node t : test) {
            /*if (i == 1) {
                GridPane.setHalignment(t, HPos.CENTER);
            }*/
            layout.add(t, gridPaneNums[i][0], gridPaneNums[i][1]);
            i++;
        }
    }

    private void initialiseHelpScreenElements() {
        String[] helpHeadings = {"How to Play", "How the Game Work", "Hand Strengths"};
        String[] helpContent = {"Test test test test test test test test Test test test test test test test test test Test test test test test test Test test test test test test test Test test test test test test test test", "vvvvvvvv", "fffffffffffffffff"};
        Button button = new Button("<-");
        button.setOnAction( __ ->
                changeScreen(button.getText()));
        test.add(button);
        Label label = new Label("Help");
        label.setFont(new Font(60.0));
        test.add(label);
        for (int i = 0; i < helpHeadings.length; i++) {
            Label heading = new Label(helpHeadings[i]);
            heading.setFont(new Font(30.0));
            Label content = new Label(helpContent[i]);
            content.setWrapText(true);
            test.add(heading);
            test.add(content);
        }
    }

    private void setUpHelpScreenLayout() {
        layout = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(), column2 = new ColumnConstraints();
        ColumnConstraints[] columnArray = {column1, column2};

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        columnArray[0].setPercentWidth(10);
        columnArray[1].setPercentWidth(90);

        int[][] gridPaneNums = {{0, 0}, {1, 0}, {1, 3}, {1, 4}, {1, 7}, {1, 8}, {1, 11}, {1, 12}};

        layout.getColumnConstraints().addAll(columnArray);

        int i = 0;
        for (Node t : test) {
            if (i == 1) {
                GridPane.setHalignment(t, HPos.CENTER);
            }
            layout.add(t, gridPaneNums[i][0], gridPaneNums[i][1]);
            i++;
        }
    }

    private void startScreen() {
        Scene scene = new Scene(layout, screenDim[0], screenDim[1]);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void changeScreen(String newButton) {
        if (newButton.equals("Start")) {
            GameUI game = new GameUI();
        }
        else if (newButton.equals("Settings")) {
            clearCanvas();
            initialiseSettingsScreenElements();
            setUpSettingsScreenLayout();
            startScreen();
        }
        else if (newButton.equals("Help")) {
            clearCanvas();
            initialiseHelpScreenElements();
            setUpHelpScreenLayout();
            startScreen();
        }
        else if (newButton.equals("<-")) {
            clearCanvas();
            initialiseMainScreenElements();
            setUpMainScreenLayout();
            startScreen();
        }
    }

    void clearCanvas() {
        layout.getChildren().clear();
        test.clear();
    }
}

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    private static String state;
    private List<Node> test = new ArrayList<>();
    private GridPane layout;

    public static void main(String[] args) {
        state = "init";
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double[] screenDim = {primaryScreenBounds.getWidth() / 2, primaryScreenBounds.getHeight() / 2};
        primaryStage.setTitle("CardGame");
        initialiseElements();
        setUpLayout(screenDim);
        startScreen(primaryStage, screenDim);
    }

    private void initialiseElements() {
        if (state.equals("init")) {
            String[] initStringsButton = {"Start", "Help", "Settings"};
            Label label = new Label("CardGame");
            label.setFont(new Font(40.0));
            test.add(label);
            for (String initStringButton : initStringsButton) {
                Button button = new Button(initStringButton);
                button.setFont(new Font(20.0));
                button.applyCss();
                test.add(button);
            }
        }
        if (state.equals("")) {

        }
    }

    private void setUpLayout(double[] screenInfo) {
        layout = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(), column2 = new ColumnConstraints(), column3 = new ColumnConstraints();
        ColumnConstraints[] columnArray = {column1, column2, column3};

        BackgroundImage image = new BackgroundImage(new Image("/resources/Poker_cards_and_chips.jpg", screenInfo[0], screenInfo[1], false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
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

    private void startScreen(Stage primaryScreen, double[] screenDim) {
        //System.out.println(screenDim[0]);
        //System.out.println(screenDim[1]);
        Scene scene = new Scene(layout, screenDim[0], screenDim[1]);
        primaryScreen.setScene(scene);
        primaryScreen.show();
    }
}

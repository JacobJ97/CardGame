import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    private String state;
    private List<Node> test = new ArrayList<>();

    /* State - initString */
    private String[] initStringsButton = {"Start", "Help", "Settings"};
    private String initStringLabel = "CardGame";
    private ArrayList<Button> buttons = new ArrayList<>();

    public GUI(String state) {
        this.state = state;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CardGame");
        initialiseElements();
        setUpLayout();
    }

    private void initialiseElements() {
        if (state.equals("init")) {
            Label label = new Label(initStringLabel);
            test.add(label);
            for (String initStringButton : initStringsButton) {
                Button button = new Button(initStringButton);
                test.add(button);
            }
        }
        if (state.equals("")) {

        }
    }

    private void setUpLayout() {
        StackPane layout = new StackPane();
        layout.getChildren().addAll(test);
    }
}

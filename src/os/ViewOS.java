package os;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewOS extends Application {
    private final OS os = new OS();
    private final TextArea ta = new TextArea();
    private final Label lblTitle = new Label("OS simulator [Version 1.1]");
    private final Label lblAuthor = new Label("Created by [Luka Nežić, Aleksa Aćić]");
    private final VBox vbLabels = new VBox(lblTitle, lblAuthor);
    private final VBox root = new VBox(10, vbLabels, ta);

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(root, 900, 500);
        setStyles();
        appendCurrentDir();

        stage.setScene(scene);
        stage.setTitle("OS simulator");
        stage.show();
    }

    public void setStyles() {
        VBox.setVgrow(ta, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #2D2D2D");
        lblTitle.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px");
        lblAuthor.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px");
        ta.setStyle(
                "-fx-control-inner-background: #2D2D2D; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-insets: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-color: #2D2D2D; " + // Setting border color
                        "-fx-border-radius: 0; " +
                        "-fx-border-width: 1;"
        );
    }

    public void appendCurrentDir() {
        ta.appendText(os.getFileSystem().getDirectoryPath());
    }
}

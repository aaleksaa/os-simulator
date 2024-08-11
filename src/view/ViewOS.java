package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import os.OS;

import java.io.PrintStream;

public class ViewOS extends Application {
    private final OS os = new OS();
    private final TextArea ta = new TextArea();
    private final TextField tf = new TextField();
    private final Label lblTitle = new Label("OS simulator [Version 1.0]");
    private final Label lblAuthor = new Label("Created by [Luka Nežić, Aleksa Aćić]");
    private final VBox vbLabels = new VBox(lblTitle, lblAuthor);
    private final VBox root = new VBox(10, vbLabels, ta, tf);
    private final PrintStream out = new PrintStream(new TextAreaOutputStream(ta));

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(root, 900, 500);
        setStyles();

        stage.setScene(scene);
        stage.setTitle("OS simulator");
        stage.show();

        System.setOut(out);
        System.setErr(out);
        System.out.println("Type \"help\" for list of commands!");

        tf.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                String command = tf.getText().trim();
                System.out.println("> " + command);
                os.executeCommand(command);
                tf.clear();
            }
        });
    }

    public void setStyles() {
        VBox.setVgrow(ta, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #2D2D2D");
        root.setPadding(new Insets(20));
        lblTitle.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold");
        lblAuthor.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14px; -fx-font-weight: bold");
        ta.setEditable(false);
        ta.setStyle(
                "-fx-control-inner-background: #2D2D2D; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-color: #FFFFFF; " +
                        "-fx-border-radius: 0; " +
                        "-fx-border-width: 1;"
        );
        tf.setStyle(
                "-fx-control-inner-background: #2D2D2D; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 0; " +
                        "-fx-border-color: #FFFFFF; " +
                        "-fx-border-radius: 0; " +
                        "-fx-border-width: 1;"
        );
    }
}

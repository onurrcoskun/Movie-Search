package org.kodluyoruz.moviedb;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class Main extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlFile = getClass()
                .getClassLoader()
                .getResource("layout.fxml");

        loader.setLocation(fxmlFile);

        BorderPane root = loader.load();
        Scene scene = new Scene(root, 700d, 400d);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Film Veritabanı");
        primaryStage.setMinWidth(400d);
        primaryStage.setMinHeight(200d);
        primaryStage.show();
    }

}

package main.java;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {
    @FXML
    public Button settings;
    @FXML
    public Button copy;
    @FXML
    private TextField input;
    @FXML
    private TextField result;
    //flags
    private boolean capitalized = true;
    private boolean exceptFirst = true;
    private CharSequence delimiter = "";
    private Stage primaryStage;
    private Stage settingsStage;
    private SettingsController settingsController;

    public void textInput(ActionEvent actionEvent) throws IOException {
        String result = getResult(input.getText());
        result = getFormat(result);
        this.result.setText(result);
    }

    public void openSettingWindow(ActionEvent actionEvent) throws IOException {
        settingsController.init();
        settingsStage.showAndWait();
    }

    public void copyResult(ActionEvent actionEvent) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(result.getText());
        clipboard.setContents(stringSelection, null);
    }

    private String getResult(String input) throws IOException {
        String parameters = getParameters("ru", "en", input);
        HttpURLConnection connection = getRequest(parameters);
        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        JSONArray jsonArray = new JSONArray(content.toString());

        return (String)((JSONArray)((JSONArray)jsonArray.get(0)).get(0)).get(0);
    }

    private HttpURLConnection getRequest(String parameters) throws IOException {
        URL url = new URL("https://translate.googleapis.com/translate_a/single");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(parameters);
        out.flush();
        out.close();

        return connection;
    }

    private String getParameters(String from, String to, String test) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "text");
        parameters.put("client", "gtx");
        parameters.put("sl", from);
        parameters.put("tl", to);
        parameters.put("dt", "t");
        parameters.put("q", test);
        return getParamsString(parameters);
    }

    private String getFormat(String input) {
        input = input.replaceAll("([^a-zA-Z\\s])", "").toLowerCase();
        Stream<String> stingStream = Arrays.stream(input.split(" "));
        if (settingsController.getCapitalized()) {
            stingStream = stingStream.map(x -> x = x.substring(0, 1).toUpperCase() + x.substring(1));
        }

        StringBuilder result = new StringBuilder(stingStream.collect(Collectors.joining(settingsController.getDelimiter())));

        if (settingsController.getExceptFirst()) {
            result.replace(0, 1, result.substring(0, 1).toLowerCase());
        }

        return  result.toString();
    }

    public static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initSettingsWindow() throws IOException {
        settingsStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resource/settings.fxml"));
        Parent settings = loader.load();
        settingsStage.setTitle("Settings");
        settingsStage.setScene(new Scene(settings, 250, 140));
        settingsStage.initModality(Modality.WINDOW_MODAL);
        settingsStage.initOwner(primaryStage);
        settingsController = loader.getController();
    }
}

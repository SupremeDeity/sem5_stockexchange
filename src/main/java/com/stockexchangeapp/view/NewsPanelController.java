package com.stockexchangeapp.view;

import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.stockexchangeapp.MainApp;

/**
 * FXML Controller class
 *
 * @author blazej
 */
public class NewsPanelController implements Initializable {
  private MainApp app;
  JSONArray articles;
  int index;

  @FXML
  private Label title;
  @FXML
  private Label author;
  @FXML
  private Text content;
  @FXML
  private Hyperlink link;

  public void setApp(MainApp app) {
    this.app = app;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    JSONObject json = null;
    try {
      json = readJsonFromUrl(
          "https://newsapi.org/v2/everything?sources=bloomberg&apiKey=cb2b0d37fa484c128cb2a687b81854bb");
      articles = json.getJSONArray("articles");

      JSONObject article = (JSONObject) articles.get(0);
      System.out.println(article.get("description"));
      title.setText(article.getString("title"));
      author.setText(article.getString("author"));
      content.setText(article.getString("description"));
      link.setText(article.getString("url"));
    } catch (IOException ex) {
      Logger.getLogger(NewsPanelController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (JSONException ex) {
      Logger.getLogger(NewsPanelController.class.getName()).log(Level.SEVERE, null, ex);
    }

    link.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent t) {
        try {
          new ProcessBuilder("x-www-browser", link.getText()).start();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  @FXML
  public void handleNextNews() throws JSONException {

    index = (index + 1) % articles.length();

    JSONObject article = (JSONObject) articles.get(index);
    System.out.println(article.get("description"));
    title.setText(article.getString("title"));
    author.setText(article.getString("author"));
    content.setText(article.getString("description"));
    link.setText(article.getString("url"));

  }

  @FXML
  public void handlePrevNews() throws JSONException {

    index -= 1;
    if (index < 0)
      index = articles.length() - 1;

    JSONObject article = (JSONObject) articles.get(index);
    System.out.println(article.get("description"));
    title.setText(article.getString("title"));
    author.setText(article.getString("author"));
    content.setText(article.getString("description"));
    link.setText(article.getString("url"));

  }

  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }

}

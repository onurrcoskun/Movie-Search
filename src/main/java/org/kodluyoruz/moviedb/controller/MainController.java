package org.kodluyoruz.moviedb.controller;

import java.util.List;

import org.kodluyoruz.moviedb.cell.MovieSearchResultCell;
import org.kodluyoruz.moviedb.model.Movie;
import org.kodluyoruz.moviedb.model.MovieSearchResult;
import org.kodluyoruz.moviedb.model.MovieSearchResultResponse;
import org.kodluyoruz.moviedb.service.MyApiService;
import org.kodluyoruz.moviedb.service.MyDatabaseService;
import org.kodluyoruz.moviedb.service.MyHibernateService;

import com.mashape.unirest.http.exceptions.UnirestException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class MainController
{

    @FXML
    public TabPane tabRoot;

    @FXML
    public Tab tabSearch;

    @FXML
    public TextField txtSearch;

    @FXML
    public Button btnSearch;

    @FXML
    public ScrollPane scrollResults;

    @FXML
    public ListView<MovieSearchResult> lstResults;

    @FXML
    public Pagination pagination;

    @FXML
    public Button btnSelect;

    @FXML
    public ProgressIndicator prgSearch;

    private final MyApiService apiService = new MyApiService();
    private final MyDatabaseService databaseService = new MyDatabaseService();
    private final MyHibernateService hibernateService = new MyHibernateService();

    private void beforeMovieSearch()
    {
        txtSearch.setDisable(true);
        btnSearch.setDisable(true);
        lstResults.setDisable(true);
        pagination.setDisable(true);
        prgSearch.setVisible(true);
        pagination.setVisible(false);

        lstResults.getItems().clear();
    }

    private void afterMovieSearch()
    {
        txtSearch.setDisable(false);
        btnSearch.setDisable(false);
        lstResults.setDisable(false);
        pagination.setDisable(false);
        prgSearch.setVisible(false);
        pagination.setVisible(true);
    }

    private void setResults(List<MovieSearchResult> results)
    {
        Platform.runLater(() -> {
            for (MovieSearchResult result : results)
            {
                lstResults.getItems().add(result);
            }
        });
    }

    private void setPageCount(int pages)
    {
        Platform.runLater(() -> pagination.setPageCount(pages));
    }

    private void searchMovieInNewThread(String query)
    {
        try
        {
            final int page = pagination.getCurrentPageIndex() + 1;
            final MovieSearchResultResponse results = apiService.search(query, page);

            setResults(results.getResults());
            setPageCount(results.getPages());
        }
        catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            Platform.runLater(this::afterMovieSearch);
        }
    }

    private void searchMovie(String query)
    {
        Thread thread = new Thread(() -> searchMovieInNewThread(query));
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void searchMovie(ActionEvent e)
    {
        final String query = txtSearch
                .getText()
                .trim();

        if (query.length() > 0)
        {
            beforeMovieSearch();
            searchMovie(query);
        }
    }

    private void selectMovieInNewThread()
    {
        try
        {
            MovieSearchResult movieSearchResult = lstResults.getSelectionModel().getSelectedItem();
            Movie movie = apiService.getMovieDetail(movieSearchResult.getId());

            databaseService.addMovie(movie);
           
        }
        catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            Platform.runLater(() -> {
                btnSelect.setDisable(false);
                prgSearch.setVisible(false);
            });
        }
    }

    @FXML
    private void onMovieSelected(ActionEvent e)
    {
        btnSelect.setDisable(true);
        prgSearch.setVisible(true);

        Thread thread = new Thread(this::selectMovieInNewThread);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void initialize()
    {
        scrollResults.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollResults.widthProperty().addListener((observable, oldValue, newValue) -> lstResults.setPrefWidth(newValue.doubleValue()));
        scrollResults.heightProperty().addListener((observable, oldValue, newValue) -> lstResults.setPrefHeight(newValue.doubleValue()));

        lstResults.setCellFactory(param -> new MovieSearchResultCell(apiService));
        lstResults.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnSelect.setVisible(true);
            btnSelect.setDisable(false);
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            btnSearch.setDisable(newValue.trim().length() == 0);
            lstResults.getItems().clear();
        });

        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> searchMovie(new ActionEvent()));
    }

}

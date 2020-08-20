package org.kodluyoruz.moviedb.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.kodluyoruz.moviedb.model.Genre;
import org.kodluyoruz.moviedb.model.MovieSearchResult;
import org.kodluyoruz.moviedb.service.MyApiService;
import org.kodluyoruz.moviedb.service.MyHttpService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieSearchResultController
{

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    @FXML
    public ImageView imgMovie;

    @FXML
    public Label lblName;

    @FXML
    public Label lblOriginalName;

    @FXML
    public Label lblReleaseDate;

    @FXML
    public Label lblGenres;

    @FXML
    public TextArea txtOverview;

    private void setGenres(List<Long> genreIds, MyApiService apiService)
    {
        lblGenres.setText(genreIds
                .stream()
                .map(apiService::getGenre)
                .map(Genre::getName)
                .sorted()
                .collect(Collectors.joining(", "))
        );
    }

    public void setMovie(MovieSearchResult movie, MyApiService apiService)
    {
        if (movie != null)
        {
            imgMovie.setImage(new Image(MyHttpService.BASE_IMAGE_URL + movie.getPoster()));
            lblName.setText(movie.getTitle());
            lblOriginalName.setText("Orijinal Adı: " + movie.getOriginalTitle());
            lblReleaseDate.setText("Çıkış Tarihi: " + DATE_FORMAT.format(movie.getReleaseDate()));
            txtOverview.setText(movie.getOverview());

            setGenres(movie.getGenreIds(), apiService);
        }
    }

}

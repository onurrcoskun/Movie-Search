package org.kodluyoruz.moviedb.cell;

import java.io.IOException;
import java.net.URL;

import org.kodluyoruz.moviedb.controller.MovieSearchResultController;
import org.kodluyoruz.moviedb.model.MovieSearchResult;
import org.kodluyoruz.moviedb.service.MyApiService;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class MovieSearchResultCell extends ListCell<MovieSearchResult>
{

    private final MyApiService apiService;

    @Override
    protected void updateItem(MovieSearchResult item, boolean empty)
    {
        super.updateItem(item, empty);

        if (item == null || empty)
        {
            setText(null);
            setGraphic(null);
        }
        else
        {
            URL fxmlFile = getClass()
                    .getClassLoader()
                    .getResource("movie_search_result.fxml");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlFile);

            try
            {
                setText(null);
                setGraphic(loader.load());

                MovieSearchResultController controller = loader.getController();
                controller.setMovie(item, apiService);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}

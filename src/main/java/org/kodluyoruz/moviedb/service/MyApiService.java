package org.kodluyoruz.moviedb.service;

import java.util.TreeMap;

import org.kodluyoruz.moviedb.model.Genre;
import org.kodluyoruz.moviedb.model.GenreSearchResponse;
import org.kodluyoruz.moviedb.model.Movie;
import org.kodluyoruz.moviedb.model.MovieDetailResponse;
import org.kodluyoruz.moviedb.model.MovieSearchResultResponse;

import com.mashape.unirest.http.exceptions.UnirestException;

public final class MyApiService
{

    private final TreeMap<Long, Genre> genres = new TreeMap<>();

    public MyApiService()
    {
        try
        {
            final GenreSearchResponse genres = listGenres();

            for (Genre genre : genres.getGenres())
            {
                this.genres.put(genre.getId(), genre);
            }
        }
        catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    public GenreSearchResponse listGenres() throws UnirestException
    {
        return MyHttpService
                .connect("/genre/movie/list")
                .param("language", "tr-TR")
                .getAs(GenreSearchResponse.class);
    }

    public Genre getGenre(Long id)
    {
        return genres.get(id);
    }

    public MovieSearchResultResponse search(String query, Integer page) throws UnirestException
    {
        MyHttpService request = MyHttpService
                .connect("/search/movie")
                .param("language", "tr-TR")
                .param("query", query);

        if (page != null)
        {
            request.param("page", String.valueOf(page));
        }

        return request.getAs(MovieSearchResultResponse.class);
    }

    public MovieDetailResponse getMovieCredits(Long movieId) throws UnirestException
    {
        return MyHttpService
                .connect("/movie/" + movieId + "/credits")
                .param("language", "tr-TR")
                .getAs(MovieDetailResponse.class);
    }

    public Movie getMovieDetail(Long movieId) throws UnirestException
    {
        Movie movie = MyHttpService
                .connect("/movie/" + movieId)
                .param("language", "tr-TR")
                .getAs(Movie.class);

        MovieDetailResponse credits = getMovieCredits(movieId);
        movie.setCast(credits.getCast());
        movie.setCrew(credits.getCrew());

        return movie;
    }

}

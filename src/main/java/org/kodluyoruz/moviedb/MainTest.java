package org.kodluyoruz.moviedb;

import org.kodluyoruz.moviedb.model.MovieSearchResultResponse;
import org.kodluyoruz.moviedb.service.MyApiService;
import org.kodluyoruz.moviedb.service.MyDatabaseService;
import org.kodluyoruz.moviedb.service.MyHttpService;

import com.mashape.unirest.http.exceptions.UnirestException;

public class MainTest
{

    public static void main(String[] args) throws UnirestException
    {
        new MyHibernateService().findMovies();
        // new MyDatabaseService().findMovies();
    }

}

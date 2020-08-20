package org.kodluyoruz.moviedb.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.kodluyoruz.moviedb.model.Cast;
import org.kodluyoruz.moviedb.model.Crew;
import org.kodluyoruz.moviedb.model.Genre;
import org.kodluyoruz.moviedb.model.Movie;
import org.kodluyoruz.moviedb.model.ProductionCompany;

public final class MyDatabaseService implements AutoCloseable
{

    private static final String CONNECTION_URL = "jdbc:mysql://127.0.0.1:3306/movies?serverTimezone=Europe%2FIstanbul";

    private final Connection connection;

    public MyDatabaseService()
    {
        try
        {
            connection = DriverManager.getConnection(CONNECTION_URL, "root", "test");
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void addGenre(Genre genre)
    {
        int count = 0;

        try (Statement statement = connection.createStatement())
        {
            try (ResultSet rs = statement.executeQuery("select * from genres g where g.id = " + genre.getId()))
            {
                while (rs.next())
                {
                    count++;
                }
            }
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }

        if (count > 0)
        {
            return;
        }

        final String query = "insert into genres (id, name) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, genre.getId());
            statement.setString(2, genre.getName());

            statement.execute();
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public void addGenres(Movie movie)
    {
        final String query = "insert into movie_genres (movie_id, genre_id) values (?, ?)";
        for (Genre genre : movie.getGenres())
        {
            addGenre(genre);

            try (PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setLong(1, movie.getId());
                statement.setLong(2, genre.getId());

                statement.execute();
            }
            catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }

    public void addCast(Movie movie)
    {
        final String query = "insert into casts (name, `character`, image, `order`, movie_id) values (?, ?, ?, ?, ?)";
        for (Cast cast : movie.getCast())
        {
            try (PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setString(1, cast.getName());
                statement.setString(2, cast.getCharacter());
                statement.setString(3, cast.getImage());
                statement.setInt(4, cast.getOrder());
                statement.setLong(5, movie.getId());

                statement.execute();
            }
            catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }

    public void addCrew(Movie movie)
    {
        final String query = "insert into crew (name, job, image, movie_id) values (?, ?, ?, ?)";
        for (Crew crew : movie.getCrew())
        {
            try (PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setString(1, crew.getName());
                statement.setString(2, crew.getJob());
                statement.setString(3, crew.getImage());
                statement.setLong(4, movie.getId());

                statement.execute();
            }
            catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }

    public void addProductionCompanies(Movie movie)
    {
        final String query = "insert into production_companies (name, image, movie_id) values (?, ?, ?)";
        for (ProductionCompany productionCompany : movie.getProductionCompanies())
        {
            try (PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setString(1, productionCompany.getName());
                statement.setString(2, productionCompany.getImage());
                statement.setLong(3, movie.getId());

                statement.execute();
            }
            catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }

    public void addMovie(Movie movie)
    {
        final String query = "insert into movies (budget, imdb_id, org_lang, org_title, " +
                "overview, poster, release_date, duration, title, vote_count, vote_average, id) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, movie.getBudget());
            statement.setString(2, movie.getImdbId());
            statement.setString(3, movie.getOriginalLanguage());
            statement.setString(4, movie.getOriginalTitle());
            statement.setString(5, movie.getOverview());
            statement.setString(6, movie.getPoster());
            statement.setDate(7, new Date(movie.getReleaseDate().getTime()));
            statement.setInt(8, movie.getDuration());
            statement.setString(9, movie.getTitle());
            statement.setLong(10, movie.getVoteCount());
            statement.setDouble(11, movie.getVoteAverage());
            statement.setLong(12, movie.getId());

            statement.execute();

            addGenres(movie);
            addCast(movie);
            addCrew(movie);
            addProductionCompanies(movie);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<Movie> findMovies()
    {
        final LinkedList<Movie> results = new LinkedList<>();
        try (Statement statement = connection.createStatement())
        {
            try (ResultSet rs = statement.executeQuery("select * from movies"))
            {
                while (rs.next())
                {
                    Movie movie = new Movie();
                    movie.setId(rs.getLong("id"));
                    movie.setBudget(rs.getLong("budget"));
                    movie.setImdbId(rs.getString("imdb_id"));
                    movie.setOriginalLanguage(rs.getString("org_lang"));
                    movie.setOriginalTitle(rs.getString("org_title"));
                    movie.setOverview(rs.getString("overview"));
                    movie.setPoster(rs.getString("poster"));
                    movie.setReleaseDate(rs.getDate("release_date"));
                    movie.setDuration(rs.getInt("duration"));
                    movie.setTitle(rs.getString("title"));
                    movie.setVoteCount(rs.getLong("vote_count"));
                    movie.setVoteAverage(rs.getDouble("vote_average"));

                    results.add(movie);
                }
            }
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }
        return results;
    }

    @Override
    public void close() throws Exception
    {
        connection.close();
    }

}

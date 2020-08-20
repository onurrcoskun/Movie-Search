package org.kodluyoruz.moviedb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public final class MovieSearchResult implements Serializable
{

    private Long id;

    private Double popularity;

    @SerializedName("vote_count")
    private Long voteCount;

    @SerializedName("vote_average")
    private Double votes;

    private String title;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("genre_ids")
    private List<Long> genreIds = new LinkedList<>();

    private String overview;

    @SerializedName("poster_path")
    private String poster;

}

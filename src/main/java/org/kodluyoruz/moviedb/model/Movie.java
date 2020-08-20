package org.kodluyoruz.moviedb.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "movies")
public final class Movie implements Serializable
{

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "budget")
    private Long budget;

    @Transient
    private List<Genre> genres = new LinkedList<>();

    @Column(name = "imdb_id")
    @SerializedName("imdb_id")
    private String imdbId;

    @Column(name = "org_lang")
    @SerializedName("original_language")
    private String originalLanguage;

    @Column(name = "org_title")
    @SerializedName("original_title")
    private String originalTitle;

    @Column(name = "overview")
    private String overview;

    @Column(name = "poster")
    @SerializedName("poster_path")
    private String poster;

    @Column(name = "release_date")
    @SerializedName("release_date")
    private Date releaseDate;

    @Column(name = "duration")
    @SerializedName("runtime")
    private Integer duration;

    @Column(name = "title")
    private String title;

    @Column(name = "vote_count")
    @SerializedName("vote_count")
    private Long voteCount;

    @Column(name = "vote_average")
    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("production_companies")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<ProductionCompany> productionCompanies = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<Cast> cast = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<Crew> crew = new LinkedList<>();

}

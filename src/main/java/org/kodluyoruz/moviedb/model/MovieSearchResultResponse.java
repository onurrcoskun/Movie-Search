package org.kodluyoruz.moviedb.model;

import java.io.Serializable;
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
public final class MovieSearchResultResponse implements Serializable
{

    private Integer page;

    @SerializedName("total_results")
    private Integer count;

    @SerializedName("total_pages")
    private Integer pages;

    private List<MovieSearchResult> results = new LinkedList<>();

}

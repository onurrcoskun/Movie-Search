package org.kodluyoruz.moviedb.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public final class GenreSearchResponse implements Serializable
{

    private List<Genre> genres = new LinkedList<>();

}

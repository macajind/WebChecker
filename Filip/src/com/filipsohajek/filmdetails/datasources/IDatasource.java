package com.filipsohajek.filmdetails.datasources;

import com.filipsohajek.filmdetails.Film;
import com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException;
import org.json.JSONException;

import java.io.IOException;

public interface IDatasource {
    public Film getFilmByName(String name) throws IOException, FilmNotFoundException, JSONException;
    public Film getFilmById(String id) throws IOException, JSONException;
}

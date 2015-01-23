package com.filipsohajek.filmdetails.datasources;

import com.filipsohajek.filmdetails.Film;
import com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException;
import org.json.JSONException;

import java.io.IOException;

public interface IDatasource {
    /**
     * Get Film object by its name
     * @param name Name of film
     * @return Film
     * @throws IOException
     * @throws FilmNotFoundException
     * @throws JSONException
     */
    public Film getFilmByName(String name) throws IOException, FilmNotFoundException, JSONException;

    /**
     * Get Film object by its ID
     * @param id ID of film in specifed database
     * @return Film
     * @throws IOException
     * @throws JSONException
     */
    public Film getFilmById(String id) throws IOException, JSONException;
}

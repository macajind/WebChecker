package com.filipsohajek.filmdetails.datasources;

import com.filipsohajek.filmdetails.Film;

public interface IDatasource {
    public Film getFilmByName(String name);
    public Film getFilmById(String id);
}

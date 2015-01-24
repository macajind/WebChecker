package com.filipsohajek.filmdetails.datasources;

import com.filipsohajek.filmdetails.Film;
import com.filipsohajek.filmdetails.HttpClient;
import com.filipsohajek.filmdetails.StringUtils;
import com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Iterator;

public class CsfdDatasource implements IDatasource {
    private final String SOURCE_URL = "http://csfdapi.cz";
    private final String SEARCH_QUERY = "/movie?search=";
    private final String DETAILS_QUERY = "/movie/";
    public CsfdDatasource() {}

    /**
     * Get Film object by its name. Throws {@link java.io.IOException} I/O error, {@link com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException}
     * while program can't find film and {@link org.json.JSONException} on JSON parsing error.
     * @param name Name of film
     * @return Film
     * @throws IOException
     * @throws FilmNotFoundException
     * @throws JSONException
     */
    @Override
    public Film getFilmByName(String name) throws IOException, FilmNotFoundException, JSONException {
        URL url = new URL(StringUtils.concat(SOURCE_URL, SEARCH_QUERY, name.replace(" ", "+")));
        HttpClient httpClient = new HttpClient(url);
        httpClient.setFollowRedirects(true);
        String response = httpClient.getResponse();
        JSONArray obj;
        if (response.equals("[]"))
        {
            throw new FilmNotFoundException("Cannot find film of this name!");
        }

        obj = new JSONArray(response);
        //Returns ID of first film, it's in first element in JSON Array - JSON Object with film details
        return getFilmById(String.valueOf(obj.getJSONObject(0).getInt("id")));
    }

    /**
     * Get Film object by its ID. Throws {@link java.io.IOException} on I/O error and {@link org.json.JSONException}
     * on JSON parsing error.
     * @param id ID of film in specifed database
     * @return Film
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    @Override
    public Film getFilmById(String id) throws IOException, JSONException {
        Film film = new Film();
        URL url = new URL(StringUtils.concat(SOURCE_URL, DETAILS_QUERY, String.valueOf(id)));
        HttpClient detailsclient = new HttpClient(url);
        String details = detailsclient.getResponse();
        JSONObject jsonobj = new JSONObject(details);
        JSONObject names = jsonobj.getJSONObject("names");

        //Get film's names and append it to the film. Film can have multiple names in different languages.
        Iterator<?> namekeys = names.keys();
        while(namekeys.hasNext()) {
            String key = (String) namekeys.next();
            String value = names.getString(key);
            film.appendName(key, value);
        }

        //Set birthyear and runtime. Runtime must be int, so we must remove the "min" string from runtime String
        film.setBirthyear(Year.parse(String.valueOf(jsonobj.getInt("year"))));
        film.setRuntime(Integer.valueOf(jsonobj.getString("runtime").replaceAll("[^\\d.]", "")));

        //Get genres. One film can have multiple genres
        JSONArray genres = jsonobj.getJSONArray("genres");
        for (int i = 0; i < genres.length(); i++) {
            film.appendGenre(genres.getString(i));
        }

        film.setPlot(jsonobj.getString("plot"));
        film.setRating(jsonobj.getInt("rating"));

        //Set authors. Lots of dirty code. Authors are HashMap<String, ArrayList<String>>. I must simplify it.
        JSONObject authors = jsonobj.getJSONObject("authors");
        Iterator<?> authorkeys = authors.keys();
        while(authorkeys.hasNext()) {
            String key = (String) authorkeys.next();
            JSONArray value = authors.getJSONArray(key);
            ArrayList<String> authorsarray = new ArrayList<String>();
            for (int i = 0; i < value.length(); i++) {
                authorsarray.add(value.getJSONObject(i).getString("name"));
            }
            film.appendAuthor(key, authorsarray.toArray(new String[authorsarray.size()]));
        }
        return film;
    }
}

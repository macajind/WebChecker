package com.filipsohajek.filmdetails;

import com.filipsohajek.filmdetails.datasources.IDatasource;
import com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException;
import com.filipsohajek.filmdetails.renderers.IRenderer;
import org.apache.commons.cli.*;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class FilmDetails {
    public static void main(String args[]) throws NoSuchMethodException, JSONException, FilmNotFoundException, IOException, ClassNotFoundException {
        String filmname;
        String source;
        String renderer;

        Options ops = new Options();
        ops.addOption("d", "datasource", true, "Set the specifed data source");
        ops.addOption("r", "renderer", true, "Set the specifed renderer");

        CommandLineParser parser = new GnuParser();
        CommandLine cmd = null;
        try {
             cmd = parser.parse( ops, args);
        } catch (ParseException e) {
            System.err.println("An internal error occured while trying to parse arguments. Dying...");
            return;
        }

        renderer = cmd.hasOption("r") ? cmd.getOptionValue('r').toLowerCase() : "console";
        source = cmd.hasOption("d") ? cmd.getOptionValue('d').toLowerCase() : "csfd";

        filmname = cmd.getArgs().length >= 1 ? cmd.getArgs()[0] : null;
        if (filmname == null)
        {
            System.err.println("No film specifed, falling back to default film Interstellar.");
            filmname = "Interstellar";
        }

        Class<?> dsclass = null;
        try {
            dsclass = Class.forName("com.filipsohajek.filmdetails.datasources." + source.substring(0, 1).toUpperCase() + source.substring(1) + "Datasource");
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown datasource! Switching to default...");
            dsclass = Class.forName("com.filipsohajek.filmdetails.datasources.CsfdDatasource");
        }
        Constructor<?> dsctor = dsclass.getConstructor();
        IDatasource datasource = null;
        try {
            datasource = (IDatasource) dsctor.newInstance();
        } catch (Exception e) {
           System.err.println("Invalid film name! Dying...");
            e.printStackTrace();
           return;
        }
        Class<?> rclass = null;
        try {
            rclass = Class.forName("com.filipsohajek.filmdetails.renderers." + renderer.substring(0, 1).toUpperCase() + renderer.substring(1) + "Renderer");
        } catch (ClassNotFoundException e) {
            rclass = Class.forName("com.filipsohajek.filmdetails.renderers.ConsoleRenderer");
        }
        Constructor<?> rctor = rclass.getConstructor(Film.class);
        IRenderer rendererc = null;
        Film film = datasource.getFilmByName(filmname);
        try {
            rendererc = (IRenderer) rctor.newInstance(new Object[] {film});
        } catch (Exception e) {
            System.err.println("Unknown renderer! Switching to default...");
            System.err.println("Invalid film name! Dying...");
            return;
        }
        rendererc.renderFilm();
    }

}

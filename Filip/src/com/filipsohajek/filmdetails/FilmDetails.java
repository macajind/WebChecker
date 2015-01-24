package com.filipsohajek.filmdetails;

import com.filipsohajek.filmdetails.datasources.IDatasource;
import com.filipsohajek.filmdetails.datasources.exceptions.FilmNotFoundException;
import com.filipsohajek.filmdetails.renderers.IRenderer;
import org.apache.commons.cli.*;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class FilmDetails {
    //Fallback constants. Their values are used, if user does not entered it.
    private final String FALLBACK_DATASOURCE = "Csfd";
    private final String FALLBACK_RENDERER = "Console";
    private final String FALLBACK_FILM = "Interstellar;";

    public final boolean DEBUG = false;
    public final String PACKAGE = "com.filipsohajek.filmdetails.";
    public final String RENDERER_PACKAGE = PACKAGE + "renderers.";
    public final String DATASOURCE_PACKAGE = PACKAGE + "datasources.";

    public static void main(String args[]) {
        FilmDetails mainclass = new FilmDetails();
        try {
            mainclass.run(args);
        } catch (Exception e) {
            System.err.println("An unknown critical error occured. Dying...");
            if (mainclass.DEBUG) e.printStackTrace();
        }
    }

    /**
     * Main method. Parses arguments and runs application.
     * @param args Command line arguments
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    void run(String args[]) throws ClassNotFoundException, NoSuchMethodException {
        String filmname;
        String source;
        String renderer;

        CommandLineParser cliparser;
        CommandLine cmd = null;

        Class<?> dsclass = null;
        Class<?> rclass = null;

        IDatasource datasource = null;
        IRenderer rendererc = null;

        Film film = null;

        Options ops = new Options();
        ops.addOption("d", "datasource", true, "Set the specifed data source");
        ops.addOption("r", "renderer", true, "Set the specifed renderer");

        cliparser = new GnuParser();
        try {
            cmd = cliparser.parse( ops, args);
        } catch (ParseException e) {
            System.err.println("An internal error occured while trying to parse arguments. Dying...");
            if (DEBUG) e.printStackTrace();
            System.exit(-1);
        }

        renderer = cmd.hasOption("r") ? cmd.getOptionValue('r').toLowerCase() : "console";
        source = cmd.hasOption("d") ? cmd.getOptionValue('d').toLowerCase() : "csfd";

        //Check if user entered film name. If yes, get the entered value into variable filmname. If not, change back to default film
        filmname = cmd.getArgs().length >= 1 ? cmd.getArgs()[0] : null;
        if (filmname == null)
        {
            System.err.println(StringUtils.concat("No film specifed, falling back to default film ", FALLBACK_FILM));
            filmname = FALLBACK_FILM;
        }

        //Get datasource class
        try {
            dsclass = Class.forName(StringUtils.concat(DATASOURCE_PACKAGE, StringUtils.capitalizeFirst(source), "Datasource"));
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown datasource! Falling back to default...");
            dsclass = Class.forName(StringUtils.concat(DATASOURCE_PACKAGE, FALLBACK_DATASOURCE ,"Datasource"));
        }

        //Get class constructor and try to create instance.
        Constructor<?> dsctor = dsclass.getConstructor();
        try {
            datasource = (IDatasource) dsctor.newInstance();
        } catch (Exception e) {
            System.err.println("Internal error occured while trying to create Datasource. Dying...");
            if (DEBUG) e.printStackTrace();
            return;
        }

        //Get renderer class
        try {
            rclass = Class.forName(StringUtils.concat(RENDERER_PACKAGE, StringUtils.capitalizeFirst(renderer), "Renderer"));
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown renderer! Falling back to default...");
            rclass = Class.forName(StringUtils.concat(RENDERER_PACKAGE, FALLBACK_RENDERER, "Renderer" ));
        }

        //Get constructor and try to
        Constructor<?> rctor = rclass.getConstructor(Film.class);
        try {
            film = datasource.getFilmByName(filmname);
        } catch (IOException e) {
            System.err.println("An unknown I/O exception occured. Dying...");
            if (DEBUG) e.printStackTrace();
            System.exit(-1);
        } catch (FilmNotFoundException e) {
            System.err.println("The film you entered cannot be found. Dying...");
            if (DEBUG) e.printStackTrace();
            System.exit(-1);
        } catch (JSONException e) {
            System.err.println("An exception occurred while parsing reply from server. Dying...");
            if (DEBUG) e.printStackTrace();
            System.exit(-1);
        }

        //Try to create renderer and render the film.
        try {
            rendererc = (IRenderer) rctor.newInstance(new Object[] {film});
        } catch (Exception e) {
            System.err.println("An unspecifed exception occured while trying to render film details. Dying...");
            if (DEBUG) e.printStackTrace();
            System.exit(-1);
        }
        rendererc.renderFilm();
    }
}

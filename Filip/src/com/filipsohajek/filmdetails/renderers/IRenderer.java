package com.filipsohajek.filmdetails.renderers;

public interface IRenderer {
    /**
     * Render complete film details
     */
    public void renderFilm();

    /**
     * Render film birth year
     */
    public void renderBirth();

    /**
     * Render film's names
     */
    public void renderNames();

    /**
     * Render film's plot
     */
    public void renderPlot();

    /**
     * Render film's genres
     */
    public void renderGenres();

    /**
     * Render film's rating
     */
    public void renderRating();

    /**
     * Render film's runtime
     */
    public void renderRuntime();

    /**
     * Render film's authors
     */
    public void renderAuthors();
}

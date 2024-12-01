package news_search;

/**
 * Input Boundary for actions which are related to news search.
 */

public interface NewsSearchInputBoundary {

    /**
     * Executes the news search use case.
     * @param newsSearchInputData the input data
     */
    void execute(NewsSearchInputData newsSearchInputData);
}

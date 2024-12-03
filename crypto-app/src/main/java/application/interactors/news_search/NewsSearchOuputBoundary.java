package application.interactors.news_search;

/**
 * The output boundary for the News Search Use Case.
 */
public interface NewsSearchOuputBoundary {
    /**
     * Prepares the success view for the News Search Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(NewsSearchOutputData outputData);

    /**
     * Prepares the failure view for the News Search Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}

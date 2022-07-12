package hirs.persist;

/**
 * This class represents an <code>Exception</code> generated by a
 * <code>ReportSumyManageer</code>.
 */
public class ReportSummaryManagerException extends RuntimeException {

    private static final long serialVersionUID = 3081536085161873284L;

    /**
     * Creates a new <code>ReportSumMgrException</code> that has the message
     * <code>msg</code>.
     * @param msg
     *            exception message
     */
    public ReportSummaryManagerException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new <code>ReportSumMgrException</code> that wraps the given
     * <code>Throwable</code>.
     * @param t
     *            root cause
     */
    public ReportSummaryManagerException(final Throwable t) {
        super(t);
    }

    /**
     * Creates a new <code>ReportSumMgrException</code> that has the message
     * <code>msg</code> and wraps the root cause.
     * @param msg
     *            exception message
     * @param t
     *            root cause
     */
    public ReportSummaryManagerException(final String msg, final Throwable t) {
        super(msg, t);
    }
}

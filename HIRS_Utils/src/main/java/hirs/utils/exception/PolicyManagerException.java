package hirs.utils.exception;

/**
 * This class represents an <code>Exception</code> generated by a
 * <code>PolicyManager</code>.
 */
public class PolicyManagerException extends RuntimeException {

    private static final long serialVersionUID = 3081536085161873284L;

    /**
     * Creates a new <code>PolicyManagerException</code> that has the message
     * <code>msg</code>.
     *
     * @param msg
     *            exception message
     */
    public PolicyManagerException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new <code>PolicyManagerException</code> that wraps the given
     * <code>Throwable</code>.
     *
     * @param t
     *            root cause
     */
    public PolicyManagerException(final Throwable t) {
        super(t);
    }

    /**
     * Creates a new <code>PolicyManagerException</code> that has the message
     * <code>msg</code> and wraps the root cause.
     *
     * @param msg
     *            exception message
     * @param t
     *            root cause
     */
    public PolicyManagerException(final String msg, final Throwable t) {
        super(msg, t);
    }
}

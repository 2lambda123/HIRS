package hirs.persist;

/**
 * This class represents an <code>Exception</code> generated by a
 * <code>TPMDeviceStateManageer</code>.
 */
public class TPMDeviceStateManagerException extends RuntimeException {

    private static final long serialVersionUID = 1266522688839309858L;

    /**
     * Creates a new <code>TPMDeviceStateManagerException</code> that has the
     * message <code>msg</code>.
     *
     * @param msg
     *            exception message
     */
    TPMDeviceStateManagerException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new <code>TPMDeviceStateManagerException</code> that wraps the
     * given <code>Throwable</code>.
     *
     * @param t
     *            root cause
     */
    TPMDeviceStateManagerException(final Throwable t) {
        super(t);
    }

    /**
     * Creates a new <code>TPMDeviceStateManagerException</code> that has the
     * message <code>msg</code> and wraps the root cause.
     *
     * @param msg
     *            exception message
     * @param t
     *            root cause
     */
    TPMDeviceStateManagerException(final String msg, final Throwable t) {
        super(msg, t);
    }

}
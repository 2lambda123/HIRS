package hirs.persist;

import hirs.data.persist.Device;
import hirs.data.persist.DeviceState;
import hirs.data.persist.alert.AlertServiceConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


/**
 * A <code>DBDeviceStateManager</code> manages <code>DeviceState</code> objects using a database.
 */
public class DBDeviceStateManager extends DBManager<DeviceState> implements DeviceStateManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<DeviceState> criteriaQuery;

    /**
     * Creates a new <code>DBDeviceStateManager</code> that uses the default
     * database. The default database is used to store all of the
     * <code>DeviceState</code> objects.
     *
     * @param sessionFactory session factory used to access database connections
     */
    public DBDeviceStateManager(final SessionFactory sessionFactory) {
        super(DeviceState.class, sessionFactory);
    }

    /**
     * Saves the <code>DeviceState</code> in the database and returns it.
     *
     * @param state
     *            state to save
     * @return <code>DeviceState</code> that was saved
     * @throws DeviceStateManagerException
     *             if state has previously been saved or an error occurs while
     *             trying to save it to the database
     */
    @Override
    public final DeviceState saveState(final DeviceState state)
            throws DeviceStateManagerException {
        LOGGER.debug("saving state: {}", state);
        try {
            return save(state);
        } catch (DBManagerException e) {
            throw new DeviceStateManagerException(e);
        }
    }

    /**
     * Returns the <code>DeviceState</code> for a <code>Device</code>.
     *
     * @param device
     *            device
     * @param clazz
     *            Class to specify which type of <code>DeviceState</code> to retrieve
     * @return state
     * @throws DeviceStateManagerException
     *             if state has not previously been saved or an error occurs
     *             while trying to retrieve it from the database
     */
    @Override
    public final DeviceState getState(final Device device, final Class<? extends DeviceState> clazz)
            throws DeviceStateManagerException {
        LOGGER.debug("getting state for device: {}", device);
        if (device == null) {
            LOGGER.error("null device argument");
            throw new NullPointerException("null device");
        }

        DeviceState ret;
        Transaction tx = null;
        Session session = getFactory().getCurrentSession();
        try {
            LOGGER.debug("retrieving state from db");
            tx = session.beginTransaction();
            // replacement code for session.createCriteria
            criteriaBuilder = session.getCriteriaBuilder();
            criteriaQuery = criteriaBuilder.createQuery(DeviceState.class);
            criteriaQuery.from(DeviceState.class);
            Root<DeviceState> root = criteriaQuery.from(DeviceState.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("device"), device));
            ret = session.createQuery(criteriaQuery).getSingleResult();

            tx.commit();
        } catch (Exception e) {
            final String msg = "unable to retrieve object";
            LOGGER.error(msg, e);
            if (tx != null) {
                LOGGER.debug("rolling back transaction");
                tx.rollback();
            }
            throw new DBManagerException(msg, e);
        }
        return ret;
    }

    /**
     * Returns a <code>List</code> of the <code>DeviceStates</code> associated with the
     * <code>Device</code>.  If there are no states are associated, an empty list is returned.
     *
     * @param device
     *            device
     * @return list of device states
     * @throws DeviceStateManagerException
     *             if any unexpected errors occur while trying to retrieve the states
     */
    @Override
    public final List<DeviceState> getStates(final Device device) throws
            DeviceStateManagerException {
        LOGGER.debug("getting all states for device: {}", device);
        if (device == null) {
            LOGGER.error("null device argument");
            throw new NullPointerException("null device");
        }

        try {
            return super.getList(DeviceState.class);
        } catch (DBManagerException e) {
            throw new DeviceStateManagerException(e);
        }
    }

    /**
     * Updates a <code>DeviceState</code>. This updates the database entries
     * to reflect the new values that should be set.
     *
     * @param state
     *            state
     * @throws DeviceStateManagerException
     *             if state has not previously been saved or an error occurs
     *             while trying to save it to the database
     */
    @Override
    public final void updateState(final DeviceState state)
            throws DeviceStateManagerException {
        LOGGER.debug("updating state: {}", state);
        try {
            super.update(state);
        } catch (DBManagerException e) {
            throw new DeviceStateManagerException(e);
        }
    }

    /**
     * Deletes the <code>DeviceState</code> from the database.
     *
     * @param device
     *            device whose state is to be remove
     * @param clazz
     *            Class to specify which type of <code>DeviceState</code> to delete
     * @return true if successfully found and deleted the
     *         <code>DeviceState</code>
     * @throws DeviceStateManagerException
     *             if any unexpected errors occur while trying to delete it from
     *             the database
     */
    @Override
    public final boolean deleteState(final Device device, final Class<? extends DeviceState> clazz)
            throws DeviceStateManagerException {
        LOGGER.debug("deleting state for device: {}", device);
        if (device == null) {
            LOGGER.error("null device argument");
            throw new NullPointerException("null device");
        }

        boolean ret = false;
        Transaction tx = null;
        Session session = getFactory().getCurrentSession();
        try {
            LOGGER.debug("retrieving state from db");
            tx = session.beginTransaction();
            // replacement code for session.createCriteria
            criteriaBuilder = session.getCriteriaBuilder();
            criteriaQuery = criteriaBuilder.createQuery(DeviceState.class);
            criteriaQuery.from(DeviceState.class);
            Root<DeviceState> root = criteriaQuery.from(DeviceState.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("device"), device));
            final DeviceState state = session.createQuery(criteriaQuery).getSingleResult();

            if (state != null) {
                session.delete(state);
                ret = true;
            }
            tx.commit();
        } catch (Exception e) {
            final String msg = "unable to retrieve object";
            LOGGER.error(msg, e);
            if (tx != null) {
                LOGGER.debug("rolling back transaction");
                tx.rollback();
            }
            throw new DBManagerException(msg, e);
        }
        return ret;
    }

}

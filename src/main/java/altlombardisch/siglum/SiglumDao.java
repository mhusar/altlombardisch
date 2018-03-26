package altlombardisch.siglum;

import altlombardisch.data.EntityManagerListener;
import altlombardisch.data.GenericDao;
import org.hibernate.StaleObjectStateException;
import org.hibernate.UnresolvableObjectException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Data Access Object providing data operations for siglums.
 */
public class SiglumDao extends GenericDao<Siglum> implements ISiglumDao {
    /**
     * Creates an instance of a SiglumDao.
     */
    public SiglumDao() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient(Siglum siglum) {
        return siglum.getId() == null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    public Siglum refresh(Siglum siglum) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            if (isTransient(siglum)) {
                throw new IllegalArgumentException();
            }

            TypedQuery<Siglum> query = entityManager.createQuery("SELECT s FROM Siglum s " +
                    "WHERE s.id = :id", Siglum.class);
            Siglum refreshedSiglum = query.setParameter("id", siglum.getId()).getSingleResult();
            transaction.commit();
            return refreshedSiglum;
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    public void persist(Siglum siglum) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        if (siglum.getUuid() == null) {
            siglum.setUuid(UUID.randomUUID().toString());
        }

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(siglum);
            transaction.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            if (e instanceof OptimisticLockException || e instanceof StaleObjectStateException) {
                panicOnSaveLockingError(siglum, e);
            } else if (e instanceof UnresolvableObjectException) {
                panicOnSaveUnresolvableObjectError(siglum, e);
            } else {
                throw e;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    public Siglum merge(Siglum siglum) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Siglum mergedSiglum = entityManager.merge(siglum);
            mergedSiglum = entityManager.merge(mergedSiglum);
            transaction.commit();
            return mergedSiglum;
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            if (e instanceof OptimisticLockException || e instanceof StaleObjectStateException) {
                panicOnSaveLockingError(siglum, e);
            } else if (e instanceof UnresolvableObjectException) {
                panicOnSaveUnresolvableObjectError(siglum, e);
            } else {
                throw e;
            }

            return null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    @Override
    public void remove(Siglum siglum) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            Siglum mergedSiglum = entityManager.merge(siglum);

            entityManager.remove(mergedSiglum);
            transaction.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    @Override
    public Siglum findById(Integer id) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<Siglum> query = entityManager.createQuery("SELECT s FROM Siglum s " +
                    "WHERE s.id = :id", Siglum.class);
            List<Siglum> siglumList = query.setParameter("id", id).getResultList();
            transaction.commit();

            if (siglumList.isEmpty()) {
                return null;
            } else {
                return siglumList.get(0);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws RuntimeException
     */
    @Override
    public Siglum findByName(String name) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<Siglum> query = entityManager.createQuery("SELECT s FROM Siglum s " +
                    "WHERE s.name = :name", Siglum.class);
            List<Siglum> siglumList = query.setParameter("name", name).getResultList();
            transaction.commit();

            if (siglumList.isEmpty()) {
                return null;
            } else {
                return siglumList.get(0);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws RuntimeException
     */
    @Override
    public List<Siglum> findAll(String substring) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<Siglum> query = entityManager.createQuery("SELECT s FROM Siglum s " +
                    "WHERE s.name LIKE :substring", Siglum.class);
            List<Siglum> lemmaList = query.setParameter("substring", substring + "%").getResultList();
            transaction.commit();
            return lemmaList;
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        } finally {
            entityManager.close();
        }
    }
}

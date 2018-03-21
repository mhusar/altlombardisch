package altlombardisch.xml.attribute;

import altlombardisch.data.EntityManagerListener;
import altlombardisch.data.GenericDao;
import altlombardisch.xml.tag.XmlTagDefinition;
import org.hibernate.StaleObjectStateException;
import org.hibernate.UnresolvableObjectException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Data Access Object providing data operations for XML attribute definitions.
 */
public class XmlAttributeDefinitionDao extends GenericDao<XmlAttributeDefinition> implements
        IXmlAttributeDefinitionDao {
    /**
     * Creates an instance of XmlAttributeDefinitionDao.
     */
    public XmlAttributeDefinitionDao() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient(XmlAttributeDefinition definition) {
        return definition.getId() == null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    public void persist(XmlAttributeDefinition definition) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        if (definition.getUuid() == null) {
            definition.setUuid(UUID.randomUUID().toString());
        }

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(definition);
            transaction.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            if (e instanceof OptimisticLockException || e instanceof StaleObjectStateException) {
                panicOnSaveLockingError(definition, e);
            } else if (e instanceof UnresolvableObjectException) {
                panicOnSaveUnresolvableObjectError(definition, e);
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
    public XmlAttributeDefinition merge(XmlAttributeDefinition definition) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            XmlAttributeDefinition mergedDefinition = entityManager.merge(definition);
            transaction.commit();
            return mergedDefinition;
        } catch (RuntimeException e) {
            e.printStackTrace();

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            if (e instanceof OptimisticLockException || e instanceof StaleObjectStateException) {
                panicOnSaveLockingError(definition, e);
            } else if (e instanceof UnresolvableObjectException) {
                panicOnSaveUnresolvableObjectError(definition, e);
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
    public void remove(XmlAttributeDefinition definition) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            XmlAttributeDefinition mergedDefinition = entityManager.merge(definition);

            mergedDefinition.getTagDefinition().getAttributeDefinitions().remove(mergedDefinition);
            entityManager.remove(mergedDefinition);
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
    public XmlAttributeDefinition findById(Integer id) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlAttributeDefinition> query = entityManager.createQuery("SELECT x " +
                    "FROM XmlAttributeDefinition x WHERE x.id = :id", XmlAttributeDefinition.class);
            List<XmlAttributeDefinition> definitionList = query.setParameter("id", id).getResultList();
            transaction.commit();

            if (definitionList.isEmpty()) {
                return null;
            } else {
                return definitionList.get(0);
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
    public XmlAttributeDefinition findByName(XmlTagDefinition parent, String name) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlAttributeDefinition> query = entityManager.createQuery("SELECT x " +
                            "FROM XmlAttributeDefinition x WHERE x.tagDefinition = :tagDefinition AND x.name = :name",
                    XmlAttributeDefinition.class);
            List<XmlAttributeDefinition> definitionList = query.setParameter("tagDefinition", parent)
                    .setParameter("name", name).getResultList();
            transaction.commit();

            if (definitionList.isEmpty()) {
                return null;
            } else {
                return definitionList.get(0);
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
    public XmlAttributeDefinition findFirst(XmlTagDefinition parent) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlAttributeDefinition> query = entityManager.createQuery("SELECT x " +
                            "FROM XmlAttributeDefinition x WHERE x.tagDefinition = :tagDefinition ORDER BY x.name",
                    XmlAttributeDefinition.class);
            List<XmlAttributeDefinition> definitionList = query.setParameter("tagDefinition", parent)
                    .setFirstResult(0).setMaxResults(1).getResultList();
            transaction.commit();

            if (definitionList.isEmpty()) {
                return null;
            } else {
                return definitionList.get(0);
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
    @SuppressWarnings("unchecked")
    public List<XmlAttributeDefinition> findAll(XmlTagDefinition parent) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlAttributeDefinition> query = entityManager.createQuery("SELECT x " +
                            "FROM XmlAttributeDefinition x WHERE x.tagDefinition = :tagDefinition ORDER BY x.name",
                    XmlAttributeDefinition.class);
            List<XmlAttributeDefinition> definitionList = query.setParameter("tagDefinition", parent).getResultList();
            transaction.commit();
            return definitionList;
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

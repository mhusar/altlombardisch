package altlombardisch.xml.tag;

import altlombardisch.data.EntityManagerListener;
import altlombardisch.data.GenericDao;
import altlombardisch.xml.document.XmlDocumentDefinition;
import org.hibernate.StaleObjectStateException;
import org.hibernate.UnresolvableObjectException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Data Access Object providing data operations for XML tag
 * definitions.
 */
public class XmlTagDefinitionDao extends GenericDao<XmlTagDefinition> implements IXmlTagDefinitionDao {
    /**
     * Creates an instance of XmlTagDefinitionDao.
     */
    public XmlTagDefinitionDao() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient(XmlTagDefinition definition) {
        return definition.getId() == null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    public void persist(XmlTagDefinition definition) throws RuntimeException {
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
    public XmlTagDefinition merge(XmlTagDefinition definition) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            XmlTagDefinition mergedDefinition = entityManager.merge(definition);
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
    public void remove(XmlTagDefinition definition) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            XmlTagDefinition mergedDefinition = entityManager.merge(definition);

            mergedDefinition.getDocumentDefinition().getTagDefinitions().remove(mergedDefinition);
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
    public XmlTagDefinition findById(Integer id) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlTagDefinition> query = entityManager.createQuery("SELECT x " +
                    "FROM XmlTagDefinition x WHERE x.id = :id", XmlTagDefinition.class);
            List<XmlTagDefinition> definitionList = query.setParameter("id", id).getResultList();
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
    public XmlTagDefinition findByName(XmlDocumentDefinition parent, String name) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlTagDefinition> query = entityManager.createQuery("SELECT x FROM XmlTagDefinition x " +
                            "WHERE x.documentDefinition = :documentDefinition AND x.name = :name",
                    XmlTagDefinition.class);
            List<XmlTagDefinition> definitionList = query.setParameter("documentDefinition", parent)
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
    public XmlTagDefinition findFirst(XmlDocumentDefinition parent) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlTagDefinition> query = entityManager.createQuery("SELECT x " +
                            "FROM XmlTagDefinition x WHERE x.documentDefinition = :documentDefinition ORDER BY x.name",
                    XmlTagDefinition.class);
            List<XmlTagDefinition> definitionList = query.setParameter("documentDefinition", parent)
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
    public List<XmlTagDefinition> findAll(XmlDocumentDefinition parent) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlTagDefinition> query = entityManager.createQuery("SELECT x " +
                            "FROM XmlTagDefinition x WHERE x.documentDefinition = :documentDefinition ORDER BY x.name",
                    XmlTagDefinition.class);
            List<XmlTagDefinition> definitionList = query.setParameter("documentDefinition", parent).getResultList();
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

package altlombardisch.xml.document;

import altlombardisch.data.EntityManagerListener;
import altlombardisch.data.GenericDao;
import altlombardisch.xml.XmlHelper;
import org.hibernate.StaleObjectStateException;
import org.hibernate.UnresolvableObjectException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Data Access Object providing data operations for XML document definitions.
 */
public class XmlDocumentDefinitionDao extends GenericDao<XmlDocumentDefinition> implements IXmlDocumentDefinitionDao {
    /**
     * Identifiers of document definitions needed for the application to work.
     */
    private static final String[] IDENTIFIERS = {"fontMarkup", "siglumTextMarkup"};

    /**
     * Creates an instance of XmlDocumentDefinitionDao.
     */
    public XmlDocumentDefinitionDao() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isTransient(XmlDocumentDefinition definition) {
        return definition.getId() == null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    public void persist(XmlDocumentDefinition definition) throws RuntimeException {
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
    @Override
    public void initialize() throws RuntimeException {
        for (String identifier : IDENTIFIERS) {
            EntityManager entityManager = EntityManagerListener.createEntityManager();
            EntityTransaction transaction = null;
            XmlDocumentDefinition definition = null;

            try {
                transaction = entityManager.getTransaction();
                transaction.begin();

                TypedQuery<XmlDocumentDefinition> query = entityManager.createQuery("SELECT x " +
                                "FROM XmlDocumentDefinition x WHERE x.identifier = :identifier",
                        XmlDocumentDefinition.class);
                List<XmlDocumentDefinition> definitionList = query.setParameter("identifier", identifier)
                        .getResultList();

                if (!definitionList.isEmpty()) {
                    transaction.commit();
                    continue;
                }

                definition = new XmlDocumentDefinition();

                definition.setRootElement("document");
                definition.setSchema(XmlHelper.EMPTY_SCHEMA);
                definition.setIdentifier(identifier);
                definition.setUuid(UUID.randomUUID().toString());
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
    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException
     */
    @Override
    public XmlDocumentDefinition findById(Integer id) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlDocumentDefinition> query = entityManager.createQuery("SELECT x " +
                    "FROM XmlDocumentDefinition x WHERE x.id = :id", XmlDocumentDefinition.class);
            List<XmlDocumentDefinition> definitionList = query.setParameter("id", id).getResultList();
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
    public XmlDocumentDefinition findByIdentifier(String identifier) throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlDocumentDefinition> query = entityManager.createQuery("SELECT x " +
                    "FROM XmlDocumentDefinition x WHERE x.identifier = :identifier", XmlDocumentDefinition.class);
            List<XmlDocumentDefinition> definitionList = query.setParameter("identifier", identifier).getResultList();
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
    public XmlDocumentDefinition findFirst() throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlDocumentDefinition> query = entityManager.createQuery("SELECT x " +
                            "FROM XmlDocumentDefinition x ORDER BY x.identifier",
                    XmlDocumentDefinition.class);
            List<XmlDocumentDefinition> definitionList = query.setFirstResult(0).setMaxResults(1).getResultList();
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
    public List<XmlDocumentDefinition> findAll() throws RuntimeException {
        EntityManager entityManager = EntityManagerListener.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            TypedQuery<XmlDocumentDefinition> query = entityManager.createQuery("SELECT x " +
                    "FROM XmlDocumentDefinition x ORDER BY x.identifier", XmlDocumentDefinition.class);
            List<XmlDocumentDefinition> definitionList = query.getResultList();
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

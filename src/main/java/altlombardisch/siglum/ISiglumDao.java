package altlombardisch.siglum;

import altlombardisch.data.IDao;

import java.util.List;

/**
 * Defines a siglum DAO by extending interface IDao.
 */
public interface ISiglumDao extends IDao<Siglum> {
    /**
     * Returns the matching siglum for a given ID.
     *
     * @param id the ID of a matching siglum
     * @return The matching siglum or null.
     */
    Siglum findById(Integer id);

    /**
     * Returns the matching siglum for a given name.
     * 
     * @param name
     *            the name of a siglum
     * @return The matching siglum or null.
     */
    Siglum findByName(String name);

    /**
     * Returns a list of matching siglums for a given substring.
     *
     * @param substring substring of a siglum name
     * @return A list of siglums.
     */
    List<Siglum> findAll(String substring);
    
    /**
     * Returns the first siglum of a package.
     * 
     * @return A siglum or null.
     */
    Siglum getFirstSiglum();

    /**
     * Returns the ID of the next siglum.
     * 
     * @param siglum the latest a siglum
     * @return A positive integer or -1.
     */
    Integer getNextSiglumId(Siglum siglum);
}

package com.zombie.infrastructure;

import com.zombie.domain.Item;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;

/**
 * Item dao manager
 *
 * @author mvurts
 */
@Repository
public class ItemManager {

    @PersistenceContext
    private EntityManager em;


    /**
     * Save item to database
     *
     * @param item
     * @return new {@link Item} instance
     */
    @Transactional
    public Item save(Item item) {
        return em.merge(item);
    }

    @Transactional
    public void saveAll(Collection<Item> items) {
        for (Item item : items) {
            em.merge(item);
        }
    }

    /**
     * Better solution is schema versioning where all entities have relation to some schema version (instead of truncate before data loading)
     */
    @Deprecated
    @Transactional
    public void truncate() {
        em.createNativeQuery("truncate table t_item cascade").executeUpdate();
    }

}

package com.zombie.infrastructure;

import com.zombie.domain.Item;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}

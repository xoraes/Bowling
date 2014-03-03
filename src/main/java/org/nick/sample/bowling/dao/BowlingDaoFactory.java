package org.nick.sample.bowling.dao;

/**
 * <p>Factory for getting the dao object</p>
 */
public class BowlingDaoFactory {

    public static BowlingDao getDao() {
        BowlingDao inMemoryDaoImpl = BowlingDaoImpl.getInstance();
        return inMemoryDaoImpl;
    }
}

package org.nick.sample.bowling.dao;

/**
 * <p>Factory for getting the dao object. We should favor DI framework like guice
 * here instead of this factory class as it will allow for easy mocking</p>
 */
public class BowlingDaoFactory {

    public static BowlingDao getDao() {
        BowlingDao daoImpl = BowlingDaoImpl.getInstance();
        return daoImpl;
    }
}

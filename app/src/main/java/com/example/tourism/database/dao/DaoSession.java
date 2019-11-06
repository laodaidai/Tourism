package com.example.tourism.database.dao;

import com.example.tourism.database.bean.SeachContent;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig seachContentDaoConfig;

    private final SeachContentDao seachContentDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        seachContentDaoConfig = daoConfigMap.get(SeachContentDao.class).clone();
        seachContentDaoConfig.initIdentityScope(type);

        seachContentDao = new SeachContentDao(seachContentDaoConfig, this);

        registerDao(SeachContent.class, seachContentDao);
    }
    
    public void clear() {
        seachContentDaoConfig.clearIdentityScope();
    }

    public SeachContentDao getSeachContentDao() {
        return seachContentDao;
    }

}
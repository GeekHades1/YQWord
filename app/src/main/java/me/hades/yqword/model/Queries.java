package me.hades.yqword.model;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import me.hades.yqword.utils.CommonValues;


/**
 * Created by Hades on 2018/6/9.
 *
 * 单词记忆查询工具
 */

public class Queries {
    WordDao wordDao;

    private static Queries ourInstance = null;

    public static Queries getInstance(DaoSession daoSession) {
        if (ourInstance == null) {
            ourInstance = new Queries(daoSession);
        }
        return ourInstance;
    }

    private Queries(DaoSession daoSession) {
        wordDao = daoSession.getWordDao();

    }

    public List<Word> getList(String state, boolean coreMode, boolean easyMode) {
        QueryBuilder<Word> queryBuilder = wordDao.queryBuilder();
        switch (state) {
            case CommonValues.NEVER_SHOW:
                queryBuilder.where(WordDao.Properties.NeverShow.eq("1"));
                break;
            case CommonValues.NOT_LEARNED:
                queryBuilder.where(WordDao.Properties.KnowTime.isNull()).
                        where(WordDao.Properties.UnknownTime.isNull()).where(WordDao.Properties.NeverShow.isNull());
                break;
            case CommonValues.KNOWED:
                queryBuilder.where(WordDao.Properties.KnowTime.gt("0")).where(WordDao.Properties.NeverShow.isNull()).orderDesc(WordDao.Properties.KnowTime);
                break;
            case CommonValues.NEED_LEARN:
                queryBuilder.where(WordDao.Properties.NeverShow.isNull());
                break;
            case CommonValues.EASY:
                return queryBuilder.where(WordDao.Properties.Easy.eq(true)).list();


        }
        if (coreMode) {
            queryBuilder.where(WordDao.Properties.Hot.eq(1));
        }
        if (easyMode) {
            queryBuilder.where(WordDao.Properties.Easy.notEq(true));
        }
        return queryBuilder.list();
    }
}

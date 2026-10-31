package com.aptech.boutquetShop.utils.page;

import com.aptech.boutquetShop.utils.DataUtil;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;

public class PagingRepo<T> {
    private final EntityManager em;
    private final Class<T> genericClass;
    private String totalSql;
    private String getSql;

    public PagingRepo(EntityManager em, Class<T> genericClass) {
        this.em = em;
        this.genericClass = genericClass;
    }

    public PagingRepo<T> with(String totalSql, String getSql) {
        this.totalSql = totalSql;
        this.getSql = getSql;
        return this;
    }

    public PagingRepo<T> withGet(String getSql) {
        this.getSql = getSql;
        return this;
    }

    public PagingRepo<T> withTotal(String totalSql) {
        this.totalSql = totalSql;
        return this;
    }

    public Page<T> query(Integer page, Integer size, Map map){
        Query countQuery = em.createQuery(totalSql);
        countQuery = buildQueryByMapCondition(countQuery,map);
        Query selectQuery = em.createQuery(getSql, Tuple.class);
        selectQuery = buildQueryByMapCondition(selectQuery,map);
        return execute(page, size,countQuery, selectQuery);
    }

    private Page execute(int page, int size, Query countQuery, Query selectQuery) {
        if(size > 100000){
            throw new RuntimeException();
        }

        Long total = 0L;
        total = (Long)countQuery.getSingleResult(); // lay tong so ban ghi

        selectQuery.setFirstResult(size * (page - 1));
        selectQuery.setMaxResults(size);

        List result = DataUtil.convertFromQueryResult(selectQuery.getResultList(), genericClass);
        return new Page<T>(page,size).withResult(result).compute(total.intValue());

    }

    private Query buildQueryByMapCondition(Query query,Map<String, Object> map){
        try {
            if(!map.isEmpty()){
                for(Map.Entry<String,Object> entry : map.entrySet()){
                    query.setParameter(entry.getKey(),entry.getValue());
                }
            }
        }catch (Exception ex){
            System.out.println("buildQueryByMapCondition " + ex.getMessage());
        }
        return query;
    }

}
package org.chwin.firefighting.apiserver.test;



import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.hibernate.query.internal.QueryImpl;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.*;

public class JpaTest<main> {
    public static void main(String[] args) {
        Map m = Maps.newLinkedHashMap();
        try {

            m.put("a", "xx");
            m.put("@注解1", "notnull");
            System.out.println(JSON.toJSONString(m));
            throw new RuntimeException("someworee");
        }catch(Exception e)
        { //  System.out.println(JSON.toJSONString(e,true));

        }

        EntityManagerFactory factory =new EntityManagerFactory(){
            @Override
            public EntityManager createEntityManager() {
                return new EntityManager(){
                    @Override
                    public void persist(Object o) {

                    }

                    @Override
                    public <T> T merge(T t) {
                        return null;
                    }

                    @Override
                    public void remove(Object o) {

                    }

                    @Override
                    public <T> T find(Class<T> aClass, Object o) {
                        return null;
                    }

                    @Override
                    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
                        return null;
                    }

                    @Override
                    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
                        return null;
                    }

                    @Override
                    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
                        return null;
                    }

                    @Override
                    public <T> T getReference(Class<T> aClass, Object o) {
                        return null;
                    }

                    @Override
                    public void flush() {

                    }

                    @Override
                    public void setFlushMode(FlushModeType flushModeType) {

                    }

                    @Override
                    public FlushModeType getFlushMode() {
                        return null;
                    }

                    @Override
                    public void lock(Object o, LockModeType lockModeType) {

                    }

                    @Override
                    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {

                    }

                    @Override
                    public void refresh(Object o) {

                    }

                    @Override
                    public void refresh(Object o, Map<String, Object> map) {

                    }

                    @Override
                    public void refresh(Object o, LockModeType lockModeType) {

                    }

                    @Override
                    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {

                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public void detach(Object o) {

                    }

                    @Override
                    public boolean contains(Object o) {
                        return false;
                    }

                    @Override
                    public LockModeType getLockMode(Object o) {
                        return null;
                    }

                    @Override
                    public void setProperty(String s, Object o) {

                    }

                    @Override
                    public Map<String, Object> getProperties() {
                        return null;
                    }

                    @Override
                    public Query createQuery(String s) {
                        return null;
                    }

                    @Override
                    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
                        return null;
                    }

                    @Override
                    public Query createQuery(CriteriaUpdate criteriaUpdate) {
                        return null;
                    }

                    @Override
                    public Query createQuery(CriteriaDelete criteriaDelete) {
                        return null;
                    }

                    @Override
                    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public Query createNamedQuery(String s) {
                        return null;
                    }

                    @Override
                    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public Query createNativeQuery(String s) {
                        return null;
                    }

                    @Override
                    public Query createNativeQuery(String s, Class aClass) {
                        return null;
                    }

                    @Override
                    public Query createNativeQuery(String s, String s1) {
                        return null;
                    }

                    @Override
                    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
                        return null;
                    }

                    @Override
                    public StoredProcedureQuery createStoredProcedureQuery(String s) {
                        return null;
                    }

                    @Override
                    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
                        return null;
                    }

                    @Override
                    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
                        return null;
                    }

                    @Override
                    public void joinTransaction() {

                    }

                    @Override
                    public boolean isJoinedToTransaction() {
                        return false;
                    }

                    @Override
                    public <T> T unwrap(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public Object getDelegate() {
                        return null;
                    }

                    @Override
                    public void close() {

                    }

                    @Override
                    public boolean isOpen() {
                        return false;
                    }

                    @Override
                    public EntityTransaction getTransaction() {
                        return null;
                    }

                    @Override
                    public EntityManagerFactory getEntityManagerFactory() {
                        return null;
                    }

                    @Override
                    public CriteriaBuilder getCriteriaBuilder() {
                        return null;
                    }

                    @Override
                    public Metamodel getMetamodel() {
                        return null;
                    }

                    @Override
                    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public EntityGraph<?> createEntityGraph(String s) {
                        return null;
                    }

                    @Override
                    public EntityGraph<?> getEntityGraph(String s) {
                        return null;
                    }

                    @Override
                    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
                        return null;
                    }
                };
            }

            @Override
            public EntityManager createEntityManager(Map map) {
                return null;
            }

            @Override
            public EntityManager createEntityManager(SynchronizationType synchronizationType) {
                return null;
            }

            @Override
            public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
                return null;
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                return null;
            }

            @Override
            public Metamodel getMetamodel() {
                return null;
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public Map<String, Object> getProperties() {
                return null;
            }

            @Override
            public Cache getCache() {
                return null;
            }

            @Override
            public PersistenceUnitUtil getPersistenceUnitUtil() {
                return null;
            }

            @Override
            public void addNamedQuery(String s, Query query) {

            }

            @Override
            public <T> T unwrap(Class<T> aClass) {
                return null;
            }

            @Override
            public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {

            }
        };
                //Persistence.createEntityManagerFactory("myJpa");
        //创建实体管理类
        EntityManager em = factory.createEntityManager();
        //获取事务对象
        EntityTransaction tx = em.getTransaction();
        //开启事务
    //    tx.begin();


        //保存操作
       // em.persist(m);

        Query query = em.createNativeQuery("select * from class1");
        List result = query.getResultList();
    //    query.
        //提交事务
    //    tx.commit();
        //释放资源
        em.close();
        factory.close();

        System.out.println("---");

    }
}

package cn.itcast.erp.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import cn.itcast.erp.dao.IBaseDao;

/**
 * 通用数据访问层
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDao<T> extends HibernateDaoSupport implements IBaseDao<T> {

    /**
     * T 实际所代表的类型
     */
    private Class<T> entityClass;

    public BaseDao() {
        //获取对象对应的父类的类型
        Type type = this.getClass().getGenericSuperclass();
        //转成带参数，即泛型的类型
        ParameterizedType pType = (ParameterizedType)type;
        //获取参数泛型类型数组
        Type[] types = pType.getActualTypeArguments();
        //由于我们的BaseDao<T>的泛型参数里只有一个类型T，因此数组的第一个元素就是类型T的实际上的类型
        entityClass = (Class<T>)types[0];
    }

    @Resource(name="sessionFactory")
    public void setSf(SessionFactory sf) {
        super.setSessionFactory(sf);
    }

    @Override
    public List<T> getList(T t1, T t2, Object obj) {
        DetachedCriteria dc = getDetachedCriteria(t1, t2, obj);
        return (List<T>) this.getHibernateTemplate().findByCriteria(dc);
    }

    @Override
    public Long getCount(T t1, T t2, Object obj) {
        DetachedCriteria dc = getDetachedCriteria(t1, t2, obj);
        dc.setProjection(Projections.rowCount());
        List<?> list = this.getHibernateTemplate().findByCriteria(dc);
        return (Long)list.get(0);
    }

    @Override
    public List<T> getListByPage(T t1, T t2, Object obj, int startRow, int maxResults) {
        DetachedCriteria dc = getDetachedCriteria(t1, t2, obj);
        dc.addOrder(Order.asc("uuid"));
        return (List<T>) this.getHibernateTemplate().findByCriteria(dc,startRow,maxResults);
    }

    /**
     * 构建查询条件，由子类实现
     * @param t1
     * @param t2
     * @param obj
     * @return
     */
    public abstract DetachedCriteria getDetachedCriteria(T t1, T t2, Object obj);

    @Override
    public void add(T t) {
        this.getHibernateTemplate().save(t);
    }

    @Override
    public void delete(Long uuid) {
        this.getHibernateTemplate().delete(this.getHibernateTemplate().get(entityClass, uuid));
    }

    @Override
    public T get(Serializable uuid) {
        return this.getHibernateTemplate().get(entityClass, uuid);
    }

    @Override
    public void update(T t) {
        this.getHibernateTemplate().update(t);
    }
}

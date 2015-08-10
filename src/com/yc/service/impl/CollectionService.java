package com.yc.service.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.dao.orm.commons.GenericDao;
import com.yc.entity.Collection;
import com.yc.service.ICollectionService;

@Component
public class CollectionService extends GenericService<Collection> implements ICollectionService{

	@Autowired
	GenericDao<Collection> collectionDao;
	
	@Override
	GenericDao<Collection> getDao() {
		return collectionDao;
	}

	@Override
	public List<Collection> getAllByUser(Integer id) {
		return collectionDao.getBy("user.id", id);
	}
	
	/* 查询部分收藏品
	 * (non-Javadoc)
	 * @see com.yc.service.ICollectionService#searchPart()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> searchPart() {
		StringBuffer hql=new StringBuffer("SELECT * FROM collection  ORDER BY collection.id DESC LIMIT 4");
		Query query = collectionDao.getEntityManager().createNativeQuery(hql.toString(), Collection.class);
		List<Collection> collections=query.getResultList();
		return collections;
	}
}

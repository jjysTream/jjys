package com.yc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.dao.orm.commons.GenericDao;
import com.yc.entity.RecommendationTypes;
import com.yc.service.IRecommendationTypesService;

@Component
public class RecommendationTypesService extends GenericService<RecommendationTypes> implements IRecommendationTypesService{

	@Autowired
	GenericDao<RecommendationTypes> typesDao;
	
	@Override
	GenericDao<RecommendationTypes> getDao() {
		return typesDao;
	}

}

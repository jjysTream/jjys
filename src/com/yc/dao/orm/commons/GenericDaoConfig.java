package com.yc.dao.orm.commons;

import org.springframework.context.annotation.*;

import com.yc.entity.Collection;
import com.yc.entity.Recommendation;
import com.yc.entity.RecommendationTypes;
import com.yc.entity.user.Department;
import com.yc.entity.user.MembersFamily;
import com.yc.entity.user.MembersUser;
import com.yc.entity.user.Personnel;

@Configuration
public class GenericDaoConfig {

	@Bean
	public GenericDao<Department> getDepartmentDao() {
		return new GenericDaoSupport<Department>(Department.class);
	}
	@Bean
	public GenericDao<MembersFamily> getMembersFamilyDao() {
		return new GenericDaoSupport<MembersFamily>(MembersFamily.class);
	}
	@Bean
	public GenericDao<MembersUser> getMembersUserDao() {
		return new GenericDaoSupport<MembersUser>(MembersUser.class);
	}
	@Bean
	public GenericDao<Personnel> getPersonnelDao() {
		return new GenericDaoSupport<Personnel>(Personnel.class);
	}
	@Bean
	public GenericDao<Collection> getCollectionDao() {
		return new GenericDaoSupport<Collection>(Collection.class);
	}
	@Bean
	public GenericDao<Recommendation> getRecommendationDao() {
		return new GenericDaoSupport<Recommendation>(Recommendation.class);
	}
	@Bean
	public GenericDao<RecommendationTypes> getRecommendationTypesDao() {
		return new GenericDaoSupport<RecommendationTypes>(RecommendationTypes.class);
	}
}

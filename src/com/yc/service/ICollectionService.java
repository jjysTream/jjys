package com.yc.service;

import java.util.List;

import com.yc.entity.Collection;

public interface ICollectionService extends IGenericService<Collection>{

	/**
	 * 通过用户ID查询用户所有自己的收藏
	 * @param id 用户ID
	 * @return
	 */
	List<Collection> getAllByUser(Integer id);

	/**
	 * 部分收藏展示
	 * @return
	 */
	List<Collection> searchPart();
}

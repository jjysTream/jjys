package com.yc.service;

import java.util.List;

import com.yc.entity.RechargeRecord;

public interface IRechargeRecordService extends IGenericService<RechargeRecord>{

	/**
	 * 通过用户ID 查询最新的续费记录
	 * @param membersUserID
	 * @return
	 */
	List<RechargeRecord> getRechargeRecordByUser(Integer membersUserID);

}

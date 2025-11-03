package com.wrx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.entity.CheckIn;

import java.util.Map;

/**
 * <p>
 * 入住登记表 服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
public interface ICheckInService extends IService<CheckIn> {

    /**
     * 查询所有入住登记信息，包含客户和房间信息
     * @param checkIn 查询条件
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 入住登记列表
     */
    Map<String, Object> selectAllUser(CheckIn checkIn, int pageIndex, int pageSize);
    
    /**
     * 办理退房手续
     * @param checkInId 入住登记ID
     * @return 退房结果
     */
    Map<String, Object> checkOut(Integer checkInId);
    
    /**
     * 根据客户姓名和电话查询入住记录
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 入住记录列表
     */
    Map<String, Object> selectbynameandphone(String customerName, String customerPhone, int pageIndex, int pageSize);
    
    /**
     * 直接添加入住记录
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param roomType 房间类型
     * @param roomNumber 房间编号
     * @return 操作结果
     */
    Map<String, Object> addCheckIn(String customerName, String customerPhone, String roomType, String roomNumber);

}

package com.baomidou.mybatisplus.service.impl;

import com.baomidou.mybatisplus.entity.TChatMessage;
import com.baomidou.mybatisplus.mapper.TChatMessageMapper;
import com.baomidou.mybatisplus.service.ITChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户消息表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Service
public class TChatMessageServiceImpl extends ServiceImpl<TChatMessageMapper, TChatMessage> implements ITChatMessageService {

}

package com.megvii.dzh.spider.service.impl;

import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.mapper.UserTbsMapper;
import com.megvii.dzh.spider.service.IUserTbsService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserTbsServiceImpl extends BaseServiceImpl<UserTbs> implements IUserTbsService {

    @Autowired
    private UserTbsMapper userTbsMapper;

    @Override
    public List<NameValue> getUserLevel() {
        try {
            List<NameValue> list = userTbsMapper.getUserLevel(Constant.getTbName());
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserLevel error {}", e);
        }
        return null;
    }

    @Override
    public List<NameValue> getTbNameWordCloud(int limit) {
        try {
            List<NameValue> list = userTbsMapper.getTbNameWordCloud(limit,Constant.getTbName());
            log.info("---> size {} data {}", list.size());
            return list;
        } catch (Exception e) {
            log.error("getUserLevel error {}", e);
        }
        return null;
    }


}

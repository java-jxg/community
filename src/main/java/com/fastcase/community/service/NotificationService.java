package com.fastcase.community.service;

import com.fastcase.community.Enum.NotificationStatusEnum;
import com.fastcase.community.Enum.NotificationTypeEnum;
import com.fastcase.community.dto.NotificationDTO;
import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.exception.CustomizeErrorCode;
import com.fastcase.community.exception.CustomizeException;
import com.fastcase.community.mapper.NotificationMapper;
import com.fastcase.community.model.Notification;
import com.fastcase.community.model.NotificationExample;
import com.fastcase.community.model.Question;
import com.fastcase.community.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    public PageInfo list(Long userId, Integer page, Integer size) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");

        PageHelper.startPage(page,size);
        List<Notification> notifications = notificationMapper.selectByExample(example);
        PageInfo<Question> pageInfo = new PageInfo(notifications,5);
        return pageInfo;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return Long.valueOf(notificationMapper.countByExample(notificationExample));
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
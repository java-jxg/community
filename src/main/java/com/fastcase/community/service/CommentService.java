package com.fastcase.community.service;

import com.fastcase.community.Enum.CommentTypeEnum;
import com.fastcase.community.Enum.NotificationStatusEnum;
import com.fastcase.community.Enum.NotificationTypeEnum;
import com.fastcase.community.dto.CommentExDTO;
import com.fastcase.community.exception.CustomizeErrorCode;
import com.fastcase.community.exception.CustomizeException;
import com.fastcase.community.mapper.CommentMapper;
import com.fastcase.community.mapper.NotificationMapper;
import com.fastcase.community.mapper.QuestionExMapper;
import com.fastcase.community.mapper.QuestionMapper;
import com.fastcase.community.model.Comment;
import com.fastcase.community.model.Notification;
import com.fastcase.community.model.Question;
import com.fastcase.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExMapper questionExMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }


            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }


            commentMapper.insert(comment);
            comment.setCommentCount(1);
            questionExMapper.incCommentCC(comment);
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());

        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExMapper.incCommentCount(question);

            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    public List<CommentExDTO> listByQuestionId(Long id,CommentTypeEnum type) {
        List<CommentExDTO> comments= questionExMapper.getCommentByParentId(id,type.getType());
        return comments;
    }
    //创建通知
    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }
}

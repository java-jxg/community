package com.fastcase.community.mapper;

import com.fastcase.community.dto.CommentExDTO;
import com.fastcase.community.dto.QuestionDTO;

import com.fastcase.community.model.Comment;
import com.fastcase.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface QuestionExMapper {
    @Select("SELECT * FROM question q \n" +
            "LEFT JOIN USER u ON q.creator=u.id\n")
    List<QuestionDTO> list();

    @Select("SELECT * FROM question q\n" +
            "LEFT JOIN USER u ON q.creator=u.id\n" +
            "WHERE creator = #{userId}")
    List<QuestionDTO> listByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM question q\n" +
            "LEFT JOIN USER u ON q.creator=u.id\n" +
            "WHERE q.id = #{id}")
    QuestionDTO getById(@Param("id") Long id);

    @Update("update QUESTION set VIEW_COUNT = VIEW_COUNT + #{viewCount,jdbcType=INTEGER} where id = #{id}")
    void incView(Question question);
    @Update("update QUESTION set COMMENT_COUNT = COMMENT_COUNT + #{commentCount,jdbcType=INTEGER} where id = #{id}")
    int incCommentCount(Question record);




    @Select("SELECT * FROM COMMENT c\n" +
            "LEFT JOIN USER u ON c.commentator=u.id\n" +
            "WHERE c.type=#{type} AND c.parent_id=#{id} order by c.gmt_create desc")
    List<CommentExDTO> getCommentByParentId(Long id, Integer type);
    @Update("update comment set COMMENT_COUNT = COMMENT_COUNT + #{commentCount,jdbcType=INTEGER} where id = #{parentId}")
    void incCommentCC(Comment comment);

    @Select("select * from QUESTION where id != #{id} and tag regexp #{tag}")
    List<Question> selectRelated(Question question);
}
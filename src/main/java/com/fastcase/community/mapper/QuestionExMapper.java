package com.fastcase.community.mapper;

import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.model.Question;
import com.fastcase.community.model.QuestionExample;
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
}
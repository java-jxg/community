package com.fastcase.community.mapper;

import com.fastcase.community.dto.QuestionDTO;
import com.fastcase.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,comment_count,view_count,like_count,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount},#{tag})")
    void create(Question question);
    @Select("SELECT * FROM question q \n" +
            "LEFT JOIN USER u ON q.creator=u.id\n")
    List<QuestionDTO> list();
    @Select("SELECT * FROM question q\n" +
            "LEFT JOIN USER u ON q.creator=u.id\n" +
            "WHERE creator = #{userId}")
    List<QuestionDTO> listByUserId(@Param("userId") Integer userId);
    @Select("SELECT * FROM question q\n" +
            "LEFT JOIN USER u ON q.creator=u.id\n" +
            "WHERE q.id = #{id}")
    QuestionDTO getById(@Param("id") Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag} where id = #{id}")
    void update(Question question);
}

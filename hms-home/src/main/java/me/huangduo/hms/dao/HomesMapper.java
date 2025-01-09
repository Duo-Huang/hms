package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.HomeEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HomesMapper {

    @Select("SELECT * FROM homes WHERE home_id = #{homeId}")
    HomeEntity getById(Integer homeId);

    List<HomeEntity> getByIds(List<Integer> homeIds);

    @Insert("INSERT INTO homes (home_name, home_description) VALUES (#{homeName}, #{homeDescription})")
    int create(HomeEntity homeEntity);

    int update(HomeEntity homeEntity);

    @Delete("DELETE FROM homes WHERE home_id = #{homeId}") // 只有home admin可以操作, 删除后该家庭相关的所有数据全部清除
    int delete(Integer homeId);
}

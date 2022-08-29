package kh.petmily.mapper;

import kh.petmily.domain.adopt.form.AdoptTempListForm;
import kh.petmily.domain.temp.TempPet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TempMapper {

    TempPet selectByPk(int pk);

    void insert(TempPet obj);

    void update(TempPet obj);

    void delete(int pk);

    int selectCount(int mNumber);

    List<TempPet> selectIndex(@Param("start") int start,@Param("end") int end,@Param("mNumber") int mNumber);

    int selectCount();

    List<AdoptTempListForm> selectIndex(@Param("start") int start, @Param("end") int end, @Param("status") String status);

    List<AdoptTempListForm> tempApprove(int pk);

    List<AdoptTempListForm> tempRefuse(int pk);

}
package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.formgenerator.entity.FormInstance;

/**
 * <p>
 * 表单实例表 Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
public interface FormInstanceMapper extends BaseMapper<FormInstance> {
    /**
     * 生成唯一表单编号（规则：FORM_YYYYMMDD_6位自增数）
     */
    @Select("SELECT CONCAT('FORM_', DATE_FORMAT(NOW(), '%Y%m%d'), '_', LPAD(IFNULL(MAX(SUBSTRING(form_no, 11)), 0) + 1, 6, '0')) " +
            "FROM form_instance WHERE form_no LIKE CONCAT('FORM_', DATE_FORMAT(NOW(), '%Y%m%d'), '_%')")
    String generateUniqueFormNo();

    /**
     * 加锁查询当前日期的最大表单编号（FOR UPDATE 行锁，避免并发查询重复）
     */
    @Select("SELECT CONCAT('FORM_', DATE_FORMAT(NOW(), '%Y%m%d'), '_', LPAD(IFNULL(MAX(SUBSTRING(form_no, 11)), 0) + 1, 6, '0')) " +
            "FROM form_instance " +
            "WHERE form_no LIKE CONCAT('FORM_', DATE_FORMAT(NOW(), '%Y%m%d%s'), '_%') " +
            "FOR UPDATE") // 关键：加行锁，锁定查询结果集，直到事务提交/回滚
    String getMaxFormNoWithLock();

    @Select("<script>" +
            "SELECT CONCAT('FORM_', DATE_FORMAT(NOW(), '%Y%m%d'), '_', LPAD(IFNULL(MAX(SUBSTRING(form_no, 11)), 0) + 1, 6, '0')) " +
            "FROM form_instance " +
            "WHERE form_no LIKE CONCAT('FORM_', DATE_FORMAT(NOW(), '%Y%m%d%s'), '_%') " +
            "FOR UPDATE" + // 关键行：数据库行锁，必须加
            "</script>")
    String selectMaxFormNoWithLock();

    @Update("Update form_instance set is_deleted = 1 where id = #{formId}")
    void softDeleteFromByFormId(@Param("formId")Long formId);
}

package org.example.formgenerator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.formgenerator.entity.TemplateField;

import java.util.List;

/**
 * <p>
 * 模板字段表 Mapper 接口
 * </p>
 *
 * @author Fuuyu
 * @since 2026-01-22
 */
public interface TemplateFieldMapper extends BaseMapper<TemplateField> {
    /**
     * 批量插入模板字段
     */
    int batchInsert(@Param("list") List<TemplateField> fieldList);

    /**
     * 根据模板ID查询字段列表（按排序升序）
     */
    List<TemplateField> selectByTemplateId(@Param("templateId") Long templateId);

    @Select("<script>" +
            "SELECT id, template_id, field_code, field_name, field_type, field_order, default_value, is_deleted " +
            "FROM template_field " +
            "WHERE template_id = #{templateId} " +
            "  AND field_code = #{fieldCode} " +
            "  AND is_deleted = 0 " + // 核心：过滤逻辑删除的字段
            "<if test='excludeId != null'>" + // 动态拼接：编辑时排除自身
            "  AND id != #{excludeId} " +
            "</if>" +
            "</script>")
    TemplateField selectByTemplateIdAndCode(
            @Param("templateId") Long templateId,
            @Param("fieldCode") String fieldCode,
            @Param("excludeId") Long excludeId
    );
}

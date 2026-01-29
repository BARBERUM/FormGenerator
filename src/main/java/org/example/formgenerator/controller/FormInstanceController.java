package org.example.formgenerator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.common.Constant;
import org.example.formgenerator.dto.FormInstanceGenerateDTO;
import org.example.formgenerator.entity.FormData;
import org.example.formgenerator.entity.FormInstance;
import org.example.formgenerator.mapper.FormInstanceMapper;
import org.example.formgenerator.service.FormInstanceService;
import org.example.formgenerator.utils.ApiException;
import org.example.formgenerator.utils.R;
import org.example.formgenerator.vo.FormDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.FormattableFlags;
import java.util.List;

@RestController
@RequestMapping("api/form-instance")
@RequiredArgsConstructor
@Slf4j
public class FormInstanceController {

    @Autowired
    private  final FormInstanceService formInstanceService;

    @PostMapping("/generate")
    public R<?> generatorForm(@Validated FormInstanceGenerateDTO dto){
        try{
            List<Long> fromIdList = formInstanceService.generatorFormInstance(dto);
            return R.success("表单实例生成成功", fromIdList);
        }catch (ApiException e) {
            log.warn("生成表单实例失败：{}", e.getMessage());
            return R.failure( e.getMessage());
        } catch (Exception e) {
            log.error("生成表单实例系统异常", e);
            return R.failure( "系统异常：生成表单实例失败");
        }
    }


    @GetMapping("/detail/{formId}")
    public R<?>formDetail(@PathVariable("formId") Long formId){
        try{
            FormInstance formInstance = formInstanceService.getById(formId);
            if(formInstance == null||formInstance.getIsDeleted().equals(Constant.IS_DELETED_YES)){
                return R.failure("表单实例不存在或已删除");
            }
            List<FormData> formDataList = formInstanceService.getFormDataByFormId(formId);
            FormDetailVO formDetailVO = new FormDetailVO();
            formDetailVO.setFormInstance(formInstance);
            formDetailVO.setFormDataList(formDataList);

            return R.success("成功获取到实例表详细信息", formDetailVO);
        }catch (Exception e) {
        // 全局异常兜底，记录日志+返回友好提示
        log.error("查询表单详情失败，表单ID：{}", formId, e);
        return R.failure("查询表单详情失败，请联系管理员");
        }
    }


    @DeleteMapping("/delete/{formId}")
    public R<?>deleteFormByFormId(@PathVariable("formId") Long formId){
        Long id = formInstanceService.softDeleteFormByFormId(formId);
        return R.success("成功删除id为：{}的实例表格。", id);
    }

}

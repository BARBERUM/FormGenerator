package org.example.formgenerator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.formgenerator.dto.FieldMappingDTO;
import org.example.formgenerator.entity.FieldMapping;
import org.example.formgenerator.service.FieldMappingService;
import org.example.formgenerator.utils.ApiException;
import org.example.formgenerator.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/field-mapping")
@RequiredArgsConstructor
public class FieldMappingController {

    @Autowired
    private final FieldMappingService fieldMappingService;

    @PostMapping("/save")
    public R<?> saveMapping(@Validated FieldMappingDTO dto){
        try{
            fieldMappingService.saveMapping(dto);
            return R.success(dto.getId() == null?"新增映射关系成功":"编辑映射关系失败");
        }catch (ApiException e){
            log.warn("映射关系操作失败：{}", e.getMessage());
            return R.failure(e.getMessage());
        }catch (Exception e){
            log.error("映射关系操作系统异常",e);
            return R.failure("系统异常，映射关系操作失败。");
        }
    }

    @GetMapping("/list")
    public R<?> listMapping(@RequestParam Long templateId, @RequestParam Long sourceId){
        try{
            List<FieldMapping> list = fieldMappingService.listMapping(templateId, sourceId);
            return R.success("成功查询映射关系", list);
        }catch (ApiException e){
            log.warn("查询映射关系失败:{}",e.getMessage());
            return R.failure(e.getMessage());
        }catch (Exception e){
            log.error("查询映射关系系统异常.");
            return  R.failure("系统异常，查询映射关系失败。");
        }
    }

    @DeleteMapping("/remove/{id}")
    public R<?>removeMapping(@PathVariable Long  id){
        try{
            fieldMappingService.removeMapping(id);
            return R.success("删除映射关系成功");
        }catch (ApiException e) {
            log.warn("删除映射关系失败：{}", e.getMessage());
            return R.failure(e.getMessage());
        } catch (Exception e) {
            log.error("删除映射关系系统异常", e);
            return R.failure("系统异常：删除映射关系失败");
        }
    }
}

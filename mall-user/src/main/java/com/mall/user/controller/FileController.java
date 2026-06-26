package com.mall.user.controller;

import com.mall.common.exception.BusinessException;
import com.mall.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Tag(name = "文件上传", description = "文件上传接口")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload.path:D:/CodeWorkPlace/IdeaCode/online-shopping-springboot/images/}")
    private String uploadPath;

    @PostMapping("/upload")
    @Operation(summary = "上传图片")
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }

        // 2. 校验文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }

        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!suffix.equals(".jpg") && !suffix.equals(".jpeg") &&
            !suffix.equals(".png") && !suffix.equals(".gif")) {
            throw new BusinessException("只支持 jpg、jpeg、png、gif 格式的图片");
        }

        // 3. 校验文件大小（10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BusinessException("文件大小不能超过 10MB");
        }

        // 4. 生成唯一文件名
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 5. 创建目录
        File dir = new File(uploadPath + "upload");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 6. 保存文件
        try {
            File dest = new File(dir, newFileName);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }

        // 7. 返回访问路径
        String url = "/images/upload/" + newFileName;
        return R.ok(url);
    }
}

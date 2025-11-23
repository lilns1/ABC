// src/main/java/org/example/abc/dto/ApiResponse.java
package org.example.abc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApiResponse {
    // getters
    @Schema(description = "是否成功", example = "true")
    private boolean success;

    @Schema(description = "消息内容", example = "用户注册成功")
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
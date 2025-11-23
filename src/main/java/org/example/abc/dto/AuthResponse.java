package org.example.abc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthResponse {

    @Schema(description = "是否成功", example = "true")
    private boolean success;

    @Schema(description = "消息内容", example = "登录成功")
    private String message;

    @Schema(description = "登录用户 ID", example = "123")
    private Integer userId;

    @Schema(description = "登录用户名", example = "alice")
    private String username;

    public AuthResponse(boolean success, String message, Integer userId, String username) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.username = username;
    }
}

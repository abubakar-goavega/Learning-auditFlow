package com.abu.auditflow.auth.dto;

import java.util.UUID;

public record ResetPasswordRequest(
        UUID token,
        String newPassword
) {}
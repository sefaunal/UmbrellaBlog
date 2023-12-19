package com.sefaunal.umbrellachat.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author github.com/sefaunal
 * @since 2023-12-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryCodeRequest {
    private String recoveryCode;
}
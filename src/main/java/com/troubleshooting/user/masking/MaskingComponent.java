package com.troubleshooting.user.masking;

import com.troubleshooting.user.configuration.MaskingConfig;
import com.troubleshooting.user.configuration.MaskingObj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.RegExUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MaskingComponent {
    private final MaskingConfig maskingConfig;

    public String maskInfo(String info) {
        List<MaskingObj> replaceArr = maskingConfig.replaceList();
        for (MaskingObj maskingObj : replaceArr) {
            String regexString = maskingObj.regex();
            String replaceString = maskingObj.replace();
            info = RegExUtils.replaceAll(info, regexString, replaceString);
        }
        return info;
    }
}

package com.gangling.scm.base.middleware.email.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailAttachment {
    private String fileName;
    private File file;
    private ByteArrayResource byteArrayResource;

    private String inlineParamName;
    private String contentType;
}

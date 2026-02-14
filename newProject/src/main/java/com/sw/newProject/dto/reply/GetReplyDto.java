package com.sw.newProject.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetReplyDto extends ReplyDto {
    private String memNm;
    private String profileImageName;
}

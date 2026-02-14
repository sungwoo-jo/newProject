package com.sw.newProject.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TargetOrderDto {
    private Integer groupOrder;
    private Integer groupNo;
    private Integer parentGroupOrder;
    private Integer depth;
    private Integer parentDepth;
    private Integer targetOrder;
    private String boardId;
    private Integer boardNo;
}

package com.sw.newProject.dto;

import lombok.Data;

@Data
public class PageDto {
    int currentPage; // 현재 위치한 페이지의 번호
    int currentBlock; // 현재 위치한 페이지 블럭
    int pageLength; // 보여줄 게시글의 갯수
    int startPage; // 페이지 블록의 시작 번호
    int endPage; // 페이지 블록의 마지막 번호
    int totalPage; // 전체 페이지의 갯수
    int start; // SQL 쿼리의 LIMIT에 들어갈 변수(~번 부터 pageLength개를 출력)
    int totalRows; // 전체 게시글의 갯수
}

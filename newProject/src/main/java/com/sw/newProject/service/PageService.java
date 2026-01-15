package com.sw.newProject.service;

import com.sw.newProject.dto.PageDto;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    /**
     * 게시글 조회 시 전역으로 사용되는 페이징 함수
     * @param page 현재 페이지 번호
     * @param pageLength 페이징 할 갯수
     * @param totalRows 총 페이지의 수
     * @return 페이징 정보를 반환
     */
    public PageDto Paging(String page, int pageLength, int totalRows) {
        PageDto pageDto = new PageDto();
        pageDto.setPageLength(pageLength);
        pageDto.setTotalRows(totalRows);
        try {
            pageDto.setCurrentPage(Integer.parseInt(page));
        } catch (NumberFormatException e) {
            pageDto.setCurrentPage(1);
        }

        pageDto.setTotalPage(pageDto.getTotalRows() % pageDto.getPageLength() == 0 ?
                pageDto.getTotalRows() / pageDto.getPageLength() :
                (pageDto.getTotalRows() / pageDto.getPageLength()) + 1);

        if (pageDto.getCurrentPage() > pageDto.getTotalPage() || pageDto.getCurrentPage() <= 0) {
            pageDto.setCurrentPage(1);
        }

        pageDto.setCurrentBlock(pageDto.getCurrentPage() % pageDto.getPageLength() == 0 ?
                pageDto.getCurrentPage() / pageDto.getPageLength() :
                (pageDto.getCurrentPage() / pageDto.getPageLength()) + 1);

        pageDto.setStartPage((pageDto.getCurrentBlock() - 1) * pageDto.getPageLength() + 1);
        pageDto.setEndPage(pageDto.getStartPage() + pageDto.getPageLength() - 1);

        if (pageDto.getEndPage() > pageDto.getTotalPage()) {
            pageDto.setEndPage(pageDto.getTotalPage());
        }

        pageDto.setStart((pageDto.getCurrentPage() - 1) * pageDto.getPageLength());

        return pageDto;
    }

    Object object = new Object();

}

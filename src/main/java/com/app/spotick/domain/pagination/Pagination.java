package com.app.spotick.domain.pagination;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter @ToString
public class Pagination<T> {
    private int blockSize; // 페이지 한 블록당 크기
    private int currentPage; // 현재 페이지 번호
    private int startPage; // 블록의 시작 번호
    private int endPage; // 블록의 마지막 번호
    private int lastPage; // 전체 페이지 중 마지막 번호
    private boolean hasNextBlock;
    private boolean hasPrevBlock;

    public Pagination(int blockSize, Pageable pageable, Page<T> page){
        this.blockSize = blockSize;
        this.currentPage = pageable.getPageNumber() + 1;

        this.endPage = (int)(Math.ceil(currentPage/(double)blockSize)) * blockSize;
        this.startPage = endPage - blockSize + 1;
        this.lastPage = page.getTotalPages() == 0 ? 1 : page.getTotalPages();

        this.endPage = Math.min(endPage, lastPage);

        this.hasNextBlock = endPage < lastPage;
        this.hasPrevBlock = startPage > 1;
    }

    public boolean hasNextBlock(){
        return endPage < lastPage;
    }

    public boolean hasPrevBlock(){
        return startPage > 1;
    }
}

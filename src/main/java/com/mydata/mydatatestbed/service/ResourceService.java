package com.mydata.mydatatestbed.service;

import com.mydata.mydatatestbed.dto.resource.ResourceDetailResponseDto;
import com.mydata.mydatatestbed.dto.resource.ResourceListResponseDto;
import com.mydata.mydatatestbed.dto.resource.ResourceNavDto;
import com.mydata.mydatatestbed.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResourceService {

    /**
     * 자료 목록 조회
     */
    Page<ResourceListResponseDto> getResourceList(Pageable pageable);

    /**
     * 자료 검색
     */
    Page<ResourceListResponseDto> searchResources(String keyword, Pageable pageable);

    /**
     * 자료 상세 조회 (조회수 증가 포함)
     */
    ResourceDetailResponseDto getResourceDetail(Long id);

    /**
     * 다음 글 조회
     */
    ResourceNavDto getNextResource(Long currentId);

    /**
     * 이전 글 조회
     */
    ResourceNavDto getPrevResource(Long currentId);

    /**
     * 다운로드를 위한 자료 조회 (다운로드 카운트 증가 포함)
     */
    Resource getResourceForDownload(Long id);
}
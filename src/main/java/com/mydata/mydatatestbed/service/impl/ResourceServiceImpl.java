package com.mydata.mydatatestbed.service.impl;

import com.mydata.mydatatestbed.dto.resource.ResourceDetailResponseDto;
import com.mydata.mydatatestbed.dto.resource.ResourceListResponseDto;
import com.mydata.mydatatestbed.dto.resource.ResourceNavDto;
import com.mydata.mydatatestbed.entity.Resource;
import com.mydata.mydatatestbed.mapper.ResourceMapper;
import com.mydata.mydatatestbed.repository.ResourceRepository;
import com.mydata.mydatatestbed.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    @Override
    public Page<ResourceListResponseDto> getResourceList(Pageable pageable) {
        return resourceRepository.findAllWithAuthor(pageable)
                .map(resourceMapper::toListResponseDto);
    }

    @Override
    public Page<ResourceListResponseDto> searchResources(String keyword, Pageable pageable) {
        return resourceRepository.searchByKeyword(keyword, pageable)
                .map(resourceMapper::toListResponseDto);
    }

    @Override
    @Transactional
    public ResourceDetailResponseDto getResourceDetail(Long id) {
        Resource resource = resourceRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("자료를 찾을 수 없습니다: " + id));

        resource.incrementViewCount();

        return resourceMapper.toDetailResponseDto(resource);
    }

    @Override
    public ResourceNavDto getNextResource(Long currentId) {
        return resourceRepository.findNextResource(currentId)
                .map(resourceMapper::toNavDto)
                .orElse(null);
    }

    @Override
    public ResourceNavDto getPrevResource(Long currentId) {
        return resourceRepository.findPrevResource(currentId)
                .map(resourceMapper::toNavDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public Resource getResourceForDownload(Long id) {
        Resource resource = resourceRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("자료를 찾을 수 없습니다: " + id));

        resource.incrementDownloadCount();

        return resource;
    }
}
package com.eju.hookserver.service.business;


import com.eju.hookserver.mapper.dto.BkRequestDto;
import com.eju.hookserver.mapper.dto.BkResponseDto;

/**
 * @author qiushengming
 */
public interface HttpExecute {
    /**
     * 查询
     *
     * @param requestDto 请求DTO
     * @return BkResponseDto
     */
    BkResponseDto execute(BkRequestDto requestDto);
}

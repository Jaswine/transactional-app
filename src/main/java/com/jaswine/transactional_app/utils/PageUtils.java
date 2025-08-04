package com.jaswine.transactional_app.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {

    public static <T> Page<T> toPage(List<T> list, Pageable pageable, long total) {
        return new PageImpl<>(list, pageable, total);
    }
}

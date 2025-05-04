package com.example.staffmngt.dto.res;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ListContentPageDto<T> {
    private long total = 0;
    private long offset = 0;
    private int limit = 0;
    private List<T> content;

    public ListContentPageDto(Page<?> page, List<T> content) {
        this.content = content;
        this.offset = page.getPageable().getOffset() + 1; // by default page's offset is 0
        this.total = page.getTotalElements();
        this.limit = page.getSize();
    }

    public ListContentPageDto(Page<T> page) {
        this.content = page.getContent();
        this.offset = page.getPageable().getOffset() + 1; // by default page's offset is 0
        this.total = page.getTotalElements();
        this.limit = page.getSize();
    }

    public ListContentPageDto() {}

    public ListContentPageDto(long total, long offset, int limit, List<T> content) {
        this.total = total;
        this.offset = offset;
        this.limit = limit;
        this.content = content;
    }

    public long getTotal() {
        return total;
    }

    public long getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public List<T> getContent() {
        return content;
    }

}

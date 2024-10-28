package kr.kro.gonggibap.core.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PageResponse<T> {
    private int totalPages;
    private List<T> content;
}

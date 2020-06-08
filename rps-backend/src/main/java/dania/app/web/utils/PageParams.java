package dania.app.web.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data @NoArgsConstructor @AllArgsConstructor
public class PageParams {
    private Integer page;
    private Integer size;
    private String sortBy;
    private Sort.Direction direction;
}

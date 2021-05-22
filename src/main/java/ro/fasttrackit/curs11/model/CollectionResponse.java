package ro.fasttrackit.curs11.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CollectionResponse<T> {
    List<T> content;
    PageInfo pageInfo;
}

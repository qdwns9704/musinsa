package musinsa.struct.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ListResponse<T> {
    protected List<T> results;

    protected int totalCount;

    public static <T> ListResponse<T> of(List<T> results, int totalCount) {
        return new ListResponse<T>().setResults(results).setTotalCount(totalCount);
    }
}

package vn.ibex.digital_blog.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RestResponse<T> {
    private int statusCode;
    private String error;

    private Object message;
    private T data;


}

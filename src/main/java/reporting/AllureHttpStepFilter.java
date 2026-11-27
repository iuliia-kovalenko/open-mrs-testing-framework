package reporting;

import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public final class AllureHttpStepFilter implements OrderedFilter {
    @Override
    public int getOrder() {
        // Открываем шаг до того, как AllureRestAssured добавит вложения.
        return OrderedFilter.DEFAULT_PRECEDENCE - 1;
    }

    @Override
    public Response filter(FilterableRequestSpecification request,
                           FilterableResponseSpecification response,
                           FilterContext context) {
        String title = request.getMethod() + " " + request.getUserDefinedPath();
        return StepLogger.log(title, () -> context.next(request, response));
    }
}

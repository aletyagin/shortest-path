package search.circle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.LinkedList;

public class Path implements Cloneable {
    private LinkedList<Route> paths = new LinkedList<>();

    public void addRoute(Route path) {
        paths.addLast(path);
    }

    public Path cloneMe() {
        Path clone = new Path();

        paths.forEach(clone::addRoute);

        return clone;
    }

    public Integer length() {
        return
            paths.stream()
                .mapToInt(Route::getDistance)
                    .sum()
        ;
    }

    public JsonArrayBuilder toJson(JsonArrayBuilder builder) {
        for (Route route: paths) {
            builder.add(route.toJson(Json.createArrayBuilder()));
        }

        return builder;
    }
}

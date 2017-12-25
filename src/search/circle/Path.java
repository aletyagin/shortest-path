package search.circle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.LinkedList;

public class Path implements Cloneable {
    final private LinkedList<IRoute> paths = new LinkedList<>();

    private IRoute fullRoute = new NullRoute();

    public void addRoute(IRoute path) {
        paths.addLast(path);
    }

    public Path cloneMe() {
        Path clone = new Path();

        paths.forEach(clone::addRoute);

        return clone;
    }

    public Boolean isLonger(Path path) {
        return this.fullRoute().weight() > path.fullRoute().weight();
    }

    public JsonArrayBuilder asJson() {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        for (IRoute route: paths) {
            builder.add(route.asJson());
        }

        return builder;
    }

    private IRoute fullRoute() {
        if (this.fullRoute.weight() == 0) {
            this.fullRoute =
                paths.stream().reduce(
                    this.fullRoute,
                    (current, path) -> path.addRoute(current)
                )
            ;
        }

        return this.fullRoute;
    }
}

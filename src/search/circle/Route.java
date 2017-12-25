package search.circle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class Route implements IRoute {
    final private Integer from;
    final private Integer to;
    final private Integer weight;

    public Route(Integer from, Integer to, Integer weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public IRoute addRoute(IRoute route) {
        return new Route(from, route.getTo(), weight + route.weight());
    }

    public Integer weight() {
        return weight;
    }

    public Integer getTo() {
        return to;
    }

    public JsonArrayBuilder asJson() {
        return
            Json.createArrayBuilder()
                .add(this.from)
                .add(this.to)
        ;
    }
}

package search.circle;

import javax.json.JsonArrayBuilder;

public class Route{
    private Integer from;
    private Integer to;
    private Integer distance;

    public Route(Integer from, Integer to, Integer distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public JsonArrayBuilder toJson(JsonArrayBuilder builder) {
        builder.add(this.from);
        builder.add(this.to);

        return builder;
    }
}

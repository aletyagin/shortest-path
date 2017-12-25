package search.circle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class Paths implements IPaths {
    private Iterable<Path> list;

    public Paths(Iterable<Path> src) {
        this.list = src;
    }

    public JsonArrayBuilder asJson() {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        this.list.forEach(
            path -> builder.add(path.asJson())
        );

        return builder;
    }
}

package search.circle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.ArrayList;
import java.util.OptionalInt;

public class Paths {
    private ArrayList<Path> paths = new ArrayList<>();
    final private Integer maxWeight;

    public Paths(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void addPath(Path path) {
        this.paths.add(path);
    }

    public Boolean notExists() {
        return this.minLength().equals(maxWeight);
    }

    public JsonArrayBuilder toJson(JsonArrayBuilder builder) {
        for (Path path: paths) {
            builder.add(path.toJson(Json.createArrayBuilder()));
        }

        return builder;
    }

    private Integer minLength() {
        OptionalInt minLength = paths.stream().mapToInt(Path::length).min();
        if (minLength.isPresent()) {
            return minLength.getAsInt();
        }

        return this.maxWeight;
    }
}

package ru.itchyfeet.takes.circle;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class NullRoute implements IRoute {
    @Override
    public Integer weight() {
        return 0;
    }

    @Override
    public IRoute addRoute(IRoute path) {
        return this;
    }

    @Override
    public Integer getTo() {
        return 0;
    }

    @Override
    public JsonArrayBuilder asJson() {
        return Json.createArrayBuilder().addNull();
    }
}

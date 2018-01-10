package ru.itchyfeet.takes.circle;

import javax.json.JsonArrayBuilder;

public interface IRoute {
    Integer weight();

    Integer getTo();

    IRoute addRoute(IRoute path);

    JsonArrayBuilder asJson();
}

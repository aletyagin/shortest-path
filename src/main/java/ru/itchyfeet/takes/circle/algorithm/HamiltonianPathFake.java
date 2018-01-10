package ru.itchyfeet.takes.circle.algorithm;

import org.cactoos.collection.Limited;
import org.cactoos.list.ListOf;
import ru.itchyfeet.takes.circle.IPaths;
import ru.itchyfeet.takes.circle.Path;
import ru.itchyfeet.takes.circle.Paths;
import ru.itchyfeet.takes.circle.Route;

import java.util.ArrayList;
import java.util.List;

public class HamiltonianPathFake implements IHamiltonianPath {

    public IPaths find(int[][] weightMatrix, Integer shortestPathsCount) {
        List<Path> paths = new ArrayList<>();

        Path path = new Path();
        path.addRoute(new Route(0, 1, 1));
        path.addRoute(new Route(1, 0, 2));

        paths.add(path);

        return
            new Paths(
                new Limited<>(
                    shortestPathsCount,
                    new ListOf<>(paths)
                )
            );
    }
}

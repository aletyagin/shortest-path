package ru.itchyfeet.takes.circle.algorithm;

import org.cactoos.collection.Limited;
import org.cactoos.collection.Sorted;
import org.cactoos.list.ListOf;
import ru.itchyfeet.takes.circle.IPaths;
import ru.itchyfeet.takes.circle.Path;
import ru.itchyfeet.takes.circle.Paths;
import ru.itchyfeet.takes.circle.Route;

import java.util.ArrayList;
import java.util.List;

public class HamiltonianPath implements IHamiltonianPath {
    final private Integer INFINITY = 999999999;

    public IPaths find(int[][] weightMatrix, Integer shortestPathsCount) {

        Integer n = weightMatrix.length;
        int m = 1<<n;

        int[][] t = new int[m][n];
        t[1][0] = 0;

        Path[][] pathArray = new Path[m][n];

        for (int i=1; i<m; i+=2) {
            for (int j = (i == 1) ? 1 : 0; j < n; ++j) {
                t[i][j] = INFINITY;
                if (j > 0 && get(j, i)) {
                    int temp = i ^ (1 << j);
                    for (int k = 0; k < n; ++k) {
                        if (get(k, i) && weightMatrix[k][j] > 0) {
                            int tij = t[i][j];
                            int sum = t[temp][k] + weightMatrix[k][j];

                            if (tij > sum) {
                                Path path;

                                if (pathArray[temp] != null) {
                                    if (pathArray[temp][k] != null) {
                                        path = pathArray[temp][k].cloneMe();
                                    } else {
                                        path = new Path();
                                    }
                                } else {
                                    path = new Path();
                                }

                                path.addRoute(new Route(k, j, weightMatrix[k][j]));
                                pathArray[i][j] = path;
                            }

                            t[i][j] = Math.min(tij, sum);
                        }
                    }
                }
            }
        }

        List<Path> paths = new ArrayList<>();
        for (int j=1; j<n; ++j) {
            if (weightMatrix[j][0] > 0) {
                Path path = pathArray[m - 1][j];
                path.addRoute(new Route(j, 0, t[j][0]));
                paths.add(path);
            }
        }

        return
            new Paths(
                new Limited<>(
                    shortestPathsCount,
                    new Sorted<>(
                        (Path a, Path b) -> {
                            if (a.isLonger(b)) {
                                return 1;
                            }

                            return -1;
                        },
                        new ListOf<>(paths)
                    )
                )
            );
    }

    private Boolean get(Integer nmb, Integer x)
    {
        return (x&(1<<nmb))!=0;
    }
}

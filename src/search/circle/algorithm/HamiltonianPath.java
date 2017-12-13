package search.circle.algorithm;

import search.circle.Route;
import search.circle.Paths;
import search.circle.Path;

public class HamiltonianPath {
    final private Integer INFINITY = 999999999;

    public Paths find(int[][] weightMatrix) {

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

        Paths paths = new Paths(INFINITY);

        for (int j=1; j<n; ++j) {
            if (weightMatrix[j][0] > 0) {
                Path path = pathArray[m - 1][j];
                path.addRoute(new Route(j, 0, t[j][0]));

                paths.addPath(path);
            }
        }

        return paths;
    }

    private Boolean get(Integer nmb, Integer x)
    {
        return (x&(1<<nmb))!=0;
    }
}

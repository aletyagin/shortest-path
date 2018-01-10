package ru.itchyfeet.takes.circle.algorithm;

import ru.itchyfeet.takes.circle.IPaths;

public interface IHamiltonianPath {
    IPaths find(int[][] weightMatrix, Integer shortestPathsCount);
}

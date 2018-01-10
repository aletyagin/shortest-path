package ru.itchyfeet.takes;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;
import ru.itchyfeet.takes.circle.algorithm.HamiltonianPathFake;

public class CircleTest {
    @Test
    public void noErrors() throws Exception {
        MatcherAssert.assertThat(
            new RsPrint(
                new Circle(new HamiltonianPathFake())
                    .act(
                        new RqFake("POST", "/circle", "[[1,2],[2,1]]")
                    )
            )
                .printBody(),
            CoreMatchers.equalTo("{\"code\":\"0\",\"message\":\"Some routes was found\",\"routes\":[[[0,1],[1,0]]]}")
        );
    }

    @Test
    public void tooManyElements() throws Exception {
        MatcherAssert.assertThat(
            new RsPrint(
                new Circle(new HamiltonianPathFake())
                    .act(
                        new RqFake("POST", "/circle", "[[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]]")
                    )
            )
                .printBody(),
            CoreMatchers.equalTo("{\"code\":\"1\",\"message\":\"too many elements\"}")
        );
    }

    @Test
    public void firstRowColCountIsGreaterThenOtherRowsColCount() throws Exception {
        MatcherAssert.assertThat(
            new RsPrint(
                new Circle(new HamiltonianPathFake())
                    .act(
                        new RqFake("POST", "/circle", "[[1,1],[1,1,1],[1,1,1]]")
                    )
            )
                .printBody(),
            CoreMatchers.equalTo("{\"code\":\"1\",\"message\":\"invalid request parameter\"}")
        );
    }

    @Test
    public void noSuitableData() throws Exception {
        MatcherAssert.assertThat(
            new RsPrint(
                new Circle(new HamiltonianPathFake())
                    .act(
                        new RqFake("POST", "/circle", "[]")
                    )
            )
                .printBody(),
            CoreMatchers.equalTo("{\"code\":\"1\",\"message\":\"no suitable data\"}")
        );
    }
}

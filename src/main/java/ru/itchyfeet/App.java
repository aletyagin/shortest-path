package ru.itchyfeet;

import org.takes.Response;
import org.takes.facets.fallback.*;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.*;
import org.takes.misc.Opt;
import org.takes.rs.RsText;
import ru.itchyfeet.takes.Circle;
import ru.itchyfeet.takes.TkLogged;
import ru.itchyfeet.takes.circle.algorithm.HamiltonianPath;

public final class App {
    public static void main(final String... args) throws Exception {
        new FtBasic(
            new BkSafe(
                new BkParallel(
                    new BkBasic(
                        new TkFallback(
                            new TkFork(
                                new FkMethods(
                                    "POST",
                                    new TkLogged(
                                        new TkFork(
                                            new FkRegex("/circle", new Circle(new HamiltonianPath()))
                                        )
                                    )
                                )
                            ),
                            new FbChain(
                                new FbStatus(404, new RsText("sorry, page is absent")),
                                new Fallback() {
                                    public Opt<Response> route(final RqFallback req) {
                                        return
                                            new Opt.Single<>(
//                                                new RsText(ExceptionUtils.getStackTrace(req.throwable()))
                                                new RsText("this method is not allowed here")
                                            );
                                    }
                                }
                            )
                        )
                    ),
                    10
                )
            ),
            8080
        )
            .start(Exit.NEVER);
    }
}
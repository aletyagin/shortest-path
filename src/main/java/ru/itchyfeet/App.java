package ru.itchyfeet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takes.Response;
import org.takes.facets.fallback.*;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.*;
import org.takes.misc.Opt;
import org.takes.rs.RsJson;
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
                                    "GET",
                                    new TkFork(
                                        new FkRegex("/checkavailable", new RsText("1"))
                                    )
                                ),
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
                                new Fallback() {
                                    public Opt<Response> route(final RqFallback req) {
                                        final String errorMessage = "{\"code\": 1, \"message\": \"this method is not allowed here\"}";

                                        final Logger logger = LoggerFactory.getLogger("Fallback");
                                        logger.error(errorMessage, req.throwable());

                                        return
                                            new Opt.Single<>(
                                                new RsJson(
                                                    new RsText(errorMessage)
                                                )
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
import org.takes.Response;
import org.takes.facets.fallback.Fallback;
import org.takes.facets.fallback.FbChain;
import org.takes.facets.fallback.FbStatus;
import org.takes.facets.fallback.RqFallback;
import org.takes.facets.fallback.TkFallback;
import org.takes.facets.fork.FkMethods;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.BkBasic;
import org.takes.http.BkParallel;
import org.takes.http.BkSafe;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.misc.Opt;
import org.takes.rs.RsText;
import search.Circle;
import search.circle.algorithm.HamiltonianPath;

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
                                    new TkFork(
                                        new FkRegex("/circle", new Circle(new HamiltonianPath()))
                                    )
                                )
                            ),
                            new FbChain(
                                new FbStatus(404, new RsText("sorry, page is absent")),
                                new FbStatus(405, new RsText("this method is not allowed here")),
                                new Fallback() {
                                    @Override
                                    public Opt<Response> route(final RqFallback req) {
                                        return new Opt.Single<>(
//                                            new RsText(ExceptionUtils.getStackTrace(req.throwable()))
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
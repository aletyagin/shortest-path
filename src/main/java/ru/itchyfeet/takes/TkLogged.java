package ru.itchyfeet.takes;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHeaders;
import org.takes.rq.RqHref;
import org.takes.rq.RqMethod;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.*;

public class TkLogged implements Take {
    /**
     * Original take.
     */
    private final Take origin;

    /**
     * Log target.
     */
    private final String target;

    public TkLogged(final Take take) {
        this.target = take.getClass().getCanonicalName();
        this.origin = take;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public Response act(final Request req) throws IOException {
        final long start = System.currentTimeMillis();

        final Request bufferedRequest =
            new Request() {
                private InputStream body = null;

                public Iterable<String> head() throws IOException {
                    return req.head();
                }

                public InputStream body() throws IOException {
                    if (this.body == null) {
                        this.body = new BufferedInputStream(req.body());
                        this.body.mark(100);
                    }

                    return this.body;
                }
            };

        try {
            this.logRequest(bufferedRequest);

            MDC.put("message_type", "Internal");
            final Response rsp = this.origin.act(bufferedRequest);

            this.logResponse(rsp, start);

            return rsp;
        } catch (final Throwable ex) {
            this.logException(ex, start);
            throw ex;
        }
    }

    private void logRequest(final Request req) throws IOException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("url", new RqHref.Base(req).href().toString());
        builder.add("method", new RqMethod.Base(req).method());
        builder.add("headers", this.headers(req));
        builder.add("body", this.requestBody(req));

        MDC.put("message_type", "Request");
        MDC.put("session_logger_id", new RqHeaders.Base(req).header("Session-Logger-Id").get(0));
        final Logger logger = LoggerFactory.getLogger(this.target);
        logger.info(builder.build().toString());
    }

    private void logResponse(final Response rsp, final long start) throws IOException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("status", rsp.head().iterator().next());
        builder.add("body", this.responseBody(rsp.body()));
        builder.add("execution_time", System.currentTimeMillis() - start);

        MDC.put("message_type", "Response");
        final Logger logger = LoggerFactory.getLogger(this.target);
        logger.info(builder.build().toString());
    }

    private void logException(final Throwable ex, final long start) {
        final Logger logger = LoggerFactory.getLogger(this.target);

        MDC.put("message_type", "Exception");
        logger.info(
            String.format(
                "execution_time = %s",
                System.currentTimeMillis() - start
            ),
            ex
        );
    }

    private JsonObject headers(final Request req) throws IOException {
        RqHeaders.Base headers = new RqHeaders.Base(req);

        JsonObjectBuilder builder = Json.createObjectBuilder();
        headers.names().forEach(
            key -> {
                try {
                    JsonArrayBuilder hdrBuilder = Json.createArrayBuilder();
                    for (String hdr: headers.header(key)) {
                        hdrBuilder.add(hdr);
                    }

                    builder.add(key, hdrBuilder);
                } catch (IOException ex) {
                    /*_*/
                }
            }
        );

        return builder.build();
    }

    private String responseBody(final InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, "UTF-8");
    }

    private String requestBody(final Request req) throws IOException {
        RqHeaders.Base headers = new RqHeaders.Base(req);

        if (headers.header("content-length").size() == 0) {
            return "";
        }

        DataInputStream in = new DataInputStream(req.body());

        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] messageByte = new byte[1000];
            boolean end = false;
            int length = 0;
            int bytesRead;

            while (!end) {
                bytesRead = in.read(messageByte);

                length += bytesRead;

                result.write(messageByte, 0, bytesRead);

                if (Integer.toString(length).equals(headers.header("content-length").get(0))) {
                    end = true;
                }
            }

            req.body().reset();

            return result.toString("UTF-8");
        }
    }
}

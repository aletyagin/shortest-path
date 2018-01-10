package ru.itchyfeet.takes;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;
import ru.itchyfeet.takes.circle.IPaths;
import ru.itchyfeet.takes.circle.algorithm.IHamiltonianPath;

import javax.json.*;
import java.io.IOException;

/**
 * @link http://kursy-programmirovaniya.ru/zadacha-kommivoyazhyora-realizaciya-na-c.html
 */
public class Circle implements Take {
    private IHamiltonianPath hamiltonianPath;

    public Circle (IHamiltonianPath hamiltonianPath) {
        this.hamiltonianPath = hamiltonianPath;
    }

    public Response act(Request request) throws IOException {
        JsonArray data = Json.createReader(request.body()).readArray();

        int rowCount = data.size();

        if (rowCount == 0) {
            return new RsJson(this.errorJsonResponse("no suitable data"));
        }

        int colCount = data.getJsonArray(0).size();

        final int NMAX = 16;
        if (rowCount > NMAX) {
            return new RsJson(this.errorJsonResponse("too many elements"));
        }

        int[][] weightMatrix = new int[rowCount][colCount];

        for (int i=0; i<rowCount; i++) {
            JsonArray row = data.getJsonArray(i);
            if (colCount != row.size()) {
                return new RsJson(this.errorJsonResponse("invalid request parameter"));
            }

            for (int j=0; j<colCount; j++) {
                weightMatrix[i][j] = row.getInt(j);
            }
        }

        IPaths paths = this.hamiltonianPath.find(weightMatrix, 3);

        return
            new RsJson(
                Json.createObjectBuilder()
                    .add("code", "0")
                    .add("message", "Some routes was found")
                    .add("routes", paths.asJson())
                    .build()
            );
    }

    private JsonObject errorJsonResponse(String message) {
        return
            Json.createObjectBuilder()
                .add("code", "1")
                .add("message", message)
                .build()
        ;
    }
}

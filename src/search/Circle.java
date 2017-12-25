package search;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;
import search.circle.IPaths;
import search.circle.algorithm.HamiltonianPath;

import javax.json.*;
import java.io.IOException;

/**
 * @link http://kursy-programmirovaniya.ru/zadacha-kommivoyazhyora-realizaciya-na-c.html
 */
public class Circle implements Take {
    private HamiltonianPath hamiltonianPath;

    public Circle (HamiltonianPath hamiltonianPath) {
        this.hamiltonianPath = hamiltonianPath;
    }

    @Override
    public Response act(Request request) throws IOException {
        JsonArray data = Json.createReader(request.body()).readArray();
        int rowCount = data.size();
        int colCount = data.getJsonArray(0).size();

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

        final int NMAX = 16;

        if (weightMatrix.length > NMAX) {
            return new RsJson(this.errorJsonResponse("too many elements"));
        }

        IPaths paths = this.hamiltonianPath.find(weightMatrix, 3);

        try {
            return
                new RsJson(
                    Json.createObjectBuilder()
                        .add("code", "0")
                        .add("message", "Some routes was found")
                        .add("routes", paths.asJson())
                        .build()
                );
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return new RsJson(this.errorJsonResponse("Some errors occurred"));
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

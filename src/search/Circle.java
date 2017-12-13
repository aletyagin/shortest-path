package search;

import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;
import search.circle.algorithm.HamiltonianPath;
import search.circle.Paths;

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
                return
                    new RsJson(
                        Json.createObjectBuilder()
                            .add("code", "1")
                            .add("message", "invalid request parameter")
                            .build()
                    );
            }

            for (int j=0; j<colCount; j++) {
                weightMatrix[i][j] = row.getInt(j);
            }
        }

        final int NMAX = 16;

        if (weightMatrix.length > NMAX) {
            return
                new RsJson(
                    Json.createObjectBuilder()
                        .add("code", "1")
                        .add("message", "too many elements")
                        .build()
                );
        }

        Paths path = this.hamiltonianPath.find(weightMatrix);

        if (path.notExists()) {
            return
                new RsJson(
                    Json.createObjectBuilder()
                        .add("code", "1")
                        .add("message", "no route found")
                        .build()
                );
        }

        return
            new RsJson(
                Json.createObjectBuilder()
                    .add("code", "0")
                    .add("message", "Some routes was found")
                    .add("routes", path.toJson(Json.createArrayBuilder()))
                    .build()
            );
    }
}

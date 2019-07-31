import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;

public class github {

    private static String gitHubAuthenticationToken;
    private static WebTarget gitHubTarget;

    public static void main(String[] args){
        gitHubAuthenticationToken = System.getenv("GITHUB_AUTH_TOKEN");
        Client client = ClientBuilder.newClient();
        gitHubTarget = client.target("https://api.github.com");
        gitHubTarget = client.target("http://evergreen-1.pp-devcos-evergreen.us-central1.gcp.dev.paypalinc.com:8080");
        String repoOwner = "TheAlgorithms";
        String forkRepo = "Python";
        String botUser = "edwardlhwang";
//        checkUserExists(botUser);
//        if (checkForkExists(botUser, forkRepo)) {
//            deleteGitHubFork(botUser, forkRepo);
//        }
//        createGitHubFork(repoOwner, forkRepo);
       // createPullRequest("edwardlhwang", "apitest","Test Pull Request", "edwardlhwang:mutation.json", "master");
        test();
//        System.out.println("Hi");
    }

    /**
     * Calls /
     * @return
     */
    private static boolean checkForkExists(String botUser, String forkRepo) {
        try {
            JsonObject gitResponse = new JsonParser().parse(gitHubTarget
                    .path("repos/" + botUser + "/" + forkRepo)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "token " + gitHubAuthenticationToken)
                    .get(String.class)).getAsJsonObject();
            if (gitResponse.get("id") == null) {
                System.out.println("Fork Does Not Exist");
                return false;
            }
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }

    private static boolean checkUserExists(String user) {
        try {
            JsonObject gitResponse = new JsonParser().parse(gitHubTarget
                    .path("users/" + user)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "token " + gitHubAuthenticationToken)
                    .get(String.class)).getAsJsonObject();
            if (gitResponse.get("id") == null) {
                System.out.println("Fork Does Not Exist");
                return false;
            }
            System.out.println("checkUserExits" + gitResponse);
        } catch (NotFoundException e) {
            return false;
        }
        return true;
    }


    private static void deleteGitHubFork(String botUser, String forkRepo) {
        gitHubTarget.path("repos/" + botUser + "/" + forkRepo)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "token " + gitHubAuthenticationToken)
                .delete(String.class);
        System.out.println("Delete:");
    }

    private static void createGitHubFork(String repoOwner, String forkRepo) {
        String forkPath = "repos/" + repoOwner + "/" + forkRepo + "/forks";
        System.out.println(forkPath);
        JsonObject gitResponse = new JsonParser().parse(gitHubTarget
                .path(forkPath)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "token " + gitHubAuthenticationToken)
                .post(Entity.json(null), String.class)).getAsJsonObject();
        System.out.println(gitResponse);
    }

    private static void createPullRequest(String owner, String repo, String title, String head, String base){
        String input = String.format("{ \"title\": \"%s\", \"head\": \"%s\", \"base\": \"%s\" }", title, head, base);
        JsonObject gitResponse = new JsonParser().parse(gitHubTarget
                .path("repos/" + owner + "/" + repo + "/pulls")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "token " + gitHubAuthenticationToken)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), String.class)).getAsJsonObject();
    }

    private static void test() {
        String query = "{ \"query\": \"query{getUpdatePlansOrderedDescByLastChange(offset:0,limit:10) { id } }\" }";
        JsonObject gitResponse = new JsonParser().parse(gitHubTarget
                .path("graphql")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(query, MediaType.APPLICATION_JSON), String.class)).getAsJsonObject();
        System.out.println(gitResponse);
    }

}

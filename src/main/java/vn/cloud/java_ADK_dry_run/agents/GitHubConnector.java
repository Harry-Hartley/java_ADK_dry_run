package vn.cloud.java_ADK_dry_run.agents;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class GitHubConnector {

    private final GitHub github;

    public GitHubConnector() throws IOException {

        String githubToken = System.getenv("GITHUB_TOKEN");

        if (githubToken == null || githubToken.isEmpty()) {
            throw new IOException("Error: GITHUB_TOKEN environment variable not set.");
        }

        this.github = new GitHubBuilder().withOAuthToken(githubToken).build();
        this.github.checkApiUrlValidity();
        System.out.println("Successfully authenticated with GitHub.");
    }


    public String readFileFromRepo(String repoName, String filePath, String branch) throws IOException {
        System.out.printf("Attempting to read file '%s' from repo '%s' on branch '%s'%n", filePath, repoName, branch);

        GHRepository repository = github.getRepository(repoName);
        GHContent fileContent = repository.getFileContent(filePath, branch);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent.read(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) {
        try {
            GitHubConnector connector = new GitHubConnector();

            String repository = "Harry-Hartley/java_ADK_dry_run";
            String fileToRead = "";
            String branchName = "Harry"; // Or "master"

            String content = connector.readFileFromRepo(repository, fileToRead, branchName);

            System.out.println("\n--- FILE CONTENT ---");
            System.out.println(content);

        } catch (IOException e) {
            System.err.println("An error occurred while accessing GitHub: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
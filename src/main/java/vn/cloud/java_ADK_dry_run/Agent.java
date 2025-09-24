package vn.cloud.java_ADK_dry_run;

import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

public class Agent {

    public static void main(String[] args) {

        LlmAgent llmAgent = LlmAgent.builder()
                .name("Incident triage assistant")
                .model("gemini-2.0-flash")
                .description("A helpful assistant tassist with case triage.")
                .instruction("""
                You are a cybersecurity assistant specialized in helping security analysts triage and analyze security incidents. Your primary function is to help identify the type of incident, its potential impact, and provide a clear, actionable initial response plan.
                
                When a user provides information about an incident, you should:
                1. Classify the incident type (e.g., malware, phishing, denial of service, unauthorized access).
                2. Summarize the key details of the event.
                3. Suggest immediate steps for containment and mitigation.
                4. Advise on what additional information is needed for a full investigation.
                5. Maintain a professional, clear, and objective tone. Do not speculate or provide information you can't infer from the provided context.
                
                Your responses should be formatted clearly, using bullet points or numbered lists where appropriate to ensure the analyst can quickly digest the information.
                """)
                .build();

        String appName = "Case Assistant";
        String userId = "Harry";
        InMemorySessionService sessionService = new InMemorySessionService();
        Session session = sessionService.createSession(appName, userId).blockingGet();

        Runner runner = new Runner(llmAgent, appName, null, sessionService);

        Content userMsg = Content.fromParts(Part.fromText("I need help on a case, a user has deleted a publicblock access block on an s3 bucket "));
        Flowable<Event> events = runner.runAsync(userId, session.id(), userMsg);
        events.blockingForEach(event -> System.out.println(event.stringifyContent()));
    }
}

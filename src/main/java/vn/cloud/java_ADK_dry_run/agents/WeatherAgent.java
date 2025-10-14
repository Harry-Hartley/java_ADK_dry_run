package vn.cloud.java_ADK_dry_run.agents;

import com.google.adk.agents.LlmAgent;

public class WeatherAgent {

    public static LlmAgent ROOT_AGENT = LlmAgent.builder()
            .name("WeatherAgent")
            .model("gemini-2.0-flash")
            .description("Agent to answer questions about the time and weather in a city.")
            .instruction("You are a helpful agent who can answer user questions about the time and weather in a city.")
            .build();
}
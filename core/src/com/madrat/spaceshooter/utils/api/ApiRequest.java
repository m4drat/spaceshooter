package com.madrat.spaceshooter.utils.api;

import com.madrat.spaceshooter.utils.api.resourcereprs.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;

import java.util.List;

public class ApiRequest {

    private ClientConfig config;
    private Client client;

    public ApiRequest() {
        config = new DefaultClientConfig();
        // Set up jackson not to crash, if it will receive non standart user fields
        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        config.getSingletons().add(jacksonJsonProvider);
        config.getClasses().add(JacksonJaxbJsonProvider.class);
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        client = Client.create(config);

        // Set timeouts for connection (will throw exception)
        client.setConnectTimeout(5000);
        client.setReadTimeout(5000);
    }

    // SendScore endpoint
    public void sendScore(User user, String apiUrl, String apiEndpoint) {
        WebResource resource = client.resource(apiUrl);

        // Actual request
        resource.path(apiEndpoint).type("application/json").post(user);
    }

    // Get scoreboard endpoint
    public List<User> getScoreBoard(String apiUrl, String apiEndpoint, int count) throws Exception {
        WebResource resource = client.resource(apiUrl);

        // Actual request
        ClientResponse response = resource.path(apiEndpoint).queryParam("count", Integer.toString(count)).accept("application/json").get(ClientResponse.class);
        List<User> users = response.getEntity(new GenericType<List<User>>() {
        });
        return users;
    }
}

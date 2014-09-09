package org.lds.mediafinder.utils.jira;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.lds.stack.tomcat.decrypt.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class JiraProjectDao {

    private String restBase = "https://code.ldschurch.org/jira/rest/api/2/";
    private String userpass = "aqat:p@ssw0rd";

    /**
     * CURRENTLY BROKEN: provide a file for attachment to the given issue
     *
     * @param issueKey
     * @param attachment
     * @throws Exception
     */
    public void attachFileToIssue(String issueKey, File attachment) throws Exception {

        //byte[] fileBytes = getBytesFromFile(attachment);

        String apiUrl = restBase + "issue/" + issueKey + "/attachments";
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "multipart/form-data; boundary=\"" + generateBoundary() + "\"");
        headers.add("X-Atlassian-Token", "nocheck");
        headers.add("Authorization", "Basic " + Base64.byteArrayToBase64(userpass.getBytes()));
        headers.add("content-disposition", "form-data; name=\"Failure-" + System.currentTimeMillis() + "\"; filename=\"" + attachment.getName() + "\"");

        MultiValueMap<String, File> parameters = new LinkedMultiValueMap<String, File>();
        parameters.add("file", attachment);
        getRt().postForLocation(apiUrl, new HttpEntity<MultiValueMap<String, File>>(parameters, headers));

    }

    /**
     * Set the transition to an issue with optional comment
     *
     * @param issueKey
     * @param actionId
     * @param remoteFieldValues
     * @return
     */
    public boolean setTransition(String issueKey, Integer actionId, String comment) throws Exception {

        boolean result = false;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonBody = mapper.createObjectNode();
        Object transition = jsonBody.putObject("transition");
        ((ObjectNode) transition).put("id", actionId);
        Object fields = jsonBody.putObject("fields");
        Object reasonField = ((ObjectNode) fields).putObject("customfield_10642");
        ((ObjectNode) reasonField).put("value", "Automation");

        if (comment != null) {
            Object updateObj = jsonBody.putObject("update");
            Object commentObj = ((ObjectNode) updateObj).putArray("comment");
            Object addObj = ((ArrayNode) commentObj).insertObject(0).putObject("add");
            ((ObjectNode) addObj).put("body", comment);
        }

        String apiUrl = restBase + "issue/{issueKey}/transitions";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("Authorization", "Basic " + Base64.byteArrayToBase64(userpass.getBytes()));

        try {
            getRt().exchange(apiUrl, HttpMethod.POST, new HttpEntity<ObjectNode>(jsonBody, headers), String.class, issueKey);
            result = true;
        } catch (Exception e) {
            throw new Exception("Failed to set transition [" + actionId + "] for issue [" + issueKey + "]\n\n" + e.getMessage());
        }

        return result;
    }

    /**
     *
     * @param issueKey
     * @param actionId
     * @param remoteFieldValues
     * @return
     */
    public String getCurrentStatus(String issueKey) throws Exception {

        String apiUrl = restBase + "issue/{issueKey}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("Authorization", "Basic " + Base64.byteArrayToBase64(userpass.getBytes()));

        ResponseEntity<String> reponseEntity = getRt().exchange(apiUrl, HttpMethod.GET, new HttpEntity<String>(headers), String.class, issueKey);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode issue = mapper.readValue(reponseEntity.getBody(), JsonNode.class);
        JsonNode fields = issue.path("fields");
        String status = fields.path("status").path("name").getTextValue();
        return status;

    }

    /**
     *
     * @param issueKey
     * @return
     */
    public String getIssueByKey(String issueKey) {

        String apiUrl = restBase + "issue/{issueKey}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("Authorization", "Basic " + Base64.byteArrayToBase64(userpass.getBytes()));

        ResponseEntity<String> reponseEntity = getRt().exchange(apiUrl, HttpMethod.GET, new HttpEntity<String>(headers), String.class, issueKey);
        return reponseEntity.getBody();

    }

    /**
     * create a rest template handler
     *
     * @return
     */
    private RestTemplate getRt() {

        RestTemplate rt = new RestTemplate();

//		List<HttpMessageConverter<?>> converters = rt.getMessageConverters();
//		HttpMessageConverter<?> formHttpMessageConverter = new FormHttpMessageConverter();
//		HttpMessageConverter<?> stringHttpMessageConverternew = new StringHttpMessageConverter();
//		converters.add(formHttpMessageConverter);
//		converters.add(stringHttpMessageConverternew);
//		rt.setMessageConverters(converters);

        rt.setErrorHandler(new ResponseErrorHandler() {

            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {

                Series series = response.getStatusCode().series();
                if (series.equals(Series.SERVER_ERROR) || series.equals(Series.CLIENT_ERROR)) {
                    return true;
                }

                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                InputStream is = response.getBody();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuilder sb = new StringBuilder();
                String read;
                while ((read = br.readLine()) != null) {
                    sb.append(read);
                    sb.append("\n");
                }

                throw new RuntimeException(String.format("body: %s -- status text: %s -- status code: %s",
                        sb.toString(), response.getStatusText(), response.getStatusCode()));
            }
        });

        return rt;

    }

    protected String generateBoundary() {

        final char[] MULTIPART_CHARS =
                "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

    protected static byte[] getBytesFromFile(File file) throws IOException {
        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large!");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        return bytes;
    }
}

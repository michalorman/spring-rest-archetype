#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.meterware.httpunit.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

@SuppressWarnings({ "unchecked" })
public class UsersTest {

    @Test
    public void testConnection() throws Exception {
        WebConversation conversation = new WebConversation();
        // should cause 405 if not request parameters provided
        WebRequest request = new GetMethodWebRequest("http://localhost:8081/${artifactId}/users");
        try {
            conversation.getResponse(request);
            fail("Should respond with 405 (not supported) if no request parameters provided");
        } catch (HttpException e) {
            // ok, we expected that
            assertEquals(405, e.getResponseCode());
        }
    }

    @Test
    public void testCreate() throws Exception {
        WebConversation conversation = new WebConversation();
        WebRequest request = new PostMethodWebRequest("http://localhost:8081/${artifactId}/users");
        request.setParameter("firstName", "Dave");
        request.setParameter("lastName", "Developer");
        WebResponse response = conversation.getResponse(request);
        assertEquals(200, response.getResponseCode());
        Number userId = assertResponse(response);

        request = new GetMethodWebRequest("http://localhost:8081/${artifactId}/users/" + userId);
        response = conversation.getResponse(request);
        assertEquals(200, response.getResponseCode());
        assertResponse(response);
    }

    private Number assertResponse(WebResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(response.getText(), Map.class);
        Number userId = (Number) result.get("id");
        assertNotNull(userId);
        assertEquals("Dave", result.get("firstName"));
        assertEquals("Developer", result.get("lastName"));
        return userId;
    }

}

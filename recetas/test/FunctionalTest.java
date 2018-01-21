import org.junit.Test;

import play.mvc.Http.RequestBuilder;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * A functional test starts a Play application for every test.
 *
 * https://www.playframework.com/documentation/latest/JavaFunctionalTest
 */
public class FunctionalTest extends WithApplication {


    
    @Test
    public void testPath() {

    RequestBuilder req = Helpers.fakeRequest()
	    .method("GET")
	    .uri("/recetas/0?APIKey=123")
	    .header("Accept", "application/xml")
	    .bodyText(""); 
	    Result r = Helpers.route(app, req);
	    assertThat(r.status()).isEqualTo(200);
    }
    
}

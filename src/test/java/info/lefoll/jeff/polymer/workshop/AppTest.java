package info.lefoll.jeff.polymer.workshop;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import org.jooby.test.JoobyRule;
import org.jooby.test.MockRouter;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author jooby generator
 */
public class AppTest {

  /**
   * One app/server for all the test of this class. If you want to start/stop a new server per test,
   * remove the static modifier and replace the {@link ClassRule} annotation with {@link Rule}.
   */
  @ClassRule
  public static JoobyRule app = new JoobyRule(new App());

  @Test
  public void integrationTest() {
    get("/")
        .then()
        .assertThat()
        .body(equalTo("\"I'm alive !!\""))
        .statusCode(200)
        .contentType("application/json;charset=UTF-8");
  }
}

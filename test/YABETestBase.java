import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import play.test.FakeApplication;
import play.test.Helpers;

import java.io.IOException;

public class YABETestBase {
        public static FakeApplication app;
        public static DdlGenerator ddl;
        public static EbeanServer server;

        @BeforeClass
        public static void setup() {
            app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
            Helpers.start(app);

            server = Ebean.getServer("default");

            ServerConfig config = new ServerConfig();

            ddl = new DdlGenerator();
            ddl.setup((SpiEbeanServer) server, new H2Platform(), config);
        }

        @AfterClass
        public static void stopApp() {
            Helpers.stop(app);
        }

        @Before
        public void resetDb() throws IOException {

            String dropScript = ddl.generateDropDdl();
            Ebean.execute(Ebean.createCallableSql(dropScript));

            String createScript = ddl.generateCreateDdl();
            Ebean.execute(Ebean.createCallableSql(createScript));

        }
}

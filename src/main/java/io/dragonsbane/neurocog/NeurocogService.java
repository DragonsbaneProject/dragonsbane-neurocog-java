package io.dragonsbane.neurocog;

import io.dragonsbane.neurocog.tests.ImpairmentTest;
import io.dragonsbane.neurocog.tests.SaveTestRequest;
import io.onemfive.core.BaseService;
import io.onemfive.data.DID;
import io.onemfive.data.Envelope;
import io.onemfive.data.Route;
import io.onemfive.data.util.DLC;
import io.onemfive.neo4j.Neo4jDB;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.Date;
import java.util.Properties;

/**
 * Long-running asynchronous background service with orchestrations across multiple services residing in the bus.
 *
 * @author objectorange
 */
public class NeurocogService extends BaseService {

    public static final String OPERATION_SAVE_TEST = "SAVE_TEST";

    private Neo4jDB db = (Neo4jDB)infoVaultDB;

    @Override
    public void handleDocument(Envelope e) {
        Route r = e.getRoute();
        switch(r.getOperation()) {
            case OPERATION_SAVE_TEST: {
                SaveTestRequest req = (SaveTestRequest) DLC.getData(SaveTestRequest.class,e);
                if(req == null) {
                    req = new SaveTestRequest();
                    req.errorCode = SaveTestRequest.REQUEST_REQUIRED;
                    DLC.addData(SaveTestRequest.class,req,e);
                    break;
                }
                if(req.test == null) {
                    req.errorCode = SaveTestRequest.TEST_REQUIRED;
                    break;
                }
                saveTest(req);
                break;
            }
        }
    }

    private void saveTest(SaveTestRequest r) {
        try (Transaction tx = db.getGraphDb().beginTx()) {
            Node did = db.getGraphDb().findNode(Label.label(DID.class.getName()),"alias", r.did.getAlias());
            if(did==null) {
                did = db.getGraphDb().createNode(Label.label(DID.class.getName()));
                did.getAllProperties().putAll(r.did.toMap());
            }
            Node test = db.getGraphDb().createNode(Label.label(ImpairmentTest.class.getName()));
            test.getAllProperties().putAll(r.test.toMap());
            Relationship rel = did.createRelationshipTo(test, RelTypes.TOOK);
            rel.setProperty("date",new Date().getTime());
            tx.success();
        }
    }

    @Override
    public boolean start(Properties properties) {
        return super.start(properties);
    }

    @Override
    public boolean shutdown() {
        return super.shutdown();
    }

    @Override
    public boolean gracefulShutdown() {
        return super.gracefulShutdown();
    }
}

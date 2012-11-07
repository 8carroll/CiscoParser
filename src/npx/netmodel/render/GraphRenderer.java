package npx.netmodel.render;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.util.io.graphml.GraphMLWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import npx.netmodel.config.LogicalNetworkElem;
import npx.netmodel.config.NetworkInterface;
import npx.netmodel.config.PassiveConfig;
import npx.utilities.ipv4Helper;

/**
 *
 * @author Luis Dias Costa <luis@theincrediblemachine.org>
 */
public class GraphRenderer extends Renderer {

	private Graph graph;
	private boolean done = false;
	/**
	 * Group network nodes
	 */
//	private boolean groupNetworkEndings = true;

	public GraphRenderer(List<PassiveConfig> configList) {
		super(configList);
	}

	private void computeGraph() {

		if (done) {
			return;
		}

		Map<String, Vertex> segment2vertex = new HashMap<String, Vertex>();

		graph = new TinkerGraph();
		for (PassiveConfig passiveConfig : getConfigList()) {

			LogicalNetworkElem lne = passiveConfig.getLogicalNetworkElem();

			logger.trace("lne  " + lne);


			Vertex n = graph.getVertex(lne.getHostname());
			if (n == null) {
				n = graph.addVertex(lne.getHostname());
			}
			n.setProperty("type", 0); // a network logical/physical element

			ArrayList<NetworkInterface> nis =
					new ArrayList<NetworkInterface>(passiveConfig.getNetworkInterfaces());

			if (nis.size() > 0) {
				for (NetworkInterface ni : nis) {
					if (ni.getNetworkAddress() != null) {

						String networkString = ipv4Helper.getNetworkString(
								ni.getNetworkAddress().getAddress(),
								ni.getNetworkAddress().getMask());

						Vertex s = segment2vertex.get(networkString);

						if (s == null) { // Vertex does not exist in graph
							s = graph.addVertex(networkString);
							segment2vertex.put(networkString, s);
						}
						s.setProperty("type", 1); // a network segment

						/**
						 * finally, create an edge between host and segment
						 * Vertexes, representing a logical L3 link between
						 * an interface and the correspondent network
						 * segment
						 */
						graph.addEdge(null, n, s, networkString);
					} 
					/*else {
                            logger.trace("LNE: " + lne.getHostname() + " " + ni.getName() + " interface has no addresses");
                        }*/
				}
			} else {
				logger.trace(lne.getHostname() + "{0} has no physical interfaces! ");
			}

			// load the routing table and associate it to the graph Vertex
			//getRoutingTable(lne, n);
		}


		/*
		 * Checkup the resulting graph
		 */
		//analyzeGraph();

		/*
		 * Graph display Optimization methods
		 */
		//createAggregateSegmentFolders();

		done = true;
	}

	@Override
	public Graph getResults() {
		computeGraph();
		return graph;
	}

	/**
	 * Get the GraphML representation of the current graph
	 */
	public ByteArrayOutputStream getGraphMLResults() {

		computeGraph();

		// serialize graph to graphml
		GraphMLWriter writer = new GraphMLWriter(graph);
		writer.setNormalize(true);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			writer.outputGraph(out);
		} catch (IOException ex) {
			logger.error("Error writing graph to output stream: " + ex);
		}
		return out;

	}

	public Neo4jGraph getNeo4jGraph(String dir) {

		computeGraph();

		//@TODO java.lang.NoClassDefFoundError: org/neo4j/graphdb/GraphDatabaseService
		//        Neo4jGraph ng = new Neo4jGraph(dir);
		//
		//        for (Vertex v : graph.getVertices()) {
		//            Vertex nv = ng.addVertex(v.getId());
		//
		//            for (String k : v.getPropertyKeys()) {
		//                nv.setProperty(k, v.getProperty(k));
		//            }
		//        }
		//        
		//        for (Edge e : graph.getEdges()) {
		//            Edge ne = ng.addEdge(e.getId(), e.getOutVertex(),e.getInVertex(), e.getLabel());
		//            
		//            for (String k : e.getPropertyKeys()) {
		//                ne.setProperty(k, e.getProperty(k));
		//            }
		//        }
		//
		//        return ng;
		return null;
	}

	private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
}
